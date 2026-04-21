package dev.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.store.entity.OrdineIntegration;

public interface OrdineIntegrationRepository extends JpaRepository<OrdineIntegration,Long>{
	List<OrdineIntegration> findByStatus(String status);
}
