package info.bitcrate.rebatch.container.exception;

public class JobSpecificationException extends BatchContainerRuntimeException {

	private static final long serialVersionUID = 5005245283527257646L;

	public JobSpecificationException() {
		super();
	}

	public JobSpecificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobSpecificationException(String message) {
		super(message);
	}

	public JobSpecificationException(Throwable cause) {
		super(cause);
	}
}
