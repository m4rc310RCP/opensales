package foundation.cmo.opensales.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MRegion {
	@JsonAlias("id")
	private Long id;
	
	@JsonAlias("sigla")
	private String acronym;
	
	@JsonAlias("nome")
	private String name;
}
