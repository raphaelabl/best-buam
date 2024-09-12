package at.raphael.boundary.websockets;

import at.raphael.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.Set;


@ServerEndpoint("/order")
@ApplicationScoped
public class OrderWebsockets {

    @Inject
    Logger log;

    private Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("new Session Opened");

        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    public void sendOrderToAllClients(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper zum Serialisieren
        try {
            String orderJson = objectMapper.writeValueAsString(order);  // Order in JSON umwandeln
            for (Session session : sessions) {
                log.info("Sending to Session: " + order.id);
                session.getAsyncRemote().sendText(orderJson);  // JSON-String senden
            }
        } catch (Exception e) {
            log.error("Error serializing order", e);
        }
    }

    public void close(Session session) {
        sessions.remove(session);
    }

}
