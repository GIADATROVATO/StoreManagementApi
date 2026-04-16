package dev.store.service;

import java.util.Optional;

import dev.store.dto.ClienteDTO;

public interface ClienteService {
	ClienteDTO creaCliente(ClienteDTO D);
	Optional<ClienteDTO> findById(Long id);
	
	
}
