package foundation.cmo.opensales.graphql.exceptions;

import java.util.Collections;
import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class MGraphQLException implements GraphQLError {

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