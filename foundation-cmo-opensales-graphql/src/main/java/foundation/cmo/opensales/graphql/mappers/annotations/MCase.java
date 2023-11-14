package foundation.cmo.opensales.graphql.mappers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MCase {
	
	Case value() default Case.NONE;
	
	public enum Case{
		UPPER, LOWER, NONE		
	}
}
