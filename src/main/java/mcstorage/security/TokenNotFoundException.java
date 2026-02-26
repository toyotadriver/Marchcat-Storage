package mcstorage.security;

public class TokenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3504486064540826383L;
	
	public TokenNotFoundException(String message) {
		super(message);
	}
	
	public TokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
