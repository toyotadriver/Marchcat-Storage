package mcstorage.service;

public class StorageException extends Exception {
	private static final long serialVersionUID = -6048207677983398445L;
	
	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
