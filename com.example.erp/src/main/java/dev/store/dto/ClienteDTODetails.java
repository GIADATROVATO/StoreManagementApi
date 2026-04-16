package dev.store.dto;

import java.util.List;

import dev.store.entity.Ordine;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class ClienteDTODetails {

	private Long id;
	private String nome; 
	private String email;
	private List<OrdineDTO> ordini;
	public ClienteDTODetails() {}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<OrdineDTO> getOrdini() {
		return ordini;
	}
	public void setOrdini(List<OrdineDTO> ordini) {
		this.ordini = ordini;
	}
}
