package dev.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.store.entity.Cliente;
import dev.store.entity.Ordine;
import dev.store.entity.StatoOrdine;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine,Long>{
		List<Ordine> findByDisponibile(Boolean dip);
		List<Ordine> findByStatus(StatoOrdine status);
}
