package dev.store.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.store.entity.Operazione;
import dev.store.entity.Ordine;
import dev.store.entity.BaseEntity;
import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;
import dev.store.entity.StatoIntegration;
import dev.store.repository.IntegrationRepository;

@Service
public class IntegrationService {
	
	private final IntegrationRepository repository;
	private final ObjectMapper objectMapper; 
	
	public IntegrationService( IntegrationRepository repository,ObjectMapper objectMapper) {
		this.repository=repository;
		this.objectMapper=objectMapper;
	}
	
	private String mapToJson(Object saved) {
		try {
			return objectMapper.writeValueAsString(saved);
		}catch(Exception e) {
			throw new RuntimeException("Errore JSON",e);
		}
	}
	public void salvaEvento(BaseEntity entity, EntityType type, Operazione operazione) {
		if(entity==null) {
			throw new IllegalArgumentException("Entity non deve essere null");
		}
		
			String payload= mapToJson(entity);
			
			IntegrationEvent i= new IntegrationEvent();
			i.setEntityId((entity.getId()));
			i.setEntityType(type);
			i.setOperazione(operazione);
			i.setPayload(payload);
			i.setRetryCount(0);
			i.setCreatedAt(LocalDateTime.now());
			i.setStato(StatoIntegration.PENDING);
			
			repository.save(i);
	}
}
