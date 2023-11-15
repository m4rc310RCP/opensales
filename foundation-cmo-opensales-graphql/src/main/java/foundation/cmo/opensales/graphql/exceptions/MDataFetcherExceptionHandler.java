package foundation.cmo.opensales.graphql.exceptions;

import java.util.concurrent.CompletableFuture;

import graphql.PublicSpi;

@PublicSpi
public interface MDataFetcherExceptionHandler {

    default MDataFetcherExceptionHandlerResult onException(MDataFetcherExceptionHandlerParameters handlerParameters) {
        return MSimpleDataFetcherExceptionHandler.defaultImpl.onException(handlerParameters);
    }

 
    default CompletableFuture<MDataFetcherExceptionHandlerResult> handleException(MDataFetcherExceptionHandlerParameters handlerParameters) {
        MDataFetcherExceptionHandlerResult result = onException(handlerParameters);
        return CompletableFuture.completedFuture(result);
    }
}
