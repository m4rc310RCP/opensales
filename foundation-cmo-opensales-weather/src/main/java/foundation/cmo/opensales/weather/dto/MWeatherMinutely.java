package foundation.cmo.opensales.weather.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWeatherMinutely {
	
	@JsonAlias("dt")
	private Long date;
	
	@JsonAlias("precipitation")
	private BigDecimal precipitation;
}
