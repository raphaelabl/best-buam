package at.raphael.control;

import at.raphael.boundary.websockets.OrderWebsockets;
import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.OrderPosition;
import at.raphael.entity.dto.BuffetOrderDTO;
import at.raphael.entity.dto.OrderPrintDTO;
import at.raphael.entity.dto.PositionDTO;
import at.raphael.entity.dto.PrinterDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @Inject
    EntityManager entityManager;

    @Inject
    PrintService printService;

    @Inject
    OrderWebsockets orderWebsockets;

    @Inject
    Logger log;

    @Transactional
    public Response processOrder(Order order){

        Order persisted = order.persistOrder();

        if(persisted == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Order is already persited").build();
        }

        List<Buffet> inOrderIncludedBuffets = getBuffetsFromOrder(order);

        orderWebsockets.sendOrderToAllClients(order);

        for (Buffet buffet : inOrderIncludedBuffets) {
            if(!orderWebsockets.isBuffetActive(buffet.id)) {
                List<OrderPosition> positions = printService.createBillAndPrint(order, buffet);

                // Settings all Directly Printed Order Positions to dispached
                for (OrderPosition position : positions) {
                    order.positions
                            .stream()
                            .filter(element -> Objects.equals(element.id, position.id))
                            .findFirst()
                            .ifPresent(element -> element.dispached = true);
                }
            }
        }

        return Response.ok(order).build();

    }



    public List<Buffet> getBuffetsFromOrder(Order order){
        List<Buffet> allBuffets = Buffet.listAll();
        List<Buffet> includedBuffets = new ArrayList<>();


        // Select all Buffets from an order
        for (OrderPosition position : order.positions) {
            for (Buffet buffet : allBuffets) {
                boolean itemInBuffet = buffet.items.stream()
                        .anyMatch(item -> Objects.equals(item.id, position.item.id));
                if (itemInBuffet && !includedBuffets.contains(buffet)) {
                    includedBuffets.add(buffet);
                }
            }
        }

        return includedBuffets;
    }

    @Transactional
    public OrderPrintDTO DispatchOrder(String buffetName, Long orderId) {
        Order o = Order.findById(orderId);

        log.info("BUFFETNAME:" +buffetName);
        Buffet b = Buffet.find("login", buffetName).firstResult();

        if (o == null || b == null) {
            log.info("Order: " + o + ", Buffet: " + b);
            return null;
        }
        //orderService.processOrder(o);

        List<OrderPosition> dto = createBuffetOrder(o, b);

        // Hier eine Copy verwenden

        for (OrderPosition orderPosition : dto) {
            orderPosition.dispached = true;
        }

        OrderPrintDTO hardcopy = createHardcopy(o, b, dto);
        // Durch die übergabe des hardcopy objectes wird der fehler geworfen!


        return hardcopy;
    }

    public void createBillAndPrint(OrderPrintDTO printDTO){
        // Filter all Order Positions from Order with the same buffet


        String bill = printService.createStringForPrintFromHardcopy(printDTO);
        printService.sendToPrintersFromBuffetFromDTO(bill, printDTO.printers());

    }


    // Unbrauchbar da das managed entity in ein anderes objekt geladen wird
    public List<OrderPosition> createBuffetOrder(Order order, Buffet buffet){

        Set<Long> buffetItemIds = buffet.items.stream()
                .map(item -> item.id)
                .collect(Collectors.toSet());
        return order.positions.stream()
                .filter(pos->buffetItemIds.contains(pos.item.id))
                .toList();

        /*BuffetOrderDTO result = new BuffetOrderDTO();

        result.id = b.name + "_" + o.id.toString();
        result.order = new Order(); // New Instance because otherwise it gets delted
        result.order.id = o.id;
        result.order.positions = o.positions;
        result.order.tableNr = o.tableNr;
        result.order.waiter = o.waiter;

        log.info("Waiter: " + result.order.waiter.firstName + " " + result.order.waiter.lastName);

        // Filtere nur die Positionen für den spezifischen Buffet
        result.order.positions = result.order.positions.stream()
                .filter(element -> b.items.stream()
                        .anyMatch(bI -> Objects.equals(bI.id, element.item.id)))
                .toList();

        return result;*/
    }

    public OrderPrintDTO createHardcopy(Order order, Buffet buffet, List<OrderPosition> orderPositions){
        List<PositionDTO> positions = orderPositions.stream()
                .map(pos -> new PositionDTO(pos.item.name, pos.amount, pos.spezialText, pos.isSpezial))
                .toList();

        List<PrinterDTO> printerDTOList = buffet.printers
                .stream()
                .map(printer -> new PrinterDTO(printer.name, printer.ipAddress, Integer.parseInt(printer.port)))
                .toList();

        return new OrderPrintDTO(order.id, order.tableNr, order.waiter.firstName + " " + order.waiter.lastName,buffet.name,positions, printerDTOList);
    }


}
