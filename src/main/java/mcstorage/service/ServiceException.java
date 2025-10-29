package mcstorage.service;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -6048207677983398445L;
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
