package foundation.cmo.opensales.graphql.security;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface MAuth.
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface MAuth {
	
	/**
	 * Roles required.
	 *
	 * @return the string[]
	 */
	String[] rolesRequired();
}
