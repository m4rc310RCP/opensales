package foundation.cmo.opensales.graphql.security;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import foundation.cmo.opensales.graphql.security.dto.MUser;

public class MAuthToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	public MAuthToken(MUser user) {
		super(user, user.getRequestId(),
				Arrays.stream(user.getRoles()).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}

}
