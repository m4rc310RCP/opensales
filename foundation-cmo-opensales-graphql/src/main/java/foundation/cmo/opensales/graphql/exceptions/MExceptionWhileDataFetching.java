package foundation.cmo.opensales.graphql.exceptions;

import graphql.ExceptionWhileDataFetching;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;

/**
 * The Class MExceptionWhileDataFetching.
 */
public class MExceptionWhileDataFetching extends ExceptionWhileDataFetching implements MGraphQLError {

	/**
	 * Instantiates a new m exception while data fetching.
	 *
	 * @param path           the path
	 * @param exception      the exception
	 * @param sourceLocation the source location
	 */
	public MExceptionWhileDataFetching(ResultPath path, Throwable exception, SourceLocation sourceLocation) {
		super(path, exception, sourceLocation);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	@Override
	public int getCode() {
		return 0;
	}

}
