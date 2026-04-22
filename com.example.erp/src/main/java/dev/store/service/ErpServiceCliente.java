package dev.store.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;
import dev.store.entity.StatoIntegration;
import dev.store.repository.IntegrationRepository;
/*
@Service
public class ErpServiceCliente {
	private final WebClient webClient; 
	private final IntegrationRepository integration;
	private final Logger log=LoggerFactory.getLogger(ErpServiceCliente.class);
	public ErpServiceCliente(WebClient.Builder builder,IntegrationRepository integration ) {
		this.webClient=builder.baseUrl("http://localhost:8081").build();
		this.integration=integration;
	}
	@Scheduled(fixedDelay=5000)
	public void retryPending() {
		
		List<IntegrationEvent> list= integration.findByStatusEntity(
				StatoIntegration.PENDING, EntityType.CLIENTE);
		for(IntegrationEvent i: list) {
			processAsync(i);
		}
	}
	@Async
	public void processAsync(IntegrationEvent i) {
		try {
			log.info("Invio {} cliente {}", i.getOperazione(), i.getEntityId());
			switch(i.getOperazione()) {
			case CREATE:
				webClient.post().uri("/api/cliente").bodyValue(i.getPayload()).retrieve().bodyToMono(String.class).block();
				break;
			case UPDATE:
				webClient.put().uri("/api/cliente/"+ i.getEntityId()).bodyValue(i.getPayload())
					.retrieve().bodyToMono(String.class).block();
				break;
			case DELETE: 
				webClient.delete().uri("/api/cliente/"+ i.getEntityId()).retrieve().bodyToMono(Void.class).block();
				break;
			}
			i.setStato(StatoIntegration.SENT);
		}catch(Exception e){
			log.error("Errore cliente {}", i.getEntityId(), e);
			if(i.getRetryCount()>=3) {
				i.setStato(StatoIntegration.FAILED_PERMANENT);
			}else {
				i.setRetryCount(i.getRetryCount()+1);
				i.setStato(StatoIntegration.FAILED);
			}
		}
		integration.save(i);
	}
}
*/
