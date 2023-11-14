package foundation.cmo.opensales.graphql.ws;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.leangen.graphql.spqr.spring.web.dto.GraphQLRequest;

public class MStartMessage extends MPayloadMessage<GraphQLRequest> {

    @JsonCreator
    public MStartMessage(@JsonProperty("id") String id, @JsonProperty("type") String type, @JsonProperty("payload") GraphQLRequest payload) {
        super(id, GQL_START, payload);
    }
}
