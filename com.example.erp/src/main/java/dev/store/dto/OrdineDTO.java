package dev.store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.store.entity.StatoOrdine;

public class OrdineDTO {
	private LocalDateTime dataInizio; 
	private LocalDateTime dataFine; 
	private boolean disponibile; 
	private BigDecimal totale;
	private LocalDateTime dataSpedizione;
	private StatoOrdine stato;
	public LocalDateTime getDataSpedizione() {
		return dataSpedizione;
	}
	public void setDataSpedizione(LocalDateTime dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}
	public StatoOrdine getStato() {
		return stato;
	}
	public void setStato(StatoOrdine stato) {
		this.stato = stato;
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
	public boolean isDisponibile() {
		return disponibile;
	}
	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}
	public BigDecimal getTotale() {
		return totale;
	}
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	
}
