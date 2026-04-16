package dev.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.store.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
		Optional<Cliente> findByEmail(String e);
}
