package dev.store.exception;

@SuppressWarnings("serial")
public class ArticoloNonDisponibileException extends RuntimeException{
	public ArticoloNonDisponibileException(String m) {
		super(m);
	}
}
