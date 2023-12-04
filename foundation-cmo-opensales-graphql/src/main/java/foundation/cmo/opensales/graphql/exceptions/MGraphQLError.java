package foundation.cmo.opensales.graphql.exceptions;

import graphql.GraphQLError;
import graphql.PublicApi;

/**
 * The Interface MGraphQLError.
 */
@PublicApi
public interface MGraphQLError extends GraphQLError {
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	int getCode();
}
