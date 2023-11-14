package foundation.cmo.opensales.graphql.services;

import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.DefaultOperationBuilder;

public class MSchemaGenerator extends GraphQLSchemaGenerator {
	public static final String[] basePackage = {"foundation.cmo"};
	
	public MSchemaGenerator() {
		withBasePackages(basePackage);
		withOperationBuilder(new DefaultOperationBuilder(DefaultOperationBuilder.TypeInference.LIMITED));
	}
}
