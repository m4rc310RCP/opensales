package foundation.cmo.opensales.graphql.ws;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import graphql.ExecutionResult;

public class MDataMessage extends MPayloadMessage<Map<String, Object>> {

    @JsonCreator
    public MDataMessage(@JsonProperty("id") String id, @JsonProperty("payload") ExecutionResult payload) {
        super(id, GQL_DATA, payload.toSpecification());
    }
    
}
