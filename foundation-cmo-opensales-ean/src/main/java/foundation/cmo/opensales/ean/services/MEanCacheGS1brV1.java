package foundation.cmo.opensales.ean.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@EnableCaching
public class MEanCacheGS1brV1 {

	/** The auth uri. */
	@Value("${cmo.foundation.gs1br.auth.uri:https://api.gs1br.org/oauth/access-token}")
	private String authUri;

	/** The client id. */
	@Value("${cmo.foundation.gs1br.client.id:6a88f9b0-b599-42c1-a3db-a1ed4c225adf}")
	private String clientId;

	/** The client secret. */
	@Value("${cmo.foundation.gs1br.client.secret:7aaec6e3-4935-496d-ae07-049f07598578}")
	private String clientSecret;

	/**
	 * Request access token.
	 *
	 * @return the string
	 */
	public String requestAccessToken() {
		try {

			URL uri = new URL(authUri);

			HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			
			

			String authorization = String.format("%s:%s", clientId, clientSecret);
			//String authorization = String.format("%s:%s", clientId, Base64.encodeBase64String(clientSecret.getBytes()));
			//authorization = Base64.encodeBase64String(authorization.getBytes());
			authorization = String.format("Basic %s", authorization);
			connection.setRequestProperty("Authorization", authorization);
			connection.setRequestProperty("client_id ", clientId);
			
			log.info(authorization);

			ObjectMapper mapper = new ObjectMapper();

			RequestBody body = new RequestBody();
			//body.setGrantType("password");
			body.setUsername("marcelo.utfpr@me.com");
			body.setPassword("Escol@1979");

			String bodyJson = mapper.writeValueAsString(body);
			
			log.info(bodyJson);

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = bodyJson.toString().getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder resp = new StringBuilder();
				String respLine = null;
				while ((respLine = br.readLine()) != null) {
					resp.append(respLine.trim());
				}
				
				System.out.println(resp.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Instantiates a new request body.
	 */
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class RequestBody {
		
		/** The grant type. */
		@JsonAlias("grant_type")
		private String grant_type;
		
		/** The username. */
		@JsonAlias("username")
		private String username;
		
		/** The password. */
		@JsonAlias("password")
		private String password;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			new MEanCacheGS1brV1().test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test.
	 *
	 * @throws Exception the exception
	 */
	private void test() throws Exception{
		String clientID = "6a88f9b0-b599-42c1-a3db-a1ed4c225adf";
		String clientSecret = "7aaec6e3-4935-496d-ae07-049f07598578";
		//String clientID = "2da3d381-43ae-4e0a-8cc4-8d583f7732ec";
	//	String clientSecret = "23e15806-bb04-4c0c-97e8-f8beb885cd6a";
		String credentials = clientID + ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		
		String tokenEndpoint = "https://api.gs1br.org/oauth/access-token";
		URL url = new URL(tokenEndpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
		conn.setRequestProperty("client_id ", clientID);
		
		
		conn.setDoOutput(true);
		
		ObjectMapper mapper = new ObjectMapper();

		RequestBody body = new RequestBody();
		body.setGrant_type("password");
		body.setUsername("m4rc310");
		body.setPassword("senha");
//		body.setUsername(clientID);
//		body.setPassword(clientSecret);

		String requestBody = mapper.writeValueAsString(body);
		
		System.out.println(requestBody);
		
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(requestBody);
        osw.flush();
        osw.close();
        
        int responseCode = conn.getResponseCode();
        
        System.out.println(responseCode);
        System.out.println(conn.getResponseMessage());
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseBody = response.toString();
            System.out.println("Token de Acesso: " + responseBody);
        }
		
        conn.disconnect();
	}
	

}
