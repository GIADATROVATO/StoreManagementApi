package dev.store.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.store.dto.ClienteDTO;
import dev.store.dto.ClienteDTODetails;
import dev.store.dto.OrdineDTO;
import dev.store.entity.Cliente;
import dev.store.entity.Ordine;
import dev.store.exception.ClienteNotFoundException;
import dev.store.repository.ClienteRepository;
import dev.store.repository.OrdineRepository;

public class ClienteServiceImpl implements ClienteService{
	private final ClienteRepository clienteRepo;
	private final OrdineRepository repository;  
	
	public ClienteServiceImpl(OrdineRepository repository, ClienteRepository clienteRepo ) {
		this.repository=repository;
		this.clienteRepo=clienteRepo;
	}
	
	@Override
	public ClienteDTO creaCliente(ClienteDTO dto) {
		Cliente cli= new Cliente();
		mapToClienteEntity(dto,cli);
		return mapToDto(clienteRepo.save(cli));
	}
		
	@Override
	public Optional<ClienteDTO> findById(Long id) {
	
		return clienteRepo.findById(id).map(this::mapToDto);
	}
	public ClienteDTODetails findByDetailsId(Long id) {
		Cliente c = clienteRepo.findById(id).orElseThrow(()-> new ClienteNotFoundException("cliente: X"));
		return mapToDetailsDTO(c);
		
	}
	public ClienteDTO update(Long id, ClienteDTO d) {
		Cliente c= clienteRepo.findById(id).orElseThrow(()-> new ClienteNotFoundException("cliente non modificabile"));
		mapToClienteEntity(d,c);
		Cliente saved= clienteRepo.save(c);
		return mapToDto(saved);
	}
	public boolean delete(Long id) {
		Optional<Cliente> c= clienteRepo.findById(id);
		if(c.isEmpty()) return false;
		Cliente result= c.get();
		clienteRepo.delete(result);
		return true;
	}
	public List<ClienteDTO> getAll(){
		return clienteRepo.findAll().stream().map(this::mapToDto).toList();
	}
	
	/*
	 * 		numeroOrdini -> numeroClienti
	 * 			0				5 clienti
	 * 			1				2 clienti
	 */
	public Map<Integer,Long> countClientiPerNumeroOrdini(){
		return clienteRepo.findAll().stream().collect(
				Collectors.groupingBy(p->p.getOrdini()!=null ? p.getOrdini().size() :0, Collectors.counting()))
				;
	}
	/*		
	 * 		clienteID -> numeroOrdini
	 * 			1			3
	 * 			2			0
	 *			3			8 
	 */
	
	/*
	 * 		Collectors.toMap: corrispondenza 1:1 , Collectors.groupingBy: corrispondenza 1:molti 
	 */
	public Map<Long, Long> countOrdiniPerCliente(){
		return clienteRepo.findAll().stream().collect(Collectors.toMap(Cliente::getId,
				p-> (long) (p.getOrdini()!=null ? p.getOrdini().size():0)
				));
	}
	/*
	 * 		0 ordini 		[Cliente1, cliente2]
	 * 		3 ordini		[Cliente3]
	 */
	public Map<Integer,List<Cliente>> ordiniCorrispondentiAClienti(){
		return clienteRepo.findAll().stream().collect(Collectors.groupingBy(
				p-> p.getOrdini()!=null ? p.getOrdini().size():0))
				;
	}
	
	// ===================================
	//	MAPPING   
	// ===================================
	
	// ENTITY -> DETAILS DTO (con lista)
	
	public ClienteDTODetails mapToDetailsDTO(Cliente c) {
		ClienteDTODetails det= new ClienteDTODetails();
		if(c==null) return null;
		det.setId(c.getId());
		det.setNome(c.getNome());
		det.setEmail(c.getEmail());
		if(c.getOrdini()!=null) {
			det.setOrdini(c.getOrdini().stream().map(this::mapOrdineToDto).toList());
		}
		return det;
	}
	
	public ClienteDTO mapToDto(Cliente c) {
		ClienteDTO dto= new ClienteDTO();
		if(c==null) { 
			return null;
		}
		dto.setEmail(c.getEmail());
		dto.setNome(c.getNome());
		dto.setId(c.getId());
		return dto;
		
	}
	public OrdineDTO mapOrdineToDto( Ordine o) {
		OrdineDTO dto= new OrdineDTO();
		dto.setTotale(o.getTotale());
		dto.setStato(o.getStatoOrdine());
		return dto;
	}
	
	public void mapToClienteEntity(ClienteDTO dto, Cliente c) {
		c.setEmail(dto.getEmail());
		c.setNome(dto.getNome());
	}
	public void mapDetailsToClienteEntity(ClienteDTODetails dto, Cliente c) {
		c.setEmail(dto.getEmail());
		c.setNome(dto.getNome());
	}
}
