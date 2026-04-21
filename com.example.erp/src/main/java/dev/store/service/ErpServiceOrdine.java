package dev.store.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.store.entity.OrdineIntegration;
import dev.store.entity.StatoIntegration;
import dev.store.repository.OrdineIntegrationRepository;

@Service
public class ErpServiceOrdine {
	private WebClient webClient;
	private OrdineIntegrationRepository integrationRepo;
	private final Logger logger=LoggerFactory.getLogger(ErpServiceOrdine.class);
	
	public ErpServiceOrdine(WebClient.Builder builder, OrdineIntegrationRepository integrationRepo) {
		this.integrationRepo=integrationRepo;
		this.webClient=builder.baseUrl("http://localhost:8081").build();
		
	}
	@Scheduled(fixedDelay=5000)
	public void processPending() {
		List<OrdineIntegration> lista= integrationRepo.findByStatus("PENDING");
		for(OrdineIntegration i: lista) {
			processAsync(i);
		}
	}
	@Async
	public void processAsync(OrdineIntegration i) {
		try {
			logger.info("Operazione {} su ordine {}", i.getOperazione(), i.getId());
			switch(i.getOperazione()) {
		
				case CREATE :
					webClient.post().uri("/api/ordine").bodyValue(i.getPayload()).retrieve().bodyToMono(String.class).block();
					break;
				case UPDATE:
					webClient.put().uri("/api/ordine/"+ i.getId()).bodyValue(i.getPayload())
						.retrieve().bodyToMono(String.class).block();
					break;
				case DELETE: 
					webClient.delete().uri("/api/ordine/"+ i.getId()).retrieve().bodyToMono(Void.class).block();
					break;
			}
			i.setStato(StatoIntegration.SENT);		
		}catch(Exception e) {
			logger.error("Errore per ordine {}", i.getId(), e);
				
			if(i.getRetryCount()>=3) {
				i.setStato(StatoIntegration.FAILED_PERMANENT);
			}else {	
				i.setRetryCount(i.getRetryCount()+1);
				i.setStato(StatoIntegration.FAILED);
			}
		}
		integrationRepo.save(i);
	}
	
}
