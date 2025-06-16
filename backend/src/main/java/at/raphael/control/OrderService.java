package at.raphael.control;

import at.raphael.boundary.websockets.OrderWebsockets;
import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.OrderPosition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class OrderService {


    @Inject
    PrintService printService;

    @Inject
    OrderWebsockets orderWebsockets;

    @Inject
    Logger logger;

    public Order processOrder(Order order){

        List<Buffet> inOrderIncludedBuffets = getBuffetsFromOrder(order);

        for (Buffet buffet : inOrderIncludedBuffets) {
            if(!orderWebsockets.isBuffetActive(buffet.id)) {
                printService.createBillAndPrint(order, buffet);
            }
        }

        return order;

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

}
