package foundation.cmo.opensales.graphql.security;

import foundation.cmo.opensales.graphql.security.dto.MUser;

public interface IMAuthUserProvider {
	MUser authUser(String username, Object password) throws Exception;
	MUser getUserFromUsername(String username) ;
	boolean isValidUser(MUser user);
}
