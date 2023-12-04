package foundation.cmo.opensales.weather.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import foundation.cmo.opensales.weather.services.MWeatherService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MWeatherAutoConfiguration.MWeatherProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.weather.enable", havingValue = "true", matchIfMissing = false)
public class MWeatherAutoConfiguration {

	/**
	 * Status.
	 */
	@Bean("status-weather")
	void status() {
		log.info("~> Module '{}' has been loaded.", "foundation.cmo.opensales.weather");
	}
	
	
	/**
	 * Load M weather service.
	 *
	 * @return the m weather service
	 */
	@Bean
	MWeatherService loadMWeatherService() {
		return new MWeatherService();
	}
	
	/**
	 * Instantiates a new m weather property.
	 */
	@Data
	@ConfigurationProperties("cmo.foundation.weather")
	public class MWeatherProperty {
		
		/** The enable. */
		private boolean enable;
	}
}
