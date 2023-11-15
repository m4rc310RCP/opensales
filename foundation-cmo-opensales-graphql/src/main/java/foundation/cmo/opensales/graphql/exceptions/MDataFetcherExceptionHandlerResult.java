package foundation.cmo.opensales.graphql.exceptions;

import static graphql.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import graphql.PublicApi;

@PublicApi
public class MDataFetcherExceptionHandlerResult {
	private final List<MGraphQLError> errors;

    private MDataFetcherExceptionHandlerResult(Builder builder) {
        this.errors = builder.errors;
    }

    public List<MGraphQLError> getErrors() {
        return errors;
    }

    public static Builder newResult() {
        return new Builder();
    }

    public static Builder newResult(MGraphQLError error) {
        return new Builder().error(error);
    }

    public static class Builder {

        private final List<MGraphQLError> errors = new ArrayList<>();

        public Builder errors(List<MGraphQLError> errors) {
            this.errors.addAll(assertNotNull(errors));
            return this;
        }

        public Builder error(MGraphQLError error) {
            errors.add(assertNotNull(error));
            return this;
        }

        public MDataFetcherExceptionHandlerResult build() {
            return new MDataFetcherExceptionHandlerResult(this);
        }
    }

}
