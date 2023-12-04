package foundation.cmo.opensales.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Instantiates a new m district.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MDistrict {
	
	/** The id. */
	@JsonAlias("id")
	private Long id;
	
	/** The name. */
	@JsonAlias("nome")
	private String name;
	
	/** The city. */
	@JsonAlias("municipio")
	private MCity city;
}
