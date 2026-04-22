package dev.store.dto;

import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;

public interface ErpHandler {
	 EntityType getType();
	void process(IntegrationEvent event);
}
