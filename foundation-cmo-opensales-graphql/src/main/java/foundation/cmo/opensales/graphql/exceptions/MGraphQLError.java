package foundation.cmo.opensales.graphql.exceptions;

import graphql.GraphQLError;
import graphql.PublicApi;

@PublicApi
public interface MGraphQLError extends GraphQLError {
	int getCode();
}
