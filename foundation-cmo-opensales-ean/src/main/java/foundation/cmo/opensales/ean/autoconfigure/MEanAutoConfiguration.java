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

/** The Constant log. */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MEanAutoConfiguration.MEanProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.ean.enable", havingValue = "true", matchIfMissing = false)
public class MEanAutoConfiguration {

	/**
	 * Status.
	 */
	@Bean("status-ean")
	void status() {
		log.info("~> Module '{}' has been loaded.", "cmo.foundation.ean");
	}

	/**
	 * Load ean cache.
	 *
	 * @return the m ean cache
	 */
	@Bean
	MEanCache loadEanCache() {
		return new MEanCache();
	}
	
	/**
	 * Load M ean service.
	 *
	 * @return the m ean service
	 */
	@Bean
	MEanService loadMEanService() {
		return new MEanService();
	}
	
	/**
	 * Load M ean service test.
	 *
	 * @return the m ean service test
	 */
	@Bean
	MEanServiceTest loadMEanServiceTest() {
		return new MEanServiceTest();
	}
	
	/**
	 * Load M ean cache GS 1 br V 1.
	 *
	 * @return the m ean cache GS 1 br V 1
	 */
	@Bean
	MEanCacheGS1brV1 loadMEanCacheGS1brV1() {
		return new MEanCacheGS1brV1();
	}

	/**
	 * Instantiates a new m ean property.
	 */
	@Data
	@ConfigurationProperties("cmo.foundation.ean")
	public class MEanProperty {
		
		/** The enable. */
		private boolean enable;
	}

}
