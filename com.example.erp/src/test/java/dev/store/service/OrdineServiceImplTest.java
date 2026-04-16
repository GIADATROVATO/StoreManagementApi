package dev.store.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.store.dto.OrdineDTO;
import dev.store.entity.Cliente;
import dev.store.entity.Ordine;
import dev.store.entity.StatoOrdine;
import dev.store.repository.ClienteRepository;
import dev.store.repository.OrdineRepository;

import static org.junit.jupiter.api.Assertions.*;

public class OrdineServiceImplTest {
	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private OrdineRepository ordineRepository;
	private OrdineServiceImpl service;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		service= new OrdineServiceImpl(ordineRepository,clienteRepository);
	}
	
	
	@Test
	
	void notThrowWhenTotaleIsValid() {
		Ordine o= new Ordine();
		o.setTotale(BigDecimal.ZERO);
		Exception e=assertThrows( RuntimeException.class, ()-> service.validaOrdine(o));
	}

	@Test
	
	void shouldCreateOrdine() throws Exception {
		
		//simulo cosa arriva dal controller
		OrdineDTO dto= new OrdineDTO();
		dto.setTotale(new BigDecimal("10"));
		dto.setDisponibile(true);
		
		//creo un cliente finto
		Cliente c= new Cliente();
		c.setId(1L);
		
		//creo il risultato salvato
		Ordine saved= new Ordine();
		saved.setTotale(new BigDecimal("10"));
		saved.setDisponibile(true);
		
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(c));
		when(ordineRepository.save(any(Ordine.class))).thenReturn(saved);
		
		OrdineDTO result= service.createOrdine(dto, 1L);
		assertNotNull(result);
		assertEquals(new BigDecimal("10"), saved.getTotale());
		verify(clienteRepository).findById(1L);
		verify(ordineRepository).save(any(Ordine.class));
	}
	@Test
	
	void shouldThrowClienteNotFound() {
		OrdineDTO dto= new OrdineDTO();
		
		when(ordineRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, ()-> { service.createOrdine(dto, 1L);
		});
		verify(clienteRepository).findById(1L);
		
	}
}
