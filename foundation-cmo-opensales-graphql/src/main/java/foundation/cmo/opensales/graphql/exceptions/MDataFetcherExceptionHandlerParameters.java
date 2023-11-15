package foundation.cmo.opensales.graphql.exceptions;

import java.util.Map;

import graphql.PublicApi;
import graphql.execution.MergedField;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

@PublicApi
public class MDataFetcherExceptionHandlerParameters {
    private final DataFetchingEnvironment dataFetchingEnvironment;
    private final Throwable exception;

    private MDataFetcherExceptionHandlerParameters(Builder builder) {
        this.exception = builder.exception;
        this.dataFetchingEnvironment = builder.dataFetchingEnvironment;
    }

    public Throwable getException() {
        return exception;
    }

    public ResultPath getPath() {
        return dataFetchingEnvironment.getExecutionStepInfo().getPath();
    }

    public DataFetchingEnvironment getDataFetchingEnvironment() {
        return dataFetchingEnvironment;
    }

    public MergedField getField() {
        return dataFetchingEnvironment.getMergedField();
    }

    public GraphQLFieldDefinition getFieldDefinition() {
        return dataFetchingEnvironment.getFieldDefinition();
    }

    public Map<String, Object> getArgumentValues() {
        return dataFetchingEnvironment.getArguments();
    }

    public SourceLocation getSourceLocation() {
        return getField().getSingleField().getSourceLocation();
    }

    public static Builder newExceptionParameters() {
        return new Builder();
    }

    public static class Builder {
        DataFetchingEnvironment dataFetchingEnvironment;
        Throwable exception;

        private Builder() {
        }

        public Builder dataFetchingEnvironment(DataFetchingEnvironment dataFetchingEnvironment) {
            this.dataFetchingEnvironment = dataFetchingEnvironment;
            return this;
        }

        public Builder exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        public MDataFetcherExceptionHandlerParameters build() {
            return new MDataFetcherExceptionHandlerParameters(this);
        }
    }
}
