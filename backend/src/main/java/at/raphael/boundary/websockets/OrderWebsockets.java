package at.raphael.boundary.websockets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.dto.BuffetOrderDTO;
import at.raphael.entity.dto.OrderSessions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/order/{buffetName}")
@ApplicationScoped
public class OrderWebsockets {

    @Inject
    Logger log;
    @Inject
    ManagedExecutor executor;
    @Inject
    ObjectMapper objectMapper;

    private Set<OrderSessions> orderSessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("buffetName") String buffetName){

        executor.execute(() -> connectToWebsocket(session, buffetName));

        log.info(orderSessions.size());
    }

    @Transactional
    void connectToWebsocket(Session session, String buffetName){
        OrderSessions orderSession = new OrderSessions();
        orderSession.session = session;

        try {
            orderSession.buffet = Buffet.find("login", buffetName).firstResult();
            orderSessions.add(orderSession);

            List<BuffetOrderDTO> openOrders = this.getOpenOrdersForBuffet(buffetName);
            for (BuffetOrderDTO openOrder : openOrders) {
                sendBuffetOrder(openOrder, buffetName);
            }

        } catch (Exception e) {
            log.error("Error retrieving buffet", e);
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String[] splitMessage = message.split(":");
        if(splitMessage.length != 2) return;
        if(splitMessage[0].equals("ping")){
            // Only if Websocket is connected with right buffet name
            orderSessions.stream()
                    .filter(element -> element.buffet.login.equals(splitMessage[1]))
                    .findFirst()
                    .ifPresent(element ->
                    session.getAsyncRemote().sendText("pong"));
        }
    }


    @Transactional
    public List<BuffetOrderDTO> getOpenOrdersForBuffet(String buffetName) {
        Buffet buffet = Buffet.find("login", buffetName).firstResult();
        List<BuffetOrderDTO> result = new ArrayList<>();

        if (buffet == null) {
            throw new IllegalArgumentException("Buffet with name " + buffetName + " not found");
        }

        List<Order> ordersWithOpenPositions = Order.find(
                "select distinct o from Order o join o.positions p where p.dispached = false and p.item.id in ?1",
                buffet.items.stream().map(element -> element.id).toList()
        ).list();


        for (Order order : ordersWithOpenPositions) {
            BuffetOrderDTO dto = createBuffetOrder(order, buffet);

            if (!dto.order.positions.isEmpty()) {
                result.add(dto);
            }
        }

        if(!result.isEmpty()) {
            result.sort((a,b) -> a.id.compareTo(b.id));
        }

        return result;
    }

    public BuffetOrderDTO createBuffetOrder(Order o, Buffet b){
        BuffetOrderDTO result = new BuffetOrderDTO();

        result.id = o.id.toString();
        result.order = new Order(); // New Instance because otherwise it gets delted
        result.order.id = o.id;
        result.order.positions = o.positions;
        result.order.tableNr = o.tableNr;
        result.order.waiter = o.waiter;
        log.info("Waiter: "+o.waiter.id+":" + o.waiter.firstName + " " + o.waiter.lastName);

        // Filtere nur die Positionen für den spezifischen Buffet
        result.order.positions = result.order.positions.stream()
                .filter(element -> b.items.stream()
                        .anyMatch(bI -> Objects.equals(bI.id, element.item.id)))
                .toList();

        return result;
    }


    public void sendOrderToAllClients(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper zum Serialisieren

        for(OrderSessions os: orderSessions){
            BuffetOrderDTO bodto = createBuffetOrder(order, os.buffet);
            if(bodto.order.positions.isEmpty()) continue;
            try {
                String orderJson = objectMapper.writeValueAsString(bodto);
                log.info("Sending to Session: " + bodto);
                os.session.getAsyncRemote().sendText(orderJson);
            } catch (Exception e) {
                log.error("Error serializing order", e);
            }
        }
    }


    public void sendBuffetOrder(BuffetOrderDTO bodto, String buffetName){
        try{
            String mappedBodto = objectMapper.writeValueAsString(bodto);
            orderSessions
                    .stream()
                    .filter(element -> Objects.equals(element.buffet.login, buffetName))
                    .findFirst()
                    .ifPresent(sessions ->
                            sessions.session.getAsyncRemote().sendText(mappedBodto)
                    );


        } catch (JsonProcessingException e) {
            log.error("Error serializing order", e);
        }
    }

    //check if buffet has active websocket connection
    public boolean isBuffetActive(Long buffetId){
        return orderSessions.stream().map(session -> session.buffet.id).anyMatch(buffetId::equals);
    }

    @OnClose
    public void close(Session session) {
        Optional<OrderSessions> os = orderSessions.stream().filter(element -> element.session.equals(session)).findFirst();

        os.ifPresent(sessions -> orderSessions.remove(sessions));
    }

}
