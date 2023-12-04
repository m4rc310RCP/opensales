package foundation.cmo.opensales.cups.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import foundation.cmo.opensales.cups.services.MCupsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MCupsAutoConfiguration.MCupsProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.cups.enable", havingValue = "true", matchIfMissing = false)
public class MCupsAutoConfiguration {
	
	
	/**
	 * Status.
	 */
	@Bean("status-cups")
	void status() {
		log.info("~> Module '{}' has been loaded.", "cmo.foundation.cups");
	}
	
	/**
	 * Load M cups service.
	 *
	 * @return the m cups service
	 */
	@Bean()
	MCupsService loadMCupsService() {
		return new MCupsService();
	}
	
	/**
	 * Instantiates a new m cups property.
	 */
	@Data
	@ConfigurationProperties("cmo.foundation.cups")
	public static class MCupsProperty {
		
		/** The enable. */
		private boolean enable;
		
		/** The server. */
		private MServer server = new MServer();
		
		/**
		 * Instantiates a new m server.
		 */
		@Data
		public static class MServer {
			
			/** The port. */
			private int port;
			
			/** The ip. */
			private String ip;			
		}
	}
}
