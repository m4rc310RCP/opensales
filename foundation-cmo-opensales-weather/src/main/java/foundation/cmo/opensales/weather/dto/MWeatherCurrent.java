package foundation.cmo.opensales.weather.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWeatherCurrent {
	@JsonAlias("dt")
	private Long dateWeather;
	
	@JsonAlias("sunrise")
	private Long dateSunRise;
	
	@JsonAlias("sunset")
	private Long dateSunSet;
	
	@JsonAlias("temp")
	private BigDecimal temperature;
	
	@JsonAlias("feels_like")
	private BigDecimal feelsLike;
	
	@JsonAlias("pressure")
	private BigDecimal pressure;
	
	@JsonAlias("humidity")
	private BigDecimal humidity;
	
	@JsonAlias("dew_point")
	private BigDecimal dewPoint;
	
	@JsonAlias("uvi")
	private BigDecimal uvi;
	
	@JsonAlias("clouds")
	private BigDecimal clouds;
	
	@JsonAlias("visibility")
	private BigDecimal visibility;
	
	@JsonAlias("wind_speed")
	private BigDecimal speedWind;
	
	@JsonAlias("wind_deg")
	private BigDecimal degreesWind;
	
	@JsonAlias("rain")
	private Rain rain;
	
	@JsonAlias("weather")
	private List<MWeatherCurrentWeather> weather;
	
	@Data
	public class Rain {
		@JsonAlias("1h")
		private BigDecimal precipitation;
	}
}
