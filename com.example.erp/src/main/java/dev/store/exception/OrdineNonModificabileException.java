package dev.store.exception;

@SuppressWarnings("serial")
public class OrdineNonModificabileException extends RuntimeException{
	public OrdineNonModificabileException(String m) {
		super(m);
	}
}
