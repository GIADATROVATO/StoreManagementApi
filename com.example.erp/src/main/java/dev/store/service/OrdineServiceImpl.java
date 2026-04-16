package dev.store.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.store.dto.OrdineDTO;
import dev.store.entity.Cliente;
import dev.store.entity.Ordine;
import dev.store.entity.StatoOrdine;
import dev.store.repository.ClienteRepository;
import dev.store.repository.OrdineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrdineServiceImpl implements OrdineService{
	private final OrdineRepository repository; 
	private final ClienteRepository clienteRepo;
	private final static Logger log= LoggerFactory.getLogger(OrdineServiceImpl.class);
	
	/*
	 * 	invece di creare una lista e gestire la logica nel service, la porto dentro l'enum 
	 * 
	private static final Map<StatoOrdine, List<StatoOrdine>> transizioni=Map.of(
			StatoOrdine.CREATED, List.of(StatoOrdine.PENDING),
			StatoOrdine.PENDING, List.of(StatoOrdine.SHIPPED),
			StatoOrdine.SHIPPED, List.of(StatoOrdine.COMPLETED));
	*/
	
	public OrdineServiceImpl(OrdineRepository repository, ClienteRepository clienteRepo ) {
		this.repository=repository;
		this.clienteRepo=clienteRepo;
	}
	
	/*
	public void validaTransizione( StatoOrdine corrente, StatoOrdine nuovo) {
		if(!transizioni.getOrDefault(corrente,List.of()).contains(nuovo)) {
			throw new RuntimeException("Transazione non valida");
		}
	}
	*/
	
	public void validaOrdine(Ordine o) {
		if(o.getTotale()==null || o.getTotale().compareTo(BigDecimal.ZERO)<=0) 
		{ throw new RuntimeException("totale invalido");
			
		}
	}
	
	@Override
	public OrdineDTO createOrdine(OrdineDTO dto, Long clienteId) {
		Ordine o= new Ordine();
		mapToEntity(o,dto);
		Cliente cliente= clienteRepo.findById(clienteId).orElseThrow(()->new RuntimeException("cliente non trovato"));
		o.setCliente(cliente);
		
		if(!o.isDisponibile()) { throw new RuntimeException("articolo non disponibile"); }
		validaOrdine(o);
		
		o.setStato(StatoOrdine.CREATED);
		Ordine saved=repository.save(o);
		return mapToDto(saved);
	}
	public OrdineDTO processaOrdine(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new RuntimeException("ordine non trovato"));
		
		o.getStato().validaTransizione(StatoOrdine.PENDING);
		
		if(!o.isDisponibile()) {throw new RuntimeException("articolo non processabile");}
		o.setStato(StatoOrdine.PENDING);
		o.setDataInizio(LocalDateTime.now());
	
		return mapToDto(repository.save(o));
		
	}
	public OrdineDTO spedizione(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new RuntimeException("ordine non trovato"));
		
		o.getStato().validaTransizione(StatoOrdine.SHIPPED);
		if(!o.isDisponibile()) {throw new RuntimeException("ordine non processabile");}
		if(o.getTotale()==null) {throw new RuntimeException("totale mancante");}
		logDurataPartenze(o);
		o.setStato(StatoOrdine.SHIPPED);
		o.setDataSpedizione(LocalDateTime.now());
		
		return mapToDto(repository.save(o));
	}
	public OrdineDTO completaOrdine(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new RuntimeException("ordine non trovato"));
		
		o.getStato().validaTransizione(StatoOrdine.COMPLETED);
		if(o.getDataInizio()!=null && o.getDataSpedizione()!=null && 
				o.getDataSpedizione().isBefore(o.getDataInizio())) {throw new RuntimeException("date incoerenti");}
		validaOrdine(o);
		o.setStato(StatoOrdine.COMPLETED);
		o.setDataFine(LocalDateTime.now());
		
		return mapToDto(repository.save(o));
	}
	
	@Override
	public List<OrdineDTO> findByStato(StatoOrdine stato) {
		// TODO Auto-generated method stub
		return repository.findByStatus(stato).stream().map(this::mapToDto).toList();
	}

	public Map<StatoOrdine,Long> contaPerStato(){
		return repository.findAll().stream().collect(Collectors.groupingBy( 
				p->p.getStato(),Collectors.counting()));
	}
	public Map<Month,Long> ordiniPerMese(){
		return repository.findAll().stream().collect(Collectors.groupingBy
				( p->p.getDataSpedizione().getMonth(), Collectors.counting()));
				
	}
	@Override
	public Optional<OrdineDTO> findById(Long id) {
		return repository.findById(id).map(this::mapToDto);
		 
	}

	@Override
	public Optional<OrdineDTO> update(OrdineDTO dto, Long id) {
		Ordine d= repository.findById(id).orElseThrow(()-> new RuntimeException("ordine non modificabile"));
		
		if(d.getDataSpedizione()!=null && d.getStato()==StatoOrdine.SHIPPED) 
			{throw new RuntimeException("ordine non modificabile");}
		mapToEntity(d,dto);
		Ordine update= repository.save(d);
		return Optional.of(mapToDto(update));
	}

	@Override
	public boolean delete(Long id) {
		Optional<Ordine> o= repository.findById(id);
		if(o.isEmpty())
		return false;
		Ordine ordine= o.get();
		if(ordine.getStato()!=StatoOrdine.CREATED)	{throw new RuntimeException("ordine non eliminabile");}
		repository.delete(ordine);
		return true;
	}
	public void logDurataPartenze(Ordine o) {
			Long durata=ChronoUnit.DAYS.between(o.getDataInizio(), o.getDataSpedizione());
			log.info("identificativo {} spedito in {} giorni ",o.getId(),durata);
	}
	public OrdineDTO modificaDisponibilita(Long id, boolean disponibile) {
		Ordine o= repository.findById(id).orElseThrow();
		o.setDisponibile(disponibile);
		repository.save(o);
		return mapToDto(o);
		
	}
	
	// ===================================
	//	MAPPING   
	// ===================================	
	public OrdineDTO mapToDto(Ordine o) {
		OrdineDTO dto= new OrdineDTO();
		if(o!=null){
			dto.setDataFine(o.getDataFine());
			dto.setDataInizio(o.getDataInizio());
			dto.setTotale(o.getTotale());
			dto.setDisponibile(o.isDisponibile());
			dto.setDataSpedizione(o.getDataSpedizione());
			dto.setStato(o.getStato());
			
		}
		return dto;
	}
	public void mapToEntity(Ordine o, OrdineDTO dto) {
		o.setDataFine(dto.getDataFine());
		o.setDataInizio(dto.getDataInizio());
		o.setTotale(dto.getTotale());
		o.setDisponibile(dto.isDisponibile());
	}
}
