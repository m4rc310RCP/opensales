package foundation.cmo.opensales.graphql.mappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import foundation.cmo.opensales.graphql.mappers.MCoercingUtils.MFunction;
import graphql.schema.Coercing;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLScalarType;
import io.leangen.graphql.generator.mapping.TypeMapper;
import io.leangen.graphql.generator.mapping.TypeMappingEnvironment;

public abstract class MGraphQLScalarType<T extends Annotation> implements TypeMapper{
	private static final Map<String, GraphQLScalarType> maps = new HashMap<>();
	
	private GraphQLScalarType graphQLScalarType;
	

	@Override
	public GraphQLOutputType toGraphQLType(AnnotatedType javaType, Set<Class<? extends TypeMapper>> mappersToSkip,
			TypeMappingEnvironment env) {
		return graphQLScalarType;
	}

	@Override
	public GraphQLInputType toGraphQLInputType(AnnotatedType javaType, Set<Class<? extends TypeMapper>> mappersToSkip,
			TypeMappingEnvironment env) {
		return graphQLScalarType;
	}

	@Override
	public boolean supports(AnnotatedElement element, AnnotatedType type) {
		
		Class<T> annotationType = getAnnotationType();
		
		boolean supported = element.isAnnotationPresent(annotationType);
		if (supported) {
			T annotation = element.getAnnotation(annotationType);
			graphQLScalarType = init(element, type, annotation);
		}
		return supported;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getAnnotationType (){
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	
	public abstract GraphQLScalarType init(AnnotatedElement element, AnnotatedType type, T annotation);
	
	public String getString(String pattern, Object... args) {
		return MessageFormat.format(pattern, args);
	}
	
	public static GraphQLScalarType get(String key, Coercing<?, ?> coercing) {
		return get(key, key, coercing);
	}
	
	public static GraphQLScalarType get(String key, String description, Coercing<?, ?> coercing) {
		
		if (maps.containsKey(key)) {
			return maps.get(key);
		}

		GraphQLScalarType ret = GraphQLScalarType.newScalar().name(key).description(description).coercing(coercing).build();

		maps.put(key, ret);

		return ret;
	}
	
	protected String hashString(String s) {
		int hash = 7;
		for (int i = 0; i < s.length(); i++) {
			hash = hash * 31 + s.charAt(i);
		}

		hash = Math.abs(hash);

		return String.format("%010d", hash);
	}
	
	
	
	public GraphQLScalarType get(String key) {
		return maps.get(key);
	}
	
	public boolean isContainsGraphQLScalarType(String key) {
		return maps.containsKey(key);
	}

	public <O> Coercing<O, String> getCoercing(Class<O> type, MFunction<String, O> fromString,
			MFunction<O, String> toString) {
		return MCoercingUtils.get(type, fromString, toString);
	}
	
}
