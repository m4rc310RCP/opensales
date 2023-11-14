package foundation.cmo.opensales.ean.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import foundation.cmo.opensales.ean.services.MEanCache;
import foundation.cmo.opensales.ean.services.MEanCacheGS1brV1;
import foundation.cmo.opensales.ean.services.MEanService;
import foundation.cmo.opensales.ean.services.MEanServiceTest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MEanAutoConfiguration.MEanProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.ean.enable", havingValue = "true", matchIfMissing = false)
public class MEanAutoConfiguration {

	@Bean("status-ean")
	void status() {
		log.info("~> Module '{}' has been loaded.", "cmo.foundation.ean");
	}

	@Bean
	MEanCache loadEanCache() {
		return new MEanCache();
	}
	
	@Bean
	MEanService loadMEanService() {
		return new MEanService();
	}
	
	@Bean
	MEanServiceTest loadMEanServiceTest() {
		return new MEanServiceTest();
	}
	
	@Bean
	MEanCacheGS1brV1 loadMEanCacheGS1brV1() {
		return new MEanCacheGS1brV1();
	}

	@Data
	@ConfigurationProperties("cmo.foundation.ean")
	public class MEanProperty {
		private boolean enable;
	}

}
