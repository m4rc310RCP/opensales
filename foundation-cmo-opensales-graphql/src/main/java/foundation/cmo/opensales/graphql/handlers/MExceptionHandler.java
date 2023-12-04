package foundation.cmo.opensales.graphql.handlers;

import java.util.Collections;
import java.util.List;

import foundation.cmo.opensales.graphql.exceptions.MException;
import foundation.cmo.opensales.graphql.exceptions.MGraphQLError;
import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.language.SourceLocation;

/**
 * The Class MExceptionHandler.
 */
public class MExceptionHandler implements DataFetcherExceptionHandler {
	
	/**
	 * On exception.
	 *
	 * @param handlerParameters the handler parameters
	 * @return the data fetcher exception handler result
	 */
	public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = handlerParameters.getException();
		
		int code = 0;
		
		if (exception instanceof MException) {
			MException ex = (MException) exception;
			code = ex.getCode();
		}
		
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		MGraphQLException error = new MGraphQLException(exception, sourceLocation, code);

		return DataFetcherExceptionHandlerResult.newResult().error(error).build();
	}
//	public MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
//		Throwable exception = handlerParameters.getException();
//		
//		int code = 0;
//		
//		if (exception instanceof MException) {
//			MException ex = (MException) exception;
//			code = ex.getCode();
//		}
//		
//		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
//		MGraphQLException error = new MGraphQLException(exception, sourceLocation, code);
//		
//		return MDataFetcherExceptionHandlerResult.newResult().error(error).build();
//	}
	
	/**
 * The Class MGraphQLException.
 */
public class MGraphQLException implements MGraphQLError{
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;
		
		
	    /** The message. */
    	private final String message;
	    
    	/** The locations. */
    	private final List<SourceLocation> locations;
	    
    	/** The code. */
    	private final int code;

		
		/**
		 * Instantiates a new m graph QL exception.
		 *
		 * @param exception      the exception
		 * @param sourceLocation the source location
		 * @param code           the code
		 */
		public MGraphQLException(Throwable exception, SourceLocation sourceLocation, int code) {
	        this.locations = Collections.singletonList(sourceLocation);
	        this.message = exception.getMessage();
	        this.code = code;
		}
		
		/**
		 * Instantiates a new m graph QL exception.
		 */
		public MGraphQLException() {
			this(null, null, 0);
		}

		/**
		 * Gets the message.
		 *
		 * @return the message
		 */
		@Override
		public String getMessage() {
			return message;
		}
		
		/**
		 * Gets the code.
		 *
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Gets the locations.
		 *
		 * @return the locations
		 */
		@Override
		public List<SourceLocation> getLocations() {
			return this.locations;
		}

		/**
		 * Gets the error type.
		 *
		 * @return the error type
		 */
		@Override
		public ErrorClassification getErrorType() {
	        return ErrorType.DataFetchingException;
		}
		
	}
	
}
