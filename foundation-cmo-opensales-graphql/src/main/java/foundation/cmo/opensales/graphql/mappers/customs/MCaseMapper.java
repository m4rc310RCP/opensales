package foundation.cmo.opensales.graphql.mappers.customs;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;

import foundation.cmo.opensales.graphql.mappers.MGraphQLScalarType;
import foundation.cmo.opensales.graphql.mappers.annotations.MCase;
import foundation.cmo.opensales.graphql.mappers.annotations.MMapper;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

@MMapper
public class MCaseMapper extends MGraphQLScalarType<MCase> {

	@Override
	public GraphQLScalarType init(AnnotatedElement element, AnnotatedType type, MCase annotation) {
		
		String message = getString("Change case of the text (Case -> {0})", annotation.value().toString());

		Coercing<String, String> coercing = getCoercing(String.class, from -> changeCase(from, annotation),
				to -> changeCase(to, annotation));

		return get(String.format("Case%s",  annotation.value()), message, coercing);
	}

	private String changeCase(String value, MCase annotation) {
		
		switch (annotation.value()) {
			case UPPER:
				return value.toUpperCase();
			case LOWER:
				return value.toLowerCase();
			default:
				return value;
		}
	}

}
