package dev.store.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.store.entity.Operazione;
import dev.store.entity.Ordine;
import dev.store.entity.OrdineIntegration;
import dev.store.entity.StatoIntegration;
import dev.store.repository.OrdineIntegrationRepository;

@Service
public class IntegrationService {
	
	private final OrdineIntegrationRepository repository;
	private final ObjectMapper objectMapper; 
	
	public IntegrationService( OrdineIntegrationRepository repository,ObjectMapper objectMapper) {
		this.repository=repository;
		this.objectMapper=objectMapper;
	}
	
	public void salvaEvento(Ordine o, Operazione operazione) {
		try {
			String payload= objectMapper.writeValueAsString(o);
			OrdineIntegration i= new OrdineIntegration();
			i.setId(o.getId());
			i.setOperazione(operazione);
			i.setPayload(payload);
			i.setRetryCount(0);
			i.setCreatedAt(LocalDateTime.now());
			i.setStato(StatoIntegration.PENDING);
			
			repository.save(i);
		}catch(Exception e) {};
			throw new RuntimeException("Errore evento");
	}
}
