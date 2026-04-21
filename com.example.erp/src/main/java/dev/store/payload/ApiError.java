package dev.store.payload;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {
	private LocalDateTime timestamp;
	private int status; 
	private String message; 
	private String path; 
		
	public ApiError(LocalDateTime timestamp,int status, String message,String path ) {
		this.message=message;
		this.status=status;
		this.timestamp=timestamp;
		this.path=path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
