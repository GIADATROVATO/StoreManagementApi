package dev.store.entity;

import dev.store.exception.OrdineNonModificabileException;

public enum StatoOrdine {
	
	DELETED {
		@Override
		public boolean puoTransitareA(StatoOrdine nuovo) {
			return false;
		}
	},
	CREATED{
		@Override
		public boolean puoTransitareA(StatoOrdine nuovo) {
			return nuovo==PENDING;
		}
	},
	PENDING{
		@Override 
		public boolean puoTransitareA(StatoOrdine nuovo) {
			return nuovo == SHIPPED;
		}
	},
	SHIPPED{
		@Override 
		public boolean puoTransitareA(StatoOrdine nuovo) {
			return nuovo == COMPLETED;
		}
	},
	COMPLETED{
		@Override 
		public boolean puoTransitareA(StatoOrdine nuovo) {
			return false;
		}
	};
	
	
	public abstract boolean puoTransitareA(StatoOrdine nuovo);
	public void validaTransizione(StatoOrdine nuovo) {
		if(!puoTransitareA(nuovo)) {
			throw new OrdineNonModificabileException( "Transizione non valida: " +this+ " -> "+nuovo);
		}
	}
}
