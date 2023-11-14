package foundation.cmo.opensales.weather.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWeatherLocation {
	@JsonAlias("name")
	private String name;
	
	@JsonAlias("state")
	private String state;
	
	@JsonAlias("lat")
	private BigDecimal latitude;
	
	@JsonAlias("lon")
	private BigDecimal longitude;
}
