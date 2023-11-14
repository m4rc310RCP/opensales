package foundation.cmo.opensales.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWeatherCurrentWeather {
	@JsonAlias("id")
	private Long id;
	@JsonAlias("main")
	private String main;
	@JsonAlias("description")
	private String description;
	@JsonAlias("icon")
	private String icon;
}
