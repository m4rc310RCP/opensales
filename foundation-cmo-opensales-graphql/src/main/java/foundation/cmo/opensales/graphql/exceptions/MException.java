package foundation.cmo.opensales.graphql.exceptions;

public class MException extends Exception {

	private static final long serialVersionUID = 1L;

	private final int code;
	
	public MException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public static MException get(int code, String message) {
		return new MException(code, message);
	}
	
	
	public int getCode() {
		return code;
	}
}
