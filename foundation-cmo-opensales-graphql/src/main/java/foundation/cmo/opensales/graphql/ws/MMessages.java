package foundation.cmo.opensales.graphql.ws;

import static io.leangen.graphql.spqr.spring.web.apollo.ApolloMessage.GQL_COMPLETE;
import static io.leangen.graphql.spqr.spring.web.apollo.ApolloMessage.GQL_CONNECTION_ACK;
import static io.leangen.graphql.spqr.spring.web.apollo.ApolloMessage.GQL_CONNECTION_KEEP_ALIVE;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ErrorType;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import io.leangen.graphql.spqr.spring.web.apollo.ApolloMessage;
import io.leangen.graphql.spqr.spring.web.apollo.ConnectionErrorMessage;
import io.leangen.graphql.spqr.spring.web.apollo.DataMessage;
import io.leangen.graphql.spqr.spring.web.apollo.ErrorMessage;

public class MMessages {

	private static final MMessage CONNECTION_ACK = new MMessage(null, GQL_CONNECTION_ACK);
	private static final MMessage KEEP_ALIVE     = new MMessage(null, GQL_CONNECTION_KEEP_ALIVE);

	private static final ObjectMapper mapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	
	public static MMessage from(TextMessage message) throws IOException{
		return mapper.readValue(message.getPayload(), MMessage.class);
	}

    public static TextMessage connectionAck() throws JsonProcessingException {
        return jsonMessage(CONNECTION_ACK);
    }

    public static TextMessage keepAlive() throws JsonProcessingException {
        return jsonMessage(KEEP_ALIVE);
    }
    
    public static TextMessage connectionError(final String message) throws JsonProcessingException {
        return jsonMessage(new MConnectionErrorMessage(Collections.singletonMap("message", message)));
    }
    
    public static TextMessage connectionError() throws JsonProcessingException {
        return connectionError("Invalid message");
    }

    
    public static TextMessage data(String id, ExecutionResult result) throws JsonProcessingException {
        return jsonMessage(new MDataMessage(id, result));
    }

    public static TextMessage complete(String id) throws JsonProcessingException {
        return jsonMessage(new MMessage(id, GQL_COMPLETE));
    }

    public static TextMessage error(String id, List<GraphQLError> errors) throws JsonProcessingException {
        return jsonMessage(new MErrorMessage(id, errors.stream()
                .filter(error -> !error.getErrorType().equals(ErrorType.DataFetchingException))
                .map(GraphQLError::toSpecification)
                .collect(Collectors.toList())));
    }

    public static TextMessage error(String id, Throwable exception) throws JsonProcessingException {
        return error(id, exception.getMessage());
    }

    public static TextMessage error(String id, String message) throws JsonProcessingException {
        return jsonMessage(new MErrorMessage(id, Collections.singletonList(Collections.singletonMap("message", message))));
    }

    private static TextMessage jsonMessage(MMessage message) throws JsonProcessingException {
        return new TextMessage(mapper.writeValueAsString(message));
    }

}
