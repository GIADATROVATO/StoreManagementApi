package dev.store.entity;

public enum StatoOrdine {
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
			throw new RuntimeException( "Transazione non valida: " +this+ " -> "+nuovo);
		}
	}
}
