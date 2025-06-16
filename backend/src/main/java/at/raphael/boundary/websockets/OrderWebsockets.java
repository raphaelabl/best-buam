package at.raphael.boundary.websockets;

import at.raphael.control.OrderService;
import at.raphael.control.PrintService;
import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.OrderPosition;
import at.raphael.entity.dto.BuffetOrderDTO;
import at.raphael.entity.dto.OrderSessions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.websocket.OnClose;
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
    @Inject
    ObjectMapper objectMapper;

    @Inject
    OrderService orderService;
    @Inject
    PrintService printService;

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
                sendBuffetOrder(openOrder);
            }

        } catch (Exception e) {
            log.error("Error retrieving buffet", e);
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        // Message is seperated in Task and an DataContent specially for this Task
        // for Example 'dispach/Buffet1_3413213 => <Task>/OrderId'

        String task = message.split("/")[0];
        String content = message.split("/")[1];

        //region On Order Completed
        if(task.equals("dispach")){

            Long orderId = Long.valueOf(content.split("_")[1]);  // Verbessert: Vermeide mehrfache Aufrufe von split()
            String buffetName = content.split("_")[0];

            // Outsource in external Function, because of blocking Problems in Websockets
            executor.execute(() -> {processOrderInTransaction(orderId, buffetName);});

        }
        //endregion

    }

    @Transactional
    public void processOrderInTransaction(Long orderId, String buffetName) {
        Order o = Order.findById(orderId);

        log.info("BUFFETNAME:" +buffetName);
        Buffet b = Buffet.find("name", buffetName).firstResult();

        if (o == null || b == null) {
            log.info("Order: " + o + ", Buffet: " + b);
            return;
        }

        //orderService.processOrder(o);


        BuffetOrderDTO dto = createBuffetOrder(o, b);

        for (OrderPosition orderPosition : dto.order.positions) {
            orderPosition.dispached = true;
        }

        printService.createBillAndPrint(o, b);

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

        return result;
    }

    public BuffetOrderDTO createBuffetOrder(Order o, Buffet b){
        BuffetOrderDTO result = new BuffetOrderDTO();

        result.id = b.name + "_" + o.id.toString();
        result.done = false;
        result.order = new Order(); // New Instance because otherwise it gets delted
        result.order.id = o.id;
        result.order.positions = o.positions;
        result.order.tableNr = o.tableNr;
        result.order.waiter = o.waiter;

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
            try {
                String orderJson = objectMapper.writeValueAsString(bodto);
                log.info("Sending to Session: " + bodto);
                os.session.getAsyncRemote().sendText(orderJson);
            } catch (Exception e) {
                log.error("Error serializing order", e);
            }
        }


    }


    public void sendBuffetOrder(BuffetOrderDTO bodto){
        try{
            String mappedBodto = objectMapper.writeValueAsString(bodto);
            orderSessions
                    .stream()
                    .filter(element -> Objects.equals(element.buffet.name, bodto.id.split("_")[0]))
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
