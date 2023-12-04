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

import foundation.cmo.opensales.graphql.exceptions.MException;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class MGraphQLJwtService {

	/** The jwt signing key. */
	@Value("${AUTH_SECURITY_SIGNING}")
	private String jwtSigningKey;

	/** The jwt salt. */
	@Value("${AUTH_SECURITY_SALT}")
	private String jwtSalt;

	/** The iteration count. */
	@Value("${AUTH_SECURITY_ITERATION:10000}")
	private int iterationCount;

	/** The key length. */
	@Value("${AUTH_SECURITY_KEY_LENGTH:128}")
	private int keyLength;

	/** The is dev. */
	@Value("${IS_DEV:false}")
	private boolean isDev;

	/** The expiration. */
	@Value("${cmo.foundation.graphql.security.expiration:864000000}")
	private Long expiration;

	/** The auth user provider. */
	@Autowired(required = false)
	private IMAuthUserProvider authUserProvider;

	/**
	 * Encrypt.
	 *
	 * @param text the text
	 * @return the string
	 * @throws Exception the exception
	 */
	public String encrypt(String text) throws Exception {
		return encrypt(text, jwtSigningKey);
	}

	/**
	 * Encrypt.
	 *
	 * @param text the text
	 * @param key  the key
	 * @return the string
	 * @throws Exception the exception
	 */
	public String encrypt(String text, String key) throws Exception {
		byte[] keyByte = getKeyByte(key);
		SecretKeySpec keyAES = new SecretKeySpec(keyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, keyAES);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}

	/**
	 * Decrypt.
	 *
	 * @param text the text
	 * @return the string
	 * @throws Exception the exception
	 */
	public String decrypt(String text) throws Exception {
		return decrypt(text, jwtSigningKey);
	}

	/**
	 * Decrypt.
	 *
	 * @param text the text
	 * @param key  the key
	 * @return the string
	 * @throws Exception the exception
	 */
	public String decrypt(String text, String key) throws Exception {		
		byte[] keyByte = getKeyByte(key);
		byte[] textDecoded = Base64.getDecoder().decode(text);
		
		SecretKeySpec keyAES = new SecretKeySpec(keyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, keyAES);
		byte[] textoDescriptografado = cipher.doFinal(textDecoded);
		return new String(textoDescriptografado);
	}

	/**
	 * Gets the key byte.
	 *
	 * @return the key byte
	 * @throws Exception the exception
	 */
	public byte[] getKeyByte() throws Exception {
		return getKeyByte(jwtSigningKey);
	}

	/**
	 * Gets the key byte.
	 *
	 * @param skey the skey
	 * @return the key byte
	 * @throws Exception the exception
	 */
	public byte[] getKeyByte(String skey) throws Exception {
		jwtSigningKey = skey;
		//jwtSalt = "m4rc310";
		//iterationCount = 10000;
		//keyLength = 128;

		PBEKeySpec spec = new PBEKeySpec(jwtSigningKey.toCharArray(), jwtSalt.getBytes(), iterationCount, keyLength);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		return factory.generateSecret(spec).getEncoded();
	}

//	================================  //

	/**
 * Generate token.
 *
 * @param userDetails the user details
 * @return the string
 */
public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails);
	}
	
	/**
	 * Load user from token.
	 *
	 * @param token the token
	 * @param type  the type
	 * @return the m user
	 * @throws MException the m exception
	 */
	public MUser loadUserFromToken(String token, MEnumToken type) throws MException {
		return authUserProvider.loadUser(this, type, token);
	}

	/**
	 * User from token.
	 *
	 * @param <T>   the generic type
	 * @param token the token
	 * @param type  the type
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T userFromToken(String token, Class<T> type) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = decrypt(token);
		log.info("JSON: {}", json);
		
		
		
		return mapper.readValue(json, type);
	}

	/**
	 * Checks if is token valid.
	 *
	 * @param token       the token
	 * @param userDetails the user details
	 * @return the boolean
	 */
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Extract username.
	 *
	 * @param token the token
	 * @return the string
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extract expiration.
	 *
	 * @param token the token
	 * @return the date
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extract claim.
	 *
	 * @param <T>            the generic type
	 * @param token          the token
	 * @param claimsResolver the claims resolver
	 * @return the t
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extract all claims.
	 *
	 * @param token the token
	 * @return the claims
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
	}

	/**
	 * Checks if is token expired.
	 *
	 * @param token the token
	 * @return the boolean
	 */
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Creates the token.
	 *
	 * @param claims      the claims
	 * @param userDetails the user details
	 * @return the string
	 */
	private String createToken(Map<String, Object> claims, UserDetails userDetails) {

		Date now = new Date();
		Date dex = new Date(now.getTime() + 864000000);

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities()).setIssuedAt(now).setExpiration(dex)
				.signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
	}

	/**
	 * Checks if is validate user.
	 *
	 * @param user the user
	 * @return true, if is validate user
	 */
	public boolean isValidateUser(MUser user) {
		return authUserProvider.isValidUser(user);
	}

	/**
	 * Checks if is dev.
	 *
	 * @return true, if is dev
	 */
	public boolean isDev() {
		return isDev;
	}

/**
 * Test.
 */
//	==========> TESTE 
	public void test() {
		jwtSigningKey = "cmo_foundation";
		jwtSalt = jwtSigningKey;
		jwtSigningKey = "cmo_foundation";
		jwtSalt = "m4rc310";

		iterationCount = 10000;
		keyLength = 128;

		ObjectMapper mapper = new ObjectMapper();
		MUser user = new MUser();
		user.setCode(1L);
		user.setUsername("mlsilva");
		user.setRequestId("AA123");
		user.setRoles(new String[] { "ADMIN" });

		// Db9K1HlH1Ebpnyhk0SqkM9LoUN1bZraHOQzBSap+z8C6X7iJfiBXTRVkTrFAp+MMRKg1Xt4vmmiJrwazmDATOqx26qKrnHkW9r6mXeZW7ycZlhzSkBSYIbCs117dRQjJ
		// y7nJp7r8mWv1iE3Injx/8WY1K7d7+Rs2ZruiGY8kQcbQkQ4e6NLBLeV7nMYNF/bBLa0UMcjcvR5wDFwOyFFO79Gy8IUv1rguuyobWUTx+ZHHHZH2Vc2cN+T3GlOoDteF

		// MUserDetsils from = MUserDetsils.from(user);

		// log.info("{}", generateToken("ABCD"));

		try {
			String value = mapper.writeValueAsString(user);
//			log.info(value);
//			
			value = encrypt(value);
//			log.info(value);

			// String value = encrypt("Marcelo Lopes"); // uBm8TsMH2+fB0pyqLN2ruQ==
			// uBm8TsMH2+fB0pyqLN2ruQ==
			log.info(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info(String.format("%s : %s", jwtSigningKey, jwtSalt));
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new MGraphQLJwtService().test();
	}

}
