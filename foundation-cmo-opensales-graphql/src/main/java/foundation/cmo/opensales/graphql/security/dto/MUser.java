package foundation.cmo.opensales.graphql.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MUser {
	
	@JsonProperty("request_id")
	private String requestId;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty()
	private Long code;
	
	@JsonProperty()
	private String[] roles;
}
