package foundation.cmo.opensales.graphql.autoconfigure;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Main;
import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonCreator;

import foundation.cmo.opensales.graphql.exceptions.MGraphQLException;
import foundation.cmo.opensales.graphql.exceptions.MGraphQLExceptionHandler;
import foundation.cmo.opensales.graphql.mappers.annotations.MMapper;
import foundation.cmo.opensales.graphql.messages.MMessageBuilder;
import foundation.cmo.opensales.graphql.messages.i18n.M;
import foundation.cmo.opensales.graphql.security.IMAuthUserProvider;
import foundation.cmo.opensales.graphql.security.MAuth;
import foundation.cmo.opensales.graphql.security.MAuthToken;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.security.MGraphQLSecurity;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import foundation.cmo.opensales.graphql.services.MFluxService;
import foundation.cmo.opensales.graphql.strategies.MPhysicalNamingImpl;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphqlTypeComparatorRegistry;
import io.leangen.graphql.ExtensionProvider;
import io.leangen.graphql.GeneratorConfiguration;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.annotations.types.GraphQLType;
import io.leangen.graphql.execution.InvocationContext;
import io.leangen.graphql.execution.ResolverInterceptor;
import io.leangen.graphql.generator.mapping.TypeMapper;
import io.leangen.graphql.metadata.messages.MessageBundle;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.query.DefaultOperationInfoGenerator;
import io.leangen.graphql.metadata.strategy.query.OperationInfoGenerator;
import io.leangen.graphql.metadata.strategy.query.OperationInfoGeneratorParams;
import io.leangen.graphql.metadata.strategy.type.TypeInfoGenerator;
import io.leangen.graphql.metadata.strategy.value.InputFieldInfoGenerator;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;

/** The Constant log. */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MGraphQLProperty.class)
@ConditionalOnProperty(name = MConsts.PROPERTY$enable_graphql, havingValue = "true", matchIfMissing = false)
public class MGraphQLAutoConfiguration {

	/** The format. */
	@Value("${FORMAT_MESSAGES:true}")
	private boolean format;

	/** The message builder. */
	private final MMessageBuilder messageBuilder;

	/** The auth user provider. */
	@Autowired(required = false)
	private IMAuthUserProvider authUserProvider;

	// @Autowired
	// private MPerConnectionProtocolHandler connectionProtocolHandler;

	/**
	 * Instantiates a new m graph QL auto configuration.
	 */
	public MGraphQLAutoConfiguration() {
		this.messageBuilder = new MMessageBuilder();

	}

	/**
	 * Status.
	 */
	@Bean("status-graphql")
	void status() {
		log.info("~> Module '{}' has been loaded.", "foundation.cmo.service.graphql");
	}

	/**
	 * Test dinamic class.
	 *
	 * @return the object
	 * @throws Exception the exception
	 */
//	@Bean
	Object testDinamicClass() throws Exception {
		Class<?> type = new ByteBuddy().subclass(Object.class).name("MServiceTestV1").annotateType(new GraphQLApi[0])
				.defineMethod("test", String.class, Visibility.PUBLIC).intercept(FixedValue.value("OK"))
				.annotateMethod(new GraphQLQuery[0]).make()
				.load(Main.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

		return type.getDeclaredConstructor().newInstance();
	}

	/**
	 * Load M flux service.
	 *
	 * @return the m flux service
	 */
	@Bean
	MFluxService loadMFluxService() {
		return new MFluxService();
	}

	/**
	 * Load M message builder.
	 *
	 * @return the m message builder
	 */
	@Bean()
	MMessageBuilder loadMMessageBuilder() {
		return messageBuilder;
	}

	/**
	 * Graph QL.
	 *
	 * @param schema the schema
	 * @return the graph QL
	 */
	@Bean
	GraphQL graphQL(GraphQLSchema schema) {
//		schema.getQueryType().getFields().forEach(def -> {
//			log.info(">>>> {}", def.getArguments());
//		});

		// List<GraphQLFieldDefinition> fields = new ArrayList<>();
		// fields.addAll(schema.getQueryType().getFields());
		// fields.addAll(schema.getMutationType().getFields());
		// fields.addAll(schema.getSubscriptionType().getFields());

		// schema.getAllTypesAsList().forEach(type -> {
		// log.info("{} -> {}", type.getName(), type.getClass()); //
		// System.out.println(type.getClass());
		// });

//		schema.getSubscriptionType().getFields().forEach(sf -> {
//			log.info("Sub -> {}", sf);
//			});

		// SubscriptionExecutionStrategy subscriptionExecutionStrategy = new
		// SubscriptionExecutionStrategy(new MExceptionHandler());

		// MDataFetcherExceptionHandler mDataFetcherExceptionHandler = new
		// MDataFetcherExceptionHandler() {};

//		AsyncExecutionStrategy aes = new AsyncExecutionStrategy(new MExceptionHandler());
//		return GraphQL.newGraphQL(schema)
//				.defaultDataFetcherExceptionHandler(new MExceptionHandler())
//				.queryExecutionStrategy(aes)
//				.subscriptionExecutionStrategy(aes)
//				.mutationExecutionStrategy(aes).build();

		AsyncExecutionStrategy aes = new AsyncExecutionStrategy(new MGraphQLExceptionHandler());

		return GraphQL.newGraphQL(schema).subscriptionExecutionStrategy(aes).mutationExecutionStrategy(aes)
				.queryExecutionStrategy(aes).build();
	}

	// ----- Security ----- //

	/**
	 * Cors configurer.
	 *
	 * @return the web mvc configurer
	 */
	@Bean
	WebMvcConfigurer corsConfigurer() {
		log.info("Configure Cors");
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/graphql").allowedOrigins("*");
			}
		};
	}

	/**
	 * Gets the m graph QL jwt service.
	 *
	 * @return the m graph QL jwt service
	 */
	@Bean
	MGraphQLJwtService getMGraphQLJwtService() {
		return new MGraphQLJwtService();
	}

	/**
	 * Load security.
	 *
	 * @param http the http
	 * @param jwt  the jwt
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	@ConditionalOnProperty(name = "cmo.foundation.graphql.security.enable", matchIfMissing = true)
	SecurityFilterChain loadSecurity(HttpSecurity http, MGraphQLJwtService jwt) throws Exception {

		if (Objects.isNull(authUserProvider)) {
			String error = "The %s required a bean of type '%s' that could not be found.";
			error = String.format(error, "securityFilterChain", IMAuthUserProvider.class.getName());
			throw new Exception(error);
		}

		return new MGraphQLSecurity().getSecurityFilterChain(http, jwt, authUserProvider);
	}

	/**
	 * Gets the user details service.
	 *
	 * @return the user details service
	 */
	@Bean
	MUser getUserDetailsService() {
		if (authUserProvider != null) {
			return null;
		}
		return null;
	}

	/**
	 * The listener interface for receiving MApplication events. The class that is
	 * interested in processing a MApplication event implements this interface, and
	 * the object created with that class is registered with a component using the
	 * component's <code>addMApplicationListener</code> method. When the
	 * MApplication event occurs, that object's appropriate method is invoked.
	 *
	 */
	// ----- Messages ----- //
	@Component
	@ConditionalOnProperty(name = MConsts.PROPERTY$enable_graphql, matchIfMissing = true)
	public class MApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

		/**
		 * On application event.
		 *
		 * @param event the event
		 */
		@Override
		public void onApplicationEvent(ApplicationReadyEvent event) {
			log.info("~~> Start format auxiliar? = [{}]", format);

			if (format) {
				log.info("Formating messages...");
				messageBuilder.fixUnknowMessages();
			}
		}
	}

	/**
	 * Message source.
	 *
	 * @return the message source
	 */
	@Bean(name = "messageSource")
	MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages/message");
		return messageSource;
	}

	/**
	 * Message bundle.
	 *
	 * @return the message bundle
	 */
	@Bean
	MessageBundle messageBundle() {
		return key -> getString(key);
	}

	/**
	 * Load message.
	 *
	 * @return the m
	 */
	@Bean
	M loadMessage() {
		M message = new M();
		if (authUserProvider != null) {
			authUserProvider.setMessage(message);
		}
		return message;
	}

	/**
	 * Gets the string.
	 *
	 * @param pattern the pattern
	 * @param args    the args
	 * @return the string
	 */
	public String getString(String pattern, Object... args) {
		try {
			String message = messageSource().getMessage(pattern, args, Locale.forLanguageTag("pt-BR"));

			try {
				if (!pattern.startsWith("desc.")) {
					pattern = String.format("desc.%s", pattern);
					messageSource().getMessage(pattern, args, Locale.forLanguageTag("pt-BR"));
				}
			} catch (Exception e) {
				String regex = "[^a-zA-Z0-9_]+";
				String ret = pattern.replaceAll(regex, "_");
				messageBuilder.appendText(pattern, ret);
			}

			return message;
		} catch (Exception e) {
			String regex = "[^a-zA-Z0-9_]+";
			String ret = pattern.replaceAll(regex, "_");
			messageBuilder.appendText(pattern, ret);
			return ret;
		}
	}

	/**
	 * Load improved naming strategy.
	 *
	 * @return the improved naming strategy
	 */
	@Bean
	ImprovedNamingStrategy loadImprovedNamingStrategy() {
		return new ImprovedNamingStrategy() {
			private static final long serialVersionUID = 1L;

			@Override
			public String classToTableName(String className) {
				return super.classToTableName(className);
			}
		};

	}

	/**
	 * Physical naming strategy standard.
	 *
	 * @return the physical naming strategy standard impl
	 */
	@Bean
	PhysicalNamingStrategyStandardImpl physicalNamingStrategyStandard() {
		return new MPhysicalNamingImpl() {
			private static final long serialVersionUID = 1L;

			@Override
			public Identifier apply(Identifier name, JdbcEnvironment context) {
				if (name != null && name.getCanonicalName().contains("${")) {
					String message = name.getCanonicalName();
					message = message.replace("${", "");
					message = message.replace("}", "");

					try {
						message = messageSource().getMessage(message, null, Locale.forLanguageTag("pt-BR"));
						return Identifier.toIdentifier(message, true);
					} catch (Exception e) {

						Pattern pattern = Pattern.compile("\\b\\w+\\.\\w+\\b");
						Matcher matcher = pattern.matcher(message);

						while (matcher.find()) {
							String palavra = matcher.group();

							log.warn("Message not found for {}", palavra);

							messageBuilder.appendText(message, palavra);
						}

						throw new UnsupportedOperationException(e);
					}
				}
				return name;
			}
		};
	}

	/**
	 * Implicit.
	 *
	 * @return the implicit naming strategy
	 */
	@Bean
	ImplicitNamingStrategy implicit() {
		return new ImplicitNamingStrategyLegacyJpaImpl() {
			private static final long serialVersionUID = -5643307972624175002L;

			@Override
			protected Identifier toIdentifier(String stringForm, MetadataBuildingContext buildingContext) {

				if (Objects.nonNull(stringForm) && stringForm.startsWith("${")) {
					stringForm = stringForm.replace("${", "");
					stringForm = stringForm.replace("}", "");

					try {
						stringForm = messageSource().getMessage(stringForm, null, Locale.forLanguageTag("pt-BR"));
						return super.toIdentifier(stringForm, buildingContext);
					} catch (Exception e) {

						Pattern pattern = Pattern.compile("\\b\\w+\\.\\w+\\b");
						Matcher matcher = pattern.matcher(stringForm);

						while (matcher.find()) {
							String palavra = matcher.group();
							log.warn("Message not found for {}", palavra);
							messageBuilder.appendText(stringForm, palavra);
						}
						throw new UnsupportedOperationException(e);
					}
				}

				return super.toIdentifier(stringForm, buildingContext);
			}

			@Override
			protected String transformEntityName(EntityNaming entityNaming) {
				String entityName = super.transformEntityName(entityNaming);
				if (Objects.nonNull(entityName) && entityName.startsWith("${")) {
					entityName = entityName.replace("${", "");
					entityName = entityName.replace("}", "");

					try {
						return messageSource().getMessage(entityName, null, Locale.forLanguageTag("pt-BR"));
					} catch (Exception e) {

						Pattern pattern = Pattern.compile("\\b\\w+\\.\\w+\\b");
						Matcher matcher = pattern.matcher(entityName);

						while (matcher.find()) {
							String palavra = matcher.group();
							log.warn("Message not found for {}", palavra);
							messageBuilder.appendText(entityName, palavra);
						}
						throw new UnsupportedOperationException(e);
					}
				}

				return entityName;
			}

		};
	}

	/**
	 * Pageable input field.
	 *
	 * @return the extension provider
	 */
	@Bean
	@ConditionalOnMissingBean
	ExtensionProvider<GeneratorConfiguration, TypeMapper> pageableInputField() {
		log.info("~~> Starting '{}'...", "Custom Mappers");

		return (config, defaults) -> {

			final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
					false);
			provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

			final Set<BeanDefinition> classes = provider.findCandidateComponents("foundation");
			for (BeanDefinition bean : classes) {
				try {
					Class<?> clazz = Class.forName(bean.getBeanClassName());

//					for(Field field : clazz.getDeclaredFields()) {
//						field.setAccessible(true);
//						if (field.isAnnotationPresent(MReportRegistryInfo.class)) {
//							log.info("Info-----> {}", field.getName());
//						}
//					}

//					if (clazz.isAnnotationPresent(MReportRegistryInfo.class)) {
//					}

					if (clazz.isAnnotationPresent(MMapper.class)) {
						Constructor<?> constructor = clazz.getDeclaredConstructor();
						TypeMapper mapper = (TypeMapper) constructor.newInstance();
						defaults.prepend(mapper);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return defaults;
		};
	}

//	@Bean
//	ExtensionProvider<GeneratorConfiguration, ResolverInterceptor> configureResolverInterceptorFactory() {
//		log.info("===> {}", "");		
//
//		return (config, defaults) -> {
//			
//			log.info("===> {}", defaults);		
//			
//			List<ResolverInterceptor> interceptors = new ArrayList<>();
//			interceptors.add(new AuthInterceptor());
////			defaults.append(GlobalResolverInterceptorFactory.early(interceptors));
//			// defaults.prepend(GlobalResolverInterceptorFactory.early(Arrays.asList(new
//			// AuthInterceptor())));
//			return defaults;
//		};
//	}

	/**
	 * Graph QL schema.
	 *
	 * @param schemaGenerator the schema generator
	 * @return the graph QL schema
	 */
	@Bean
//	@ConditionalOnMissingBean
	GraphQLSchema graphQLSchema(GraphQLSchemaGenerator schemaGenerator) {
		schemaGenerator.withResolverInterceptors(new AuthInterceptor(), new RegistryInfoInterceptor());

		schemaGenerator.withResolverBuilders(
				new AnnotatedResolverBuilder().withOperationInfoGenerator(new MOperationInfoGenerator()));
		return schemaGenerator.generate();
	}

	/**
	 * The Class MOperationInfoGenerator.
	 */
	@SuppressWarnings("unused")
	private class MOperationInfoGenerator implements OperationInfoGenerator {

		/**
		 * Name.
		 *
		 * @param params the params
		 * @return the string
		 */
		@Override
		public String name(OperationInfoGeneratorParams params) {
			String name = getName(params);
			return name;
		}

		/**
		 * Description.
		 *
		 * @param params the params
		 * @return the string
		 */
		@Override
		public String description(OperationInfoGeneratorParams params) {
			String name = getName(params);
			if (name.startsWith("${") && name.endsWith("}")) {
				return name.replace("${", "${desc.");
			}
			return null;
		}

		/**
		 * Deprecation reason.
		 *
		 * @param params the params
		 * @return the string
		 */
		@Override
		public String deprecationReason(OperationInfoGeneratorParams params) {
			return null;
		}

		/**
		 * Gets the name.
		 *
		 * @param params the params
		 * @return the name
		 */
		private String getName(OperationInfoGeneratorParams params) {
			AnnotatedElement element = params.getElement().getElement();
			if (element.isAnnotationPresent(GraphQLQuery.class)) {
				return element.getAnnotation(GraphQLQuery.class).name();
			} else if (element.isAnnotationPresent(GraphQLSubscription.class)) {
				return element.getAnnotation(GraphQLSubscription.class).name();
			} else if (element.isAnnotationPresent(GraphQLMutation.class)) {
				return element.getAnnotation(GraphQLMutation.class).name();
			} else if (element.isAnnotationPresent(GraphQLType.class)) {
				return element.getAnnotation(GraphQLType.class).name();
			} else if (element.isAnnotationPresent(GraphQLArgument.class)) {
				return element.getAnnotation(GraphQLArgument.class).name();
			} else {
				return getMember(params).getName();
			}
		}

		/**
		 * Gets the member.
		 *
		 * @param params the params
		 * @return the member
		 */
		private Member getMember(OperationInfoGeneratorParams params) {
			Object supplier = params.getInstanceSupplier().get().getClass();
			AnnotatedElement element = params.getElement().getElement();
			return ((Member) params.getElement().getElement());
		}

	}

	/**
	 * Load default operation info generator.
	 *
	 * @return the default operation info generator
	 */
	@Bean
	DefaultOperationInfoGenerator loadDefaultOperationInfoGenerator() {
		return new DefaultOperationInfoGenerator() {
			@Override
			public String description(OperationInfoGeneratorParams params) {
				return "Bla-bla-bla";
			}
		};
	}

	/**
	 * Load input field info generator.
	 *
	 * @return the input field info generator
	 */
	@Bean
	InputFieldInfoGenerator loadInputFieldInfoGenerator() {
		return new InputFieldInfoGenerator() {
			@Override
			public Optional<String> getDescription(List<AnnotatedElement> candidates, MessageBundle messageBundle) {
				log.info("description");
				return super.getDescription(candidates, messageBundle);
			}

			@Override
			public Optional<String> getName(List<AnnotatedElement> candidates, MessageBundle messageBundle) {
				log.info("name");
				return super.getName(candidates, messageBundle);
			}
		};
	}

	/**
	 * Test type info generator.
	 *
	 * @return the type info generator
	 */
//	 @Bean
	TypeInfoGenerator testTypeInfoGenerator() {
		return new TypeInfoGenerator() {
			@Override
			public String generateTypeName(AnnotatedType type, MessageBundle messageBundle) {
				return "OK";
			}

			@Override
			public String generateTypeDescription(AnnotatedType type, MessageBundle messageBundle) {
				return "OK";
			}

			@Override
			public GraphqlTypeComparatorRegistry generateComparatorRegistry(AnnotatedType type,
					MessageBundle messageBundle) {
				return GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;
			}
		};
	}

//	@Bean
//	PerConnectionApolloHandler loadTextWebSocketHandler() {
//		return new PerConnectionApolloHandler(null, null, null, 0, 0, 0);
//	}

	/**
	 * The Class AuthInterceptor.
	 */
	private class AuthInterceptor implements ResolverInterceptor {

		/**
		 * Around invoke.
		 *
		 * @param context      the context
		 * @param continuation the continuation
		 * @return the object
		 * @throws Exception the exception
		 */
		@Override
		public Object aroundInvoke(InvocationContext context, Continuation continuation) throws Exception {
			MAuthToken authentication = (MAuthToken) SecurityContextHolder.getContext().getAuthentication();
			MAuth auth = context.getResolver().getExecutable().getDelegate().getAnnotation(MAuth.class);
			if (Objects.nonNull(auth)) {
				authUserProvider.validUserAccess(authentication, auth.rolesRequired());
			}

			return continuation.proceed(context);
		}
	}

	/**
	 * The Class RegistryInfoInterceptor.
	 */
	private class RegistryInfoInterceptor implements ResolverInterceptor {

		/**
		 * Around invoke.
		 *
		 * @param context      the context
		 * @param continuation the continuation
		 * @return the object
		 * @throws Exception the exception
		 */
		@Override
		public Object aroundInvoke(InvocationContext context, Continuation continuation) throws Exception {
//			MReportRegistryInfo rri = context.getResolver().getExecutable().getDelegate()
//					.getAnnotation(MReportRegistryInfo.class);
//
//			if (Objects.nonNull(rri)) {
//				log.info("MReportRegistryInfo: {}", rri);
//			}

			// log.info("MReportRegistryInfo: {}", context);

			return continuation.proceed(context);
		}
	}

	/**
	 * The Class MissingGenerics.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class MissingGenerics {

		/** The raw. */
		private final Map raw;

		/** The unbounded. */
		private final Map<?, ?> unbounded;

		/**
		 * Instantiates a new missing generics.
		 *
		 * @param raw       the raw
		 * @param unbounded the unbounded
		 */
		@JsonCreator
		public MissingGenerics(Map raw, Map<?, ?> unbounded) {
			this.raw = raw;
			this.unbounded = unbounded;
		}

		/**
		 * Gets the raw.
		 *
		 * @return the raw
		 */
		public Map<Integer, Integer> getRaw() {
			return raw;
		}

		/**
		 * Gets the unbounded.
		 *
		 * @return the unbounded
		 */
		public Map<?, ?> getUnbounded() {
			return unbounded;
		}
	}

}
