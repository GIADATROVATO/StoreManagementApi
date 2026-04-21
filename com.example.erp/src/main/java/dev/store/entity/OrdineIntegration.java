package dev.store.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class OrdineIntegration {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private StatoIntegration stato;		//PENDING, SENT , FAILED
	private LocalDateTime createdAt;
/*Large OBject, indica al provider di persistenza (come Hibernate) 
*				che il campo annotato deve memorizzare una grande quantità di dati,
* 				superando i limiti tipici dei campi standard come Varchar
*/
	@Lob							
    private String payload;
	@Enumerated(EnumType.STRING)
	private Operazione operazione;		//CREATE, UPDATE, DELETE
	private int retryCount;
	public OrdineIntegration() {}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StatoIntegration getStato() {
		return stato;
	}
	public void setStato(StatoIntegration stato) {
		this.stato = stato;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount=retryCount;
	}
	public Operazione getOperazione() {
		return operazione;
	}
	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}
	
	
}
