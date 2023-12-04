package foundation.cmo.opensales.graphql.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Instantiates a new m user.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MUser {
	
	/** The request id. */
	@JsonProperty("request_id")
	private String requestId;
	
	/** The username. */
	@JsonProperty("username")
	private String username;
	
	/** The code. */
	@JsonProperty("code")
	private Long code;
	
	/** The roles. */
	@JsonProperty("roles")
	private String[] roles;
}