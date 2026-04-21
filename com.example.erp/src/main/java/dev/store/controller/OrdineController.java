package dev.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.store.dto.OrdineDTO;
import dev.store.payload.ApiResponse;
import dev.store.service.ClienteServiceImpl;
import dev.store.service.OrdineServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ordine")
public class OrdineController {
	private OrdineServiceImpl service; 
	private ClienteServiceImpl clienteService;
	public OrdineController(OrdineServiceImpl service, ClienteServiceImpl clienteService) {
		this.service=service;
		this.clienteService=clienteService;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<OrdineDTO>> crea(@Valid @RequestBody OrdineDTO dto, Long id){
			OrdineDTO ordine= service.createOrdine(dto, id);
			return ResponseEntity.status(HttpStatus.CREATED).
					body(ApiResponse.succes("Ordine creato con successo",ordine));
	}
	//@GetMapping("/{id}")
	//public ResponseEntity<ApiResponse<OrdineDTO>>;
}
