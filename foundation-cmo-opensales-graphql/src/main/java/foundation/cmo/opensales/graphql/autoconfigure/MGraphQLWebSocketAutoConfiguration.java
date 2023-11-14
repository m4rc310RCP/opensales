package foundation.cmo.opensales.graphql.autoconfigure;

import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import foundation.cmo.opensales.graphql.handlers.MPerConnectionProtocolHandler;
import foundation.cmo.opensales.graphql.security.IMAuthUserProvider;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.spqr.spring.autoconfigure.DataLoaderRegistryFactory;
import io.leangen.graphql.spqr.spring.autoconfigure.SpqrProperties;
import io.leangen.graphql.spqr.spring.autoconfigure.WebSocketAutoConfiguration;
import io.leangen.graphql.spqr.spring.web.apollo.PerConnectionApolloHandler;
import io.leangen.graphql.spqr.spring.web.mvc.websocket.GraphQLWebSocketExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfiguration
@EnableWebSocket
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebSocketConfigurer.class)
@ConditionalOnProperty(name = "graphql.spqr.ws.enabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnBean(GraphQLSchema.class)
public class MGraphQLWebSocketAutoConfiguration extends WebSocketAutoConfiguration {

	private SpqrProperties config;
	private GraphQL graphQL;
	private MGraphQLJwtService jwtService;
	public MGraphQLWebSocketAutoConfiguration(GraphQL graphQL, SpqrProperties config,
			Optional<DataLoaderRegistryFactory> dataLoaderRegistryFactory, MGraphQLJwtService jwtService) {
		super(graphQL, config, dataLoaderRegistryFactory);

		this.graphQL = graphQL;
		this.config = config;
		this.jwtService = jwtService;
	}

	void status() {
		log.info("***** Enable WS Security ****");
	}

	@Override
	public PerConnectionApolloHandler webSocketHandler(GraphQLWebSocketExecutor executor) {

		boolean keepAliveEnabled = config.getWs().getKeepAlive().isEnabled();
		int keepAliveInterval = config.getWs().getKeepAlive().getIntervalMillis();
		int sendTimeLimit = config.getWs().getSendTimeLimit();
		int sendBufferSizeLimit = config.getWs().getSendBufferSizeLimit();

		return new MPerConnectionProtocolHandler(graphQL, executor, keepAliveEnabled ? defaultTaskScheduler() : null,
				keepAliveInterval, sendTimeLimit, sendBufferSizeLimit, jwtService);
	}

	private TaskScheduler defaultTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
		threadPoolScheduler.setThreadNamePrefix("GraphQLWSKeepAlive-");
		threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
		threadPoolScheduler.setRemoveOnCancelPolicy(true);
		threadPoolScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		threadPoolScheduler.initialize();
		return threadPoolScheduler;
	}

}
