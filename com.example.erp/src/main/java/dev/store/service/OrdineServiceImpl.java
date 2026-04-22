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
import dev.store.entity.EntityType;
import dev.store.entity.Operazione;
import dev.store.entity.Ordine;
import dev.store.entity.StatoOrdine;
import dev.store.exception.ArticoloNonDisponibileException;
import dev.store.exception.ClienteNotFoundException;
import dev.store.exception.OrdineNonModificabileException;
import dev.store.exception.OrdineNotFoundException;
import dev.store.repository.ClienteRepository;
import dev.store.repository.OrdineRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrdineServiceImpl implements OrdineService{
	private final OrdineRepository repository; 
	private final ClienteRepository clienteRepo;
	private final IntegrationService integrationService;
	
	private final static Logger log= LoggerFactory.getLogger(OrdineServiceImpl.class);


	
	/*
	 * 	invece di creare una lista e gestire la logica nel service, la porto dentro l'enum 
	 * 
	private static final Map<StatoOrdine, List<StatoOrdine>> transizioni=Map.of(
			StatoOrdine.CREATED, List.of(StatoOrdine.PENDING),
			StatoOrdine.PENDING, List.of(StatoOrdine.SHIPPED),
			StatoOrdine.SHIPPED, List.of(StatoOrdine.COMPLETED));
	*/
	
	public OrdineServiceImpl(OrdineRepository repository, ClienteRepository clienteRepo,
			IntegrationService integration) {
		this.repository=repository;
		this.clienteRepo=clienteRepo;
		this.integrationService=integration;
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
		{ throw new ArticoloNonDisponibileException("totale invalido");
			
		}
	}
	
	@Override
	public OrdineDTO createOrdine(OrdineDTO dto, Long clienteId) {
		Ordine o= new Ordine();
		mapToEntity(o,dto);
		Cliente cliente= clienteRepo.findById(clienteId).orElseThrow(()->new ClienteNotFoundException("cliente non trovato"));
		o.setCliente(cliente);
		
		if(!o.isDisponibile()) { throw new ArticoloNonDisponibileException("articolo non disponibile"); }
		validaOrdine(o);
		
		o.setStatoOrdine(StatoOrdine.CREATED);
		Ordine saved=repository.save(o);
		
		integrationService.salvaEvento(saved,EntityType.ORDINE, Operazione.CREATE);

		
		return mapToDto(saved);
	}
	@Override
	public Optional<OrdineDTO> update(OrdineDTO dto, Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new OrdineNonModificabileException("ordine non modificabile"));
		
		if(o.getDataSpedizione()!=null && o.getStatoOrdine()==StatoOrdine.SHIPPED) 
			{throw new 	OrdineNonModificabileException("ordine non modificabile");}
		aggiornaOrdine(dto,o);
		/*
		 *  sostuisco mapToEntity con aggiornaOrdine per avere un aggiornamento controllato e bloccare campi critici
		 */
		Ordine update= repository.save(o);
		
		integrationService.salvaEvento(update,EntityType.ORDINE, Operazione.UPDATE);
		
		return Optional.of(mapToDto(update));
	}

	@Override
	public boolean delete(Long id) {
		Ordine ordine= repository.findById(id).orElseThrow(()->new OrdineNotFoundException("ordine non trovato"));
		
		if(ordine.getStatoOrdine()!=StatoOrdine.CREATED)	{throw new OrdineNonModificabileException("ordine non eliminabile");}
		
		integrationService.salvaEvento(ordine,EntityType.ORDINE, Operazione.DELETE);
		ordine.setStatoOrdine(StatoOrdine.DELETED);
		repository.save(ordine);
	/*
	 *  SOFT DELETE 
	 *  Il record resta nel DB, ma è logicamente eliminato. Salvo l'evento DELETE, l'ordine però resta nel db
	 *  	
	 */
		
		return true;
	}
	
	/*
	 *  non uso più questi 3 metodi, ma creo IntegrationService per avere una separazione di responsabilità, niente logica ERP nel 
	 *  service principale 
	 * 
	private OrdineIntegration buildIntegration(Ordine o, Operazione operazione, String payload ) {
		OrdineIntegration i= new OrdineIntegration();
		i.setId(o.getId());
		i.setOperazione(operazione);
		i.setPayload(payload);
		i.setRetryCount(0);
		i.setCreatedAt(LocalDateTime.now());
		i.setStato(StatoIntegration.PENDING);
		return i;
	}
	private void salvaEvento(Ordine o, Operazione operazione ) {
		integration.save(buildIntegration(o, operazione, mapToJson(o)));
	}
	
	private String mapToJson(Ordine saved) {
		try {
			return objectMapper.writeValueAsString(saved);
		}catch(Exception e) {
			return "{}";
		}
	}
	*/
	
	public OrdineDTO processaOrdine(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new OrdineNotFoundException("ordine non trovato"));
		
		o.getStatoOrdine().validaTransizione(StatoOrdine.PENDING);
		
		if(!o.isDisponibile()) {throw new ArticoloNonDisponibileException("articolo non processabile");}
		o.setStatoOrdine(StatoOrdine.PENDING);
		o.setDataInizio(LocalDateTime.now());
	
		return mapToDto(repository.save(o));
		
	}
	public OrdineDTO spedizione(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new OrdineNotFoundException("ordine non trovato"));
		
		o.getStatoOrdine().validaTransizione(StatoOrdine.SHIPPED);
		if(!o.isDisponibile()) {throw new OrdineNonModificabileException("ordine non processabile");}
		if(o.getTotale()==null) {throw new OrdineNonModificabileException("totale mancante");}
		logDurataPartenze(o);
		o.setStatoOrdine(StatoOrdine.SHIPPED);
		o.setDataSpedizione(LocalDateTime.now());
		
		return mapToDto(repository.save(o));
	}
	public OrdineDTO completaOrdine(Long id) {
		Ordine o= repository.findById(id).orElseThrow(()-> new OrdineNotFoundException("ordine non trovato"));
		
		o.getStatoOrdine().validaTransizione(StatoOrdine.COMPLETED);
		if(o.getDataInizio()!=null && o.getDataSpedizione()!=null && 
				o.getDataSpedizione().isBefore(o.getDataInizio())) {throw new OrdineNonModificabileException("date incoerenti");}
		validaOrdine(o);
		o.setStatoOrdine(StatoOrdine.COMPLETED);
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
				p->p.getStatoOrdine(),Collectors.counting()));
	}
	public Map<Month,Long> ordiniPerMese(){
		return repository.findAll().stream().filter(p->p.getDataSpedizione()!=null && p.getDataSpedizione().getMonth()!=null)
				.collect(Collectors.groupingBy
						( p->p.getDataSpedizione().getMonth(), Collectors.counting()));
				
	}
	@Override
	public OrdineDTO findById(Long id) {
		return repository.findById(id).map(this::mapToDto).orElseThrow(()-> new OrdineNotFoundException("ordine non trovato"));
	}

	
	public void logDurataPartenze(Ordine o) {
			Long durata=ChronoUnit.DAYS.between(o.getDataInizio(), o.getDataSpedizione());
			log.info("identificativo {} spedito in {} giorni ",o.getId(),durata);
	}
	public OrdineDTO modificaDisponibilita(Long id, boolean disponibile) {
		Ordine o= repository.findById(id).orElseThrow(()-> new OrdineNonModificabileException("Disponibilità non modificabile"));
		
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
			dto.setStato(o.getStatoOrdine());
			
		}
		return dto;
	}
	public void mapToEntity(Ordine o, OrdineDTO dto) {
		o.setDataFine(dto.getDataFine());
		o.setDataInizio(dto.getDataInizio());
		o.setTotale(dto.getTotale());
		o.setDisponibile(dto.isDisponibile());
	}
	
	public void aggiornaOrdine(OrdineDTO dto, Ordine o) {
		o.setDisponibile(dto.isDisponibile());
		o.setTotale(dto.getTotale());
		
	}
}
