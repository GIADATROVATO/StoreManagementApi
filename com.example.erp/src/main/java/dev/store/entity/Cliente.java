package dev.store.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;

@Entity
public class Cliente {
	@Id 
	@GeneratedValue
	private Long id;
	private String nome; 
	private String email;
	@OneToMany(mappedBy="cliente")
	private List<Ordine> ordini;
	public Cliente() {}
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
	public List<Ordine> getOrdini() {
		return ordini;
	}
	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
	}

	
}
