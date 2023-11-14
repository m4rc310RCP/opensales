package foundation.cmo.opensales.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MCity {
	@JsonAlias("id")
	private Long id;
	
	@JsonAlias("nome")
	private String name;
	

}
