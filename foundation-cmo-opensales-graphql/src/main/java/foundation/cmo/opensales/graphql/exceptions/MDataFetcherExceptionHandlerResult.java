package foundation.cmo.opensales.graphql.exceptions;

import static graphql.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import graphql.PublicApi;

/**
 * The Class MDataFetcherExceptionHandlerResult.
 */
@PublicApi
public class MDataFetcherExceptionHandlerResult {
	
	/** The errors. */
	private final List<MGraphQLError> errors;

    /**
	 * Instantiates a new m data fetcher exception handler result.
	 *
	 * @param builder the builder
	 */
    private MDataFetcherExceptionHandlerResult(Builder builder) {
        this.errors = builder.errors;
    }

    /**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
    public List<MGraphQLError> getErrors() {
        return errors;
    }

    /**
	 * New result.
	 *
	 * @return the builder
	 */
    public static Builder newResult() {
        return new Builder();
    }

    /**
	 * New result.
	 *
	 * @param error the error
	 * @return the builder
	 */
    public static Builder newResult(MGraphQLError error) {
        return new Builder().error(error);
    }

    /**
	 * The Class Builder.
	 */
    public static class Builder {

        /** The errors. */
        private final List<MGraphQLError> errors = new ArrayList<>();

        /**
		 * Errors.
		 *
		 * @param errors the errors
		 * @return the builder
		 */
        public Builder errors(List<MGraphQLError> errors) {
            this.errors.addAll(assertNotNull(errors));
            return this;
        }

        /**
		 * Error.
		 *
		 * @param error the error
		 * @return the builder
		 */
        public Builder error(MGraphQLError error) {
            errors.add(assertNotNull(error));
            return this;
        }

        /**
		 * Builds the.
		 *
		 * @return the m data fetcher exception handler result
		 */
        public MDataFetcherExceptionHandlerResult build() {
            return new MDataFetcherExceptionHandlerResult(this);
        }
    }

}
