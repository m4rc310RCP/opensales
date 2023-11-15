package foundation.cmo.opensales.graphql.exceptions;

import graphql.ExceptionWhileDataFetching;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;

public class MExceptionWhileDataFetching extends ExceptionWhileDataFetching implements MGraphQLError {

	public MExceptionWhileDataFetching(ResultPath path, Throwable exception, SourceLocation sourceLocation) {
		super(path, exception, sourceLocation);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int getCode() {
		return 0;
	}

}
