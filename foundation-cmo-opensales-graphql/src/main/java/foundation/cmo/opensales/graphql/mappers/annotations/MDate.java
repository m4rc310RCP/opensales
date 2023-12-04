package foundation.cmo.opensales.graphql.mappers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface MDate.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MDate {
	
	/**
	 * Value.
	 *
	 * @return the string
	 */
	String value() default "dd/MM/yyyy HH:mm:ss";
	
	/**
	 * Unix format.
	 *
	 * @return true, if successful
	 */
	boolean unixFormat() default false;
}