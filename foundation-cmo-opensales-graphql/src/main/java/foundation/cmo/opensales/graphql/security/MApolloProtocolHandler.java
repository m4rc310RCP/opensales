/*
 * 
 */
package foundation.cmo.opensales.graphql.security;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * The Class MApolloProtocolHandler.
 */
public class MApolloProtocolHandler extends TextWebSocketHandler {
 
 /**
	 * Handle text message.
	 *
	 * @param session the session
	 * @param message the message
	 * @throws Exception the exception
	 */
 @Override
protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	 super.handleTextMessage(session, message);
}
}
