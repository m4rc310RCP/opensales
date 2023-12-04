package foundation.cmo.opensales.graphql.exceptions;

import java.util.Map;

import graphql.PublicApi;
import graphql.execution.MergedField;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

/**
 * The Class MDataFetcherExceptionHandlerParameters.
 */
@PublicApi
public class MDataFetcherExceptionHandlerParameters {
    
    /** The data fetching environment. */
    private final DataFetchingEnvironment dataFetchingEnvironment;
    
    /** The exception. */
    private final Throwable exception;

    /**
	 * Instantiates a new m data fetcher exception handler parameters.
	 *
	 * @param builder the builder
	 */
    private MDataFetcherExceptionHandlerParameters(Builder builder) {
        this.exception = builder.exception;
        this.dataFetchingEnvironment = builder.dataFetchingEnvironment;
    }

    /**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
    public Throwable getException() {
        return exception;
    }

    /**
	 * Gets the path.
	 *
	 * @return the path
	 */
    public ResultPath getPath() {
        return dataFetchingEnvironment.getExecutionStepInfo().getPath();
    }

    /**
	 * Gets the data fetching environment.
	 *
	 * @return the data fetching environment
	 */
    public DataFetchingEnvironment getDataFetchingEnvironment() {
        return dataFetchingEnvironment;
    }

    /**
	 * Gets the field.
	 *
	 * @return the field
	 */
    public MergedField getField() {
        return dataFetchingEnvironment.getMergedField();
    }

    /**
	 * Gets the field definition.
	 *
	 * @return the field definition
	 */
    public GraphQLFieldDefinition getFieldDefinition() {
        return dataFetchingEnvironment.getFieldDefinition();
    }

    /**
	 * Gets the argument values.
	 *
	 * @return the argument values
	 */
    public Map<String, Object> getArgumentValues() {
        return dataFetchingEnvironment.getArguments();
    }

    /**
	 * Gets the source location.
	 *
	 * @return the source location
	 */
    public SourceLocation getSourceLocation() {
        return getField().getSingleField().getSourceLocation();
    }

    /**
	 * New exception parameters.
	 *
	 * @return the builder
	 */
    public static Builder newExceptionParameters() {
        return new Builder();
    }

    /**
	 * The Class Builder.
	 */
    public static class Builder {
        
        /** The data fetching environment. */
        DataFetchingEnvironment dataFetchingEnvironment;
        
        /** The exception. */
        Throwable exception;

        /**
		 * Instantiates a new builder.
		 */
        private Builder() {
        }

        /**
		 * Data fetching environment.
		 *
		 * @param dataFetchingEnvironment the data fetching environment
		 * @return the builder
		 */
        public Builder dataFetchingEnvironment(DataFetchingEnvironment dataFetchingEnvironment) {
            this.dataFetchingEnvironment = dataFetchingEnvironment;
            return this;
        }

        /**
		 * Exception.
		 *
		 * @param exception the exception
		 * @return the builder
		 */
        public Builder exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        /**
		 * Builds the.
		 *
		 * @return the m data fetcher exception handler parameters
		 */
        public MDataFetcherExceptionHandlerParameters build() {
            return new MDataFetcherExceptionHandlerParameters(this);
        }
    }
}
