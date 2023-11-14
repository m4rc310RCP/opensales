package foundation.cmo.opensales.graphql.security;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MApolloProtocolHandler extends TextWebSocketHandler {
 @Override
protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	 super.handleTextMessage(session, message);
}
}
