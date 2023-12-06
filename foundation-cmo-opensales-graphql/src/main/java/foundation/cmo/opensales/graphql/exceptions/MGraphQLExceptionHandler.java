package foundation.cmo.opensales.graphql.exceptions;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.language.SourceLocation;

public class MGraphQLExceptionHandler implements DataFetcherExceptionHandler {
	@Override
	public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = handlerParameters.getException();
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		MGraphQLException error = new MGraphQLException(exception, sourceLocation);
		

		return DataFetcherExceptionHandlerResult.newResult().error(error).build();
	}
}