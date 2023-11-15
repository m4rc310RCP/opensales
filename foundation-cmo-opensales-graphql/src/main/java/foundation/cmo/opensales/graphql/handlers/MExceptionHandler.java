package foundation.cmo.opensales.graphql.handlers;

import java.util.Collections;
import java.util.List;

import foundation.cmo.opensales.graphql.exceptions.MDataFetcherExceptionHandlerParameters;
import foundation.cmo.opensales.graphql.exceptions.MDataFetcherExceptionHandlerResult;
import foundation.cmo.opensales.graphql.exceptions.MException;
import foundation.cmo.opensales.graphql.exceptions.MGraphQLError;
import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.language.SourceLocation;

public class MExceptionHandler implements DataFetcherExceptionHandler {
	
	public MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = handlerParameters.getException();
		
		int code = 0;
		
		if (exception instanceof MException) {
			MException ex = (MException) exception;
			code = ex.getCode();
		}
		
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		MGraphQLException error = new MGraphQLException(exception, sourceLocation, code);

		return MDataFetcherExceptionHandlerResult.newResult().error(error).build();
	}
	
	public class MGraphQLException implements MGraphQLError{
		private static final long serialVersionUID = 1L;
		
		
	    private final String message;
	    private final List<SourceLocation> locations;
	    private final int code;

		
		public MGraphQLException(Throwable exception, SourceLocation sourceLocation, int code) {
	        this.locations = Collections.singletonList(sourceLocation);
	        this.message = exception.getMessage();
	        this.code = code;
		}
		
		public MGraphQLException() {
			this(null, null, 0);
		}

		@Override
		public String getMessage() {
			return message;
		}
		
		public int getCode() {
			return code;
		}

		@Override
		public List<SourceLocation> getLocations() {
			return this.locations;
		}

		@Override
		public ErrorClassification getErrorType() {
	        return ErrorType.DataFetchingException;
		}
		
	}
	
}
