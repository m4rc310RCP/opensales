package foundation.cmo.opensales.jasper.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MJasperAutoConfiguration.MJasperProperty.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "cmo.foundation.jasper.enable", havingValue = "true", matchIfMissing = false)
public class MJasperAutoConfiguration {

	@Value("${FORMAT_MESSAGES:true}")
	private boolean format;

	@Bean("status-jasper")
	void status() {
		log.info("~> Module '{}' has been loaded.", "cmo.foundation.jasper");
	}

	@Data
	@ConfigurationProperties("cmo.foundation.jasper")
	public class MJasperProperty {
		private boolean enable;
		private Report report;

		@Data
		public class Report {
			private String path;
		}
	}

	@Component
	@ConditionalOnProperty(name = "cmo.foundation.jasper.enable", matchIfMissing = true)
	public class MApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
		
		@Autowired 
		private MRService service;

		@Override
		public void onApplicationEvent(ApplicationReadyEvent event) {
			log.info("~~> Compile Jasper Reports? = [{}]", format);

			if (!format) {
				return;
			}
			
			service.autoCompileReports();
		}
	}

	@Bean
	@ConditionalOnMissingBean
	MRService loadMRSevice() {
		return new MRService();
	}
	
}
