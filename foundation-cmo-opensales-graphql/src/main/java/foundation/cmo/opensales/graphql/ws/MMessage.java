package foundation.cmo.opensales.graphql.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MInitMessage.class, name = MMessage.GQL_CONNECTION_INIT),
        @JsonSubTypes.Type(value = MTerminateMessage.class, name = MMessage.GQL_CONNECTION_TERMINATE),
        @JsonSubTypes.Type(value = MStopMessage.class, name = MMessage.GQL_STOP),
        @JsonSubTypes.Type(value = MStartMessage.class, name = MMessage.GQL_START),
        @JsonSubTypes.Type(value = MErrorMessage.class, name = MMessage.GQL_CONNECTION_ERROR),
        @JsonSubTypes.Type(value = MErrorMessage.class, name = MMessage.GQL_ERROR),
})
public class MMessage {

	@JsonProperty("id") 
    private final String id;
	
	@JsonProperty("type")
    private final String type;

    //Client messages
    public static final String GQL_CONNECTION_INIT = "connection_init";
    public static final String GQL_CONNECTION_TERMINATE = "connection_terminate";
    public static final String GQL_START = "start";
    public static final String GQL_STOP = "stop";

    //Server messages
    public static final String GQL_CONNECTION_ACK = "connection_ack";
    public static final String GQL_CONNECTION_ERROR = "connection_error";
    public static final String GQL_CONNECTION_KEEP_ALIVE = "ka";
    public static final String GQL_DATA = "data";
    public static final String GQL_ERROR = "error";
    public static final String GQL_COMPLETE = "complete";
    
    public MMessage(String id, String type) {
    	this.id = id;
    	this.type = type;
    }

}
