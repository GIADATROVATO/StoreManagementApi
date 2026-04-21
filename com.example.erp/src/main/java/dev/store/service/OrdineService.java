package dev.store.service;

import java.util.List;
import java.util.Optional;

import dev.store.dto.OrdineDTO;
import dev.store.entity.Ordine;
import dev.store.entity.StatoOrdine;

public interface OrdineService {
	OrdineDTO createOrdine(OrdineDTO o, Long clienteId);
	List<OrdineDTO> findByStato(StatoOrdine stato);
	OrdineDTO findById(Long id);
	Optional<OrdineDTO> update(OrdineDTO dto , Long id);
	boolean delete(Long id);
	
}
