package foundation.cmo.opensales.graphql.ws;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MTerminateMessage extends MPayloadMessage<Map<String, Object>> {

    @JsonCreator
    public MTerminateMessage(@JsonProperty("id") String id, @JsonProperty("type") String type, @JsonProperty("payload") Map<String, Object> payload) {
        super(id, GQL_CONNECTION_TERMINATE, payload);
    }
}