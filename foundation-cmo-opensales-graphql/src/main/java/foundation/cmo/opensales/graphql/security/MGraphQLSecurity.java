package foundation.cmo.opensales.graphql.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import foundation.cmo.opensales.graphql.security.dto.MUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MGraphQLSecurity {

	private final OncePerRequestFilter jwtAuthFilter = getJwtAuthFilter();
	private final OncePerRequestFilter basicAuthFilter = getBasicAuthFilter();
	private final OncePerRequestFilter testFilter = getTestAuthFilter();
	
	private IMAuthUserProvider authUserProvider;

	@Value("${IS_DEV:true}")
	private boolean dev;
	
	
	private MGraphQLJwtService jwt;

	public SecurityFilterChain getSecurityFilterChain(HttpSecurity http, MGraphQLJwtService jwt, IMAuthUserProvider authUserProvider) throws Exception {
		this.jwt = jwt;
		this.authUserProvider = authUserProvider;
		return http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
				.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(getAuthenticationProvider)
				.addFilterBefore(testFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(auth -> {
					auth.regexMatchers(HttpMethod.GET, "/gui", "/graphql").permitAll();
					auth.regexMatchers(HttpMethod.POST, "/graphql").authenticated();
					auth.anyRequest().denyAll();
				}).build();
	}

	private OncePerRequestFilter getTestAuthFilter() {
		return new OncePerRequestFilter() {
			final String AUTHORIZATION = "Authorization";
			final String TEST = "Test";

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				
				final String authorizationHeader = request.getHeader(AUTHORIZATION);

				if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(TEST)) {
					filterChain.doFilter(request, response);
					return;
				}

				try {
					String token = authorizationHeader.replace(TEST, "").trim();
					
					int i = token.indexOf(":");
					
					String sid = token.substring(0, i);
					Long id = Long.parseLong(sid);
					String login = token.substring(i+1);
					
					MUser user = authUserProvider.getUserFromUsername(login);
					user.setRequestId(sid);
					user.setCode(id);
//					user.setUsername(login);
//					user.setRoles(new String[]{"SUPER"});
					
					
					
					SecurityContextHolder.getContext().setAuthentication(new MAuthToken(user));
				} catch (Exception e) {
					SecurityContextHolder.getContext().setAuthentication(null);
				}

				filterChain.doFilter(request, response);
			}
		};
	}

	private OncePerRequestFilter getBasicAuthFilter() {
		final String AUTHORIZATION = "Authorization";
		final String BASIC = "Basic";

		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {

				final String authorizationHeader = request.getHeader(AUTHORIZATION);
				if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BASIC)) {
					filterChain.doFilter(request, response);
					return;
				}

				try {
					String token = authorizationHeader.replace(BASIC, "").trim();

					MUser user = jwt.userFromToken(token, MUser.class);
					if (jwt.isValidateUser(user)) {
						SecurityContextHolder.getContext().setAuthentication(new MAuthToken(user));
					}
				} catch (Exception e) {
					SecurityContextHolder.getContext().setAuthentication(null);
				}

				filterChain.doFilter(request, response);
			}
		};
	}

	@Bean
	MGraphQLJwtService getMGraphQLJwtService() {
		return new MGraphQLJwtService();
	}

	private OncePerRequestFilter getJwtAuthFilter() {

		return new OncePerRequestFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {

				final String AUTHORIZATION = "Authorization";
				final String BEARER = "Bearer";

				final String authorizationHeader = request.getHeader(AUTHORIZATION);
				if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
					filterChain.doFilter(request, response);
					return;
				}

				log.info(authorizationHeader);

//				final String token = authorizationHeader.replace(BEARER, "").trim();
//				final String username;

				try {
				} catch (Exception e) {
					log.error(e.getMessage());
				}

//				username = jwtService.extractUsername(token);
//
//				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//					MUserDetails user = MUserDetails.to(userDetails.getUserFromUsername(username));
//					if (jwtService.isTokenValid(token, user)) {
//						Date expiration = jwtService.extractExpiration(token);
//						user.getUser().setAccessValidUntil(expiration);
//						
//						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//								user, null, user.getAuthorities());
//		                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//					}
//				}

				filterChain.doFilter(request, response);
			}
		};
	}

	private AuthenticationProvider getAuthenticationProvider = new AuthenticationProvider() {

		@Override
		public boolean supports(Class<?> authentication) {
			return true;
		}

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			return null;
		}
	};

}
