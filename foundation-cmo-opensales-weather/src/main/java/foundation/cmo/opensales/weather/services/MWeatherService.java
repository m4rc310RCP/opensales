package foundation.cmo.opensales.weather.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import foundation.cmo.opensales.weather.dto.MDistrict;
import foundation.cmo.opensales.weather.dto.MWeather;
import foundation.cmo.opensales.weather.dto.MWeatherLocation;
import io.leangen.graphql.annotations.GraphQLContext;

/**
 * The Class MWeatherService.
 */
@Configuration
@EnableCaching
public class MWeatherService {

	/** The weather url. */
	@Value("${cmo.foundation.weather:http://api.openweathermap.org}")
	private String weatherUrl;
	
	/** The ibge url. */
	@Value("${foundation.cmo.api.mls.geo.ibge-api-url:https://servicodados.ibge.gov.br}")
	private String ibgeUrl;

	/** The api key. */
	@Value("${cmo.foundation.weather.apikey:f78e35a5edf2478e0569d6abe8a7dcdb}")
	private String apiKey;

	/**
	 * Test.
	 *
	 * @return the string
	 */
	public String test() {
		return "MWeatherService running...";
	}

	/**
	 * Find location by name and state.
	 *
	 * @param locationName the location name
	 * @param state        the state
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<MWeatherLocation> findLocationByNameAndState(String locationName, String state) throws Exception {
		String sname = UriUtils.encode(locationName, StandardCharsets.UTF_8.toString());
		String suri = String.format("%s/geo/1.0/direct?q=%s,%s,%s&limit=%d&appid=%s", weatherUrl, sname, state, "BR", 5,
				apiKey);

		URL uri = new URL(suri);
		HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
		connection.setRequestProperty("accept", "application/json");
		
		InputStream responseStream = connection.getInputStream();
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(responseStream, new TypeReference<List<MWeatherLocation>>() {
		});
	}

	/**
	 * Gets the m weather.
	 *
	 * @param location the location
	 * @return the m weather
	 * @throws Exception the exception
	 */
	public MWeather getMWeather(@GraphQLContext MWeatherLocation location) throws Exception {

		String suri = String.format("%s/data/3.0/onecall?lat=%s&lon=%s&units=metric&lang=pt_br&appid=%s", weatherUrl,
				location.getLatitude().toPlainString(), location.getLongitude().toPlainString(), apiKey);

		URL uri = new URL(suri);
		HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
		connection.setRequestProperty("accept", "application/json");

		InputStream responseStream = connection.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(responseStream, new TypeReference<MWeather>() {
		});
	}

	/**
	 * Gets the district from ibge.
	 *
	 * @param ibgeId the ibge id
	 * @return the district from ibge
	 * @throws Exception the exception
	 */
	@Cacheable(value = "ibge_district")
	public MDistrict getDistrictFromIbge(Long ibgeId) throws Exception {
		
		String suri = String.format("%s/api/v1/localidades/distritos/%d", ibgeUrl, ibgeId);
		
		URL uri = new URL(suri);
		HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
		connection.setRequestProperty("accept", "application/json");
		
		InputStream responseStream = connection.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		
		List<MDistrict> list = mapper.readValue(responseStream, new TypeReference<List<MDistrict>>() {});
		
		if (list.isEmpty()) {
			return null;
		}
		
		return list.get(0);
	}
	
}
