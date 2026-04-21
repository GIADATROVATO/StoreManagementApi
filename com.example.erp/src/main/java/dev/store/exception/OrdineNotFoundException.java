package dev.store.exception;


@SuppressWarnings("serial")
public class OrdineNotFoundException extends RuntimeException {
	public OrdineNotFoundException(String m) {
		super(m);
	}
}
