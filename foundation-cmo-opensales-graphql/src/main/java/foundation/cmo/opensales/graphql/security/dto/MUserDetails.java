package foundation.cmo.opensales.graphql.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "from")
public class MUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private final MUser user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> ret = new ArrayList<>();
		String[] roles = user.getRoles();
		if(Objects.nonNull(roles)) {
			for(String role : roles) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				ret.add(authority);
			}
		}
		return ret;
	}

	@Override
	public String getPassword() {
		return user.getUsername();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
