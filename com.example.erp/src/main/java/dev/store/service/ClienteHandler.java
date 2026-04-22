package dev.store.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.store.dto.ErpHandler;
import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;

@Service
public class ClienteHandler implements ErpHandler{
	private final WebClient webClient;
	public ClienteHandler(WebClient.Builder builder) {
		this.webClient=builder.baseUrl("http://localhost:8081").build();
	}
	@Override
	public EntityType getType() {
		return EntityType.CLIENTE;
	}
	@Override
	public void process(IntegrationEvent event) {
		switch(event.getOperazione()) {
		case CREATE:
			webClient.post().uri("/api/cliente").bodyValue(event.getPayload()).retrieve().bodyToMono(String.class).block();
			break;
		case UPDATE:
			webClient.put().uri("/api/cliente/"+ event.getEntityId()).bodyValue(event.getPayload())
				.retrieve().bodyToMono(String.class).block();
			break;
		case DELETE: 
			webClient.delete().uri("/api/cliente/"+ event.getEntityId()).retrieve().bodyToMono(Void.class).block();
			break;
		}
	}
}
