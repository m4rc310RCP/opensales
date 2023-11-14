package foundation.cmo.opensales.graphql.ws;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MErrorMessage extends MPayloadMessage<List<Map<String, ?>>> {

    @JsonCreator
    public MErrorMessage(@JsonProperty("id") String id, @JsonProperty("payload") List<Map<String, ?>> errors) {
        super(id, GQL_ERROR, errors);
    }
}
