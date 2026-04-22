package dev.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.store.entity.EntityType;
import dev.store.entity.IntegrationEvent;
import dev.store.entity.StatoIntegration;

public interface IntegrationRepository extends JpaRepository<IntegrationEvent,Long>{
	List<IntegrationEvent> findByStatus(StatoIntegration status);
	List<IntegrationEvent> findByStatusEntity(StatoIntegration status, EntityType type);
}
