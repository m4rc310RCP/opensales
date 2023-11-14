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

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MCupsAutoConfiguration.MCupsProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.cups.enable", havingValue = "true", matchIfMissing = false)
public class MCupsAutoConfiguration {
	
	
	@Bean("status-cups")
	void status() {
		log.info("~> Module '{}' has been loaded.", "cmo.foundation.cups");
	}
	
	@Bean()
	MCupsService loadMCupsService() {
		return new MCupsService();
	}
	
	@Data
	@ConfigurationProperties("cmo.foundation.cups")
	public static class MCupsProperty {
		private boolean enable;
		private MServer server = new MServer();
		
		@Data
		public static class MServer {
			private int port;
			private String ip;			
		}
	}
}
