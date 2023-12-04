package foundation.cmo.opensales.graphql.services;

import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.DefaultOperationBuilder;

/**
 * The Class MSchemaGenerator.
 */
public class MSchemaGenerator extends GraphQLSchemaGenerator {
	
	/** The Constant basePackage. */
	public static final String[] basePackage = {"foundation.cmo"};
	
	/**
	 * Instantiates a new m schema generator.
	 */
	public MSchemaGenerator() {
		withBasePackages(basePackage);
		withOperationBuilder(new DefaultOperationBuilder(DefaultOperationBuilder.TypeInference.LIMITED));
	}
}
