package dev.store.exception;

@SuppressWarnings("serial")
public class ClienteNotFoundException extends RuntimeException{
	public ClienteNotFoundException(String message) {
		super(message);
	}
}
