package foundation.cmo.opensales.graphql.handlers;

import java.util.Collections;
import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.language.SourceLocation;

public class MExceptionHandler implements DataFetcherExceptionHandler {
	@Override
	public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = handlerParameters.getException();
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		MGraphQLException error = new MGraphQLException(exception, sourceLocation);

		return DataFetcherExceptionHandlerResult.newResult().error(error).build();
	}
	
	public class MGraphQLException implements GraphQLError{
		private static final long serialVersionUID = 1L;
		
		
	    private final String message;
	    private final List<SourceLocation> locations;

		
		public MGraphQLException(Throwable exception, SourceLocation sourceLocation) {
	        this.locations = Collections.singletonList(sourceLocation);
	        this.message = exception.getMessage();
		}
		
		public MGraphQLException() {
			this(null, null);
		}

		@Override
		public String getMessage() {
			return message;
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
