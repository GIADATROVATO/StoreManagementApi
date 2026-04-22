package dev.store.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
@Entity
public class Ordine implements BaseEntity{
	@Id
	@GeneratedValue
	private Long id;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;
	private LocalDateTime dataSpedizione;
	private boolean disponibile; 
	@Enumerated(EnumType.STRING)
	private Operazione operazione;		//CREATE, UPDATE, DELETE;
	@Enumerated(EnumType.STRING)
	private StatoOrdine stato; 			//DELETED, CREATED, PENDING, SHIPPED, COMPLETED
	private BigDecimal totale;
	@ManyToOne
	private Cliente cliente;
	/*
	 *  un ordine include
	 *   - data di presa in carico dell'ordine 
	 *   - data di ultimazione, 
	 *   - data di spedizione
	 */
	
	
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}
	public LocalDateTime getDataFine() {
		return dataFine;
	}
	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}
	public LocalDateTime getDataSpedizione() {
		return dataSpedizione;
	}
	public void setDataSpedizione(LocalDateTime dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}
	public boolean isDisponibile() {
		return disponibile;
	}
	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}
	public StatoOrdine getStatoOrdine() {
		return stato;
	}
	public void setStatoOrdine(StatoOrdine stato) {
		this.stato = stato;
	}
	public BigDecimal getTotale() {
		return totale;
	}
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Operazione getOperazione() {
		return operazione;
	}
	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}
	
}
