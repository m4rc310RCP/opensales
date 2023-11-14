package foundation.cmo.opensales.graphql.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cmo.foundation.graphql")
public class MGraphQLProperty {
	private boolean enable;
	private Security security = new Security();
	private Constant constants = new Constant();

	@Data
	public class Security {
		private boolean enable;
		private long expiration;
	}
	
	@Data
	public class Constant{
		private String path;
		private String classname;
	}
	
}