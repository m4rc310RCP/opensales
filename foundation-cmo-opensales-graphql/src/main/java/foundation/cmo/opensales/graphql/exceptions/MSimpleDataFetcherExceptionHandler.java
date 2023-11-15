package foundation.cmo.opensales.graphql.exceptions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;

import graphql.ExceptionWhileDataFetching;
import graphql.PublicApi;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.util.LogKit;

@PublicApi
public class MSimpleDataFetcherExceptionHandler implements MDataFetcherExceptionHandler {
    private static final Logger logNotSafe = LogKit.getNotPrivacySafeLogger(MSimpleDataFetcherExceptionHandler.class);

    static final MSimpleDataFetcherExceptionHandler defaultImpl = new MSimpleDataFetcherExceptionHandler();

	@Override
	public MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = unwrap(handlerParameters.getException());
		
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		ResultPath path = handlerParameters.getPath();
		
		MExceptionWhileDataFetching error = new MExceptionWhileDataFetching(path, exception, sourceLocation);
		logException(error, exception);
		
		return MDataFetcherExceptionHandlerResult.newResult().error(error).build();
	}

	@Override
	public CompletableFuture<MDataFetcherExceptionHandlerResult> handleException(
			MDataFetcherExceptionHandlerParameters handlerParameters) {
		return CompletableFuture.completedFuture(onException(handlerParameters));
	}
    
  
    protected void logException(ExceptionWhileDataFetching error, Throwable exception) {
        logNotSafe.warn(error.getMessage(), exception);
    }

    protected Throwable unwrap(Throwable exception) {
        if (exception.getCause() != null) {
            if (exception instanceof CompletionException) {
                return exception.getCause();
            }
        }
        return exception;
    }
}
