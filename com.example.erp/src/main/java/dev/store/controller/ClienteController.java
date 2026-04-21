package dev.store.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.store.dto.ClienteDTO;
import dev.store.payload.ApiResponse;
import dev.store.service.ClienteServiceImpl;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
	private ClienteServiceImpl service; 
	public ClienteController(ClienteServiceImpl s) {
		this.service=s;
	}
	@PostMapping
	public ResponseEntity<ApiResponse<ClienteDTO>> crea(@Valid @RequestBody ClienteDTO dto) {
		ClienteDTO cliente= service.creaCliente(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.succes("Cliente creato con successo", cliente));
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ClienteDTO>> getById(@PathVariable Long id){
		ClienteDTO cliente=service.findById(id).orElseThrow(()->
			new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente per id non trovato"));
			return ResponseEntity.ok(ApiResponse.succes("Successo", cliente));
	}
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ClienteDTO>> update(@PathVariable Long id, @RequestBody ClienteDTO d) {
		ClienteDTO cliente= service.update(id, d);
		return ResponseEntity.ok(ApiResponse.succes("Modifica conclusa con successo", cliente));
		
	}
	@GetMapping
	 public ResponseEntity<ApiResponse<List<ClienteDTO>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.succes("Lista clienti", service.getAll()));
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		boolean delete= service.delete(id);
		if(!delete) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente non identificato");}
		return ResponseEntity.noContent().build();
	}
	
}
