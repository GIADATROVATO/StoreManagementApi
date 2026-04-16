package dev.store.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.store.dto.ClienteDTO;
import dev.store.entity.Cliente;
import dev.store.repository.ClienteRepository;
import dev.store.repository.OrdineRepository;

public class ClienteServiceImplTest {

	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private OrdineRepository ordineRepository;
	
	private ClienteServiceImpl service;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		service= new ClienteServiceImpl(ordineRepository,clienteRepository);
	}
	@Test
	void shouldCreateClient() throws Exception{
	//arrange	
	ClienteDTO dto= new ClienteDTO();
	dto.setEmail("@mario");
	dto.setNome("Mario");
		
	Cliente c= new Cliente();
	c.setId(1L);
	c.setEmail("@mario");
	c.setNome("Mario");
		
	when(clienteRepository.save(any(Cliente.class))).thenReturn(c);	
	//act
	ClienteDTO result= service.creaCliente(dto);
	
	//assert
	assertNotNull(result);
	assertEquals(result.getNome(), "Mario");
	assertEquals(result.getId(),1L);
	assertEquals(result.getEmail(),"@mario");
	
	//verify
	verify(clienteRepository).save(any(Cliente.class));
	
	}
	@Test
	void shouldReturnClienteWhenIdExists() {
		
		Long id=1L;
		Cliente c= new Cliente();
		c.setId(1L);
		c.setNome("Mario");
		
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(c));
		
		
		Optional<ClienteDTO> result= service.findById(id);
		assertTrue(result.isPresent());
		assertEquals(result.get().getNome(),"Mario");
	
		verify(clienteRepository).findById(id);
	}
	@Test
	void shouldUpdateWhenIdExists() {

	    Long id = 1L;

	    Cliente c = new Cliente();
	    c.setNome("Mario");
	    c.setEmail("@mario");
	    c.setId(id);

	    when(clienteRepository.findById(id)).thenReturn(Optional.of(c));

	    when(clienteRepository.save(any(Cliente.class)))
	            .thenAnswer(invocation -> invocation.getArgument(0));

	    ClienteDTO dto = new ClienteDTO();
	    dto.setNome("Luigi");
	    dto.setEmail("new@mail");

	    ClienteDTO result = service.update(id, dto);

	    assertEquals("Luigi", result.getNome());
	    assertEquals("new@mail", result.getEmail());

	    verify(clienteRepository).findById(id);
	    verify(clienteRepository).save(any(Cliente.class));
	}
	@Test
	void shouldThrowExceptionWhenClienteNotFound() {
		Long id=1L;
		when(clienteRepository.findById(id)).thenReturn(Optional.empty());
		
		ClienteDTO dto= new ClienteDTO();
		
		RuntimeException e= assertThrows(RuntimeException.class,()->{ service.update(id, dto); });
		assertEquals("cliente non trovato", e.getMessage());
		verify(clienteRepository).findById(id);
		
	}
	@Test
	void shouldDeleteClienteIfIdExists(){
		Long id=1L;
		Cliente c= new Cliente();
		c.setId(id);
		when(clienteRepository.findById(id)).thenReturn(Optional.of(c));
		boolean dto= service.delete(id);
		assertTrue(dto);
		verify(clienteRepository).findById(id);
		verify(clienteRepository).delete(c);
	}
	@Test
	void shoulReturnFalseIfClienteDoesNotExists(){
		Long id=1L;
		when(clienteRepository.findById(id)).thenReturn(Optional.empty());
		boolean dto= service.delete(id);
		assertFalse(dto);
		verify(clienteRepository).findById(id);
		verify(clienteRepository, never()).delete(any());
	}
}
