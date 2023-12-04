package foundation.cmo.opensales.graphql.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Instantiates a new m graph QL property.
 */
@Data
@ConfigurationProperties("cmo.foundation.graphql")
public class MGraphQLProperty {
	
	/** The enable. */
	private boolean enable;
	
	/** The security. */
	private Security security = new Security();
	
	/** The constants. */
	private Constant constants = new Constant();

	/**
	 * Instantiates a new security.
	 */
	@Data
	public class Security {
		
		/** The enable. */
		private boolean enable;
		
		/** The expiration. */
		private long expiration;
	}
	
	/**
	 * Instantiates a new constant.
	 */
	@Data
	public class Constant{
		
		/** The path. */
		private String path;
		
		/** The classname. */
		private String classname;
	}
	
}