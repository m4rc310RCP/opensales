package foundation.cmo.opensales.graphql.exceptions;

import java.util.concurrent.CompletableFuture;

import graphql.PublicSpi;

/**
 * The Interface MDataFetcherExceptionHandler.
 */
@PublicSpi
public interface MDataFetcherExceptionHandler {

    /**
	 * On exception.
	 *
	 * @param handlerParameters the handler parameters
	 * @return the m data fetcher exception handler result
	 */
    default MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
        return MSimpleDataFetcherExceptionHandler.defaultImpl.onException(handlerParameters);
    }

 
    /**
	 * Handle exception.
	 *
	 * @param handlerParameters the handler parameters
	 * @return the completable future
	 */
    default CompletableFuture<MDataFetcherExceptionHandlerResult> handleException(MDataFetcherExceptionHandlerParameters handlerParameters) {
        MDataFetcherExceptionHandlerResult result = onException(handlerParameters);
        return CompletableFuture.completedFuture(result);
    }
}
