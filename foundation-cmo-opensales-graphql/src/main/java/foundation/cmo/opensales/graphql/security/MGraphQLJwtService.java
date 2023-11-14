package foundation.cmo.opensales.graphql.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import foundation.cmo.opensales.graphql.security.dto.MUser;
import foundation.cmo.opensales.graphql.security.dto.MUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MGraphQLJwtService {
	
	@Value("${AUTH_SECURITY_SIGNING:changeit}")
	private String jwtSigningKey;
	
	@Value("${AUTH_SECURITY_SALT:changeit}")
	private String jwtSalt;
	
	@Value("${AUTH_SECURITY_ITERATION:10000}")
	private int iterationCount;
	
	@Value("${AUTH_SECURITY_KEY_LENGTH:128}")
	private int keyLength;

	@Value("${IS_DEV:false}")
	private boolean isDev;
	
	@Value("${cmo.foundation.graphql.security.expiration:864000000}")
	private Long expiration;
	
	@Autowired(required = false)
	private IMAuthUserProvider authUserProvider;

	public String encrypt(String text) throws Exception {
		SecretKeySpec keyAES = new SecretKeySpec(getKeyByte(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, keyAES);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}

	public String decrypt(String text) throws Exception {
						byte[] textDecoded = Base64.getDecoder().decode(text);
		SecretKeySpec keyAES = new SecretKeySpec(getKeyByte(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, keyAES);
		byte[] textoDescriptografado = cipher.doFinal(textDecoded);
		return new String(textoDescriptografado);
	}

	public byte[] getKeyByte() throws Exception {
		PBEKeySpec spec = new PBEKeySpec(jwtSigningKey.toCharArray(), jwtSalt.getBytes(), iterationCount, keyLength);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		return factory.generateSecret(spec).getEncoded();
	}

//	================================  //

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails);
	}
	
	
	public <T> T userFromToken(String token, Class<T> type) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = decrypt(token);
		return mapper.readValue(json, type);
	}
	

	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private String createToken(Map<String, Object> claims, UserDetails userDetails) {

		Date now = new Date();
		Date dex = new Date(now.getTime() + 864000000);

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities()).setIssuedAt(now).setExpiration(dex)
				.signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
	}
	
	public boolean isValidateUser(MUser user) {
		return authUserProvider.isValidUser(user);
	}
	
	public boolean isDev() {
		return isDev;
	}
	
//	==========> TESTE 
	public void test()  {
		jwtSigningKey = "changeit";
		jwtSalt = jwtSigningKey;
		jwtSigningKey = "cmo_foundation";
		jwtSalt = "m4rc310";
		
		iterationCount = 10000;
		keyLength = 128;
		
		ObjectMapper mapper = new ObjectMapper();
		MUser user = new MUser();
		user.setCode(1234l);
		user.setUsername("UsernameTest");	
		user.setRequestId("AAAABBBCCCDDD");
		
		//MUserDetsils from = MUserDetsils.from(user);
		
		log.info("{}", generateToken(MUserDetails.from(user)));	
		try {
			String value = mapper.writeValueAsString(user);
			log.info(value);
			
			value = encrypt(value);
			log.info(value);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		log.info(String.format("%s : %s", jwtSigningKey, jwtSalt));
	}
	
	public static void main(String[] args) {
		new MGraphQLJwtService().test();
	}

}
