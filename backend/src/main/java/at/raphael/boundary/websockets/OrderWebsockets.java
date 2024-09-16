package at.raphael.boundary.websockets;

import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.dto.BuffetOrderDTO;
import at.raphael.entity.dto.OrderSessions;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.util.*;


@ServerEndpoint("/order/{buffetName}")
@ApplicationScoped
public class OrderWebsockets {

    @Inject
    Logger log;

    @Inject
    ManagedExecutor executor;

    private Set<OrderSessions> orderSessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("buffetName") String buffetName){

        executor.execute(() -> {
            OrderSessions orderSession = new OrderSessions();
            orderSession.session = session;

            try {
                orderSession.buffet = Buffet.find("name", buffetName).firstResult();
                orderSessions.add(orderSession);
            } catch (Exception e) {
                log.error("Error retrieving buffet", e);
            }
        });
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Return when order is finished
    }

    public void sendOrderToAllClients(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper zum Serialisieren

        for(OrderSessions os: orderSessions){
            BuffetOrderDTO bodto = createBuffetOrder(order, os.buffet);
            os.ordersToDo.add(bodto);
            try {
                String orderJson = objectMapper.writeValueAsString(bodto);
                log.info("Sending to Session: " + bodto);
                os.session.getAsyncRemote().sendText(orderJson);
            } catch (Exception e) {
                log.error("Error serializing order", e);
            }
        }


    }

    public BuffetOrderDTO createBuffetOrder(Order o, Buffet b){
        BuffetOrderDTO result = new BuffetOrderDTO();

        result.id = b.name + "_" + o.id.toString();
        result.done = false;
        result.order = o;
        result.order.positions = result.order.positions.stream().filter(element -> b.items.stream().anyMatch(bI -> Objects.equals(bI.id, element.item.id))).toList();

        return result;
    }

    public void close(Session session) {
        Optional<OrderSessions> os = orderSessions.stream().filter(element -> element.session.equals(session)).findFirst();

        os.ifPresent(sessions -> orderSessions.remove(sessions));
        return;
    }

}
