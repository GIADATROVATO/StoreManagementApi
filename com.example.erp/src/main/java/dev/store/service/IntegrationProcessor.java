package dev.store.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.store.entity.IntegrationEvent;
import dev.store.entity.StatoIntegration;
import dev.store.repository.IntegrationRepository;

@Service
public class IntegrationProcessor {
	public static final int MAX_RETRY=4;
	private Dispatcher dispatcher;
	private IntegrationRepository repository;
	private Logger log=LoggerFactory.getLogger(IntegrationProcessor.class);
	public IntegrationProcessor(Dispatcher d,IntegrationRepository r) {
		this.dispatcher=d;
		this.repository=r;
	}
	
	@Scheduled(fixedDelay=5000)
	public void processPending() {
		List<IntegrationEvent> lista= new ArrayList<>();
		lista.addAll(repository.findByStatus(StatoIntegration.PENDING));
		lista.addAll(repository.findByStatus(StatoIntegration.FAILED));
		
		for(IntegrationEvent i:lista) {
			int retry=i.getRetryCount();
			try {
				log.info("Invio {} {}", i.getOperazione(),i.getEntityId());
				dispatcher.dispatch(i);
				i.setStato(StatoIntegration.SENT);
					
			}catch(Exception e) {
				log.error("Errore evento {} su {}" ,i.getId(), i.getOperazione(),e);		//Id dell'evento nel db
				retry++;
				if(retry>=MAX_RETRY) {
					i.setRetryCount(retry);
					i.setStato(StatoIntegration.FAILED_PERMANENT);
				}else {
					i.setRetryCount(retry);
					i.setStato(StatoIntegration.FAILED);
				}
			}
			repository.save(i);
		}
	}
	
}
