package foundation.cmo.opensales.graphql.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface MReportRegistryInfo.
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface MReportRegistryInfo {

}
