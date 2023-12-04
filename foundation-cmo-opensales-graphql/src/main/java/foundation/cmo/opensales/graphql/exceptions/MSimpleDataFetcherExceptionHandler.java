package foundation.cmo.opensales.graphql.exceptions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;

import graphql.ExceptionWhileDataFetching;
import graphql.PublicApi;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.util.LogKit;

/**
 * The Class MSimpleDataFetcherExceptionHandler.
 */
@PublicApi
public class MSimpleDataFetcherExceptionHandler implements MDataFetcherExceptionHandler {
    
    /** The Constant logNotSafe. */
    private static final Logger logNotSafe = LogKit.getNotPrivacySafeLogger(MSimpleDataFetcherExceptionHandler.class);

    /** The Constant defaultImpl. */
    static final MSimpleDataFetcherExceptionHandler defaultImpl = new MSimpleDataFetcherExceptionHandler();

	/**
	 * On exception.
	 *
	 * @param handlerParameters the handler parameters
	 * @return the m data fetcher exception handler result
	 */
	@Override
	public MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
		Throwable exception = unwrap(handlerParameters.getException());
		
		SourceLocation sourceLocation = handlerParameters.getSourceLocation();
		ResultPath path = handlerParameters.getPath();
		
		MExceptionWhileDataFetching error = new MExceptionWhileDataFetching(path, exception, sourceLocation);
		logException(error, exception);
		
		return MDataFetcherExceptionHandlerResult.newResult().error(error).build();
	}

	/**
	 * Handle exception.
	 *
	 * @param handlerParameters the handler parameters
	 * @return the completable future
	 */
	@Override
	public CompletableFuture<MDataFetcherExceptionHandlerResult> handleException(
			MDataFetcherExceptionHandlerParameters handlerParameters) {
		return CompletableFuture.completedFuture(onException(handlerParameters));
	}
    
  
    /**
	 * Log exception.
	 *
	 * @param error     the error
	 * @param exception the exception
	 */
    protected void logException(ExceptionWhileDataFetching error, Throwable exception) {
        logNotSafe.warn(error.getMessage(), exception);
    }

    /**
	 * Unwrap.
	 *
	 * @param exception the exception
	 * @return the throwable
	 */
    protected Throwable unwrap(Throwable exception) {
        if (exception.getCause() != null) {
            if (exception instanceof CompletionException) {
                return exception.getCause();
            }
        }
        return exception;
    }
}
