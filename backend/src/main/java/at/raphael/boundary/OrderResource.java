package at.raphael.boundary;

import at.raphael.boundary.websockets.OrderWebsockets;
import at.raphael.entity.Order;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("order")
public class OrderResource {

    @Inject
    OrderWebsockets orderWebsockets;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postOrder(Order order) {
        Order persisted = order.persistOrUpdate();

        orderWebsockets.sendOrderToAllClients(persisted);

        return Response.ok(persisted).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder() {
        List<Order> orders = Order.listAll();
        return Response.ok(orders).build();
    }

    @GET
    @Path("id")
    public Response getBuffetById(@QueryParam("id") long id) {
        Order order = Order.findById(id);
        return Response.ok(order).build();
    }

}
