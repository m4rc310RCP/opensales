package foundation.cmo.opensales.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWeather {
	@JsonAlias("current")
	private MWeatherCurrent current;
	
	@JsonAlias("minutely")
	private List<MWeatherMinutely> minutely;
}
