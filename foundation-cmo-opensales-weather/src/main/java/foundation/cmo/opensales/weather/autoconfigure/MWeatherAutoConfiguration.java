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

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MWeatherAutoConfiguration.MWeatherProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.weather.enable", havingValue = "true", matchIfMissing = false)
public class MWeatherAutoConfiguration {

	@Bean("status-weather")
	void status() {
		log.info("~> Module '{}' has been loaded.", "foundation.cmo.opensales.weather");
	}
	
	
	@Bean
	MWeatherService loadMWeatherService() {
		return new MWeatherService();
	}
	
	@Data
	@ConfigurationProperties("cmo.foundation.weather")
	public class MWeatherProperty {
		private boolean enable;
	}
}
