package foundation.cmo.opensales.graphql.ws;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MConnectionErrorMessage extends MPayloadMessage<Map<String, ?>> {
	@JsonCreator
	public MConnectionErrorMessage(@JsonProperty("payload") Map<String, ?> error) {
		super(null, GQL_CONNECTION_ERROR, error);
	}

}
