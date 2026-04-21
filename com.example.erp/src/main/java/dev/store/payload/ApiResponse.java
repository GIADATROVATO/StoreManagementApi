package dev.store.payload;

public class ApiResponse<T> {
	private String message;
	private T data;
	private boolean response;
	public ApiResponse( String message, boolean response, T data) {
		this.message=message;
		this.data=data;
		this.response=response;
	}
	public static <T> ApiResponse<T> succes(String message, T data){
		return new ApiResponse<>(message, true, data);
	}
	public static <T> ApiResponse<T> fail(String message, T data){
		return new ApiResponse<>(message, false, data);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public boolean isResponse() {
		return response;
	}
	public void setResponse(boolean response) {
		this.response = response;
	}
	
	
}
