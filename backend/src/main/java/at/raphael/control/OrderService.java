package at.raphael.control;

import at.raphael.entity.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    PrintService printService;

    public Order processOrder(Order order){

        Order persisted = order.persistOrUpdate();

        printService.printOrder(persisted);

        return persisted;

    }

}
