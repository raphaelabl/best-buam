package at.raphael.boundary;

import at.raphael.boundary.websockets.OrderWebsockets;
import at.raphael.control.OrderService;
import at.raphael.entity.Order;
import at.raphael.entity.Waiter;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("order")
public class OrderResource {
    @Inject
    OrderService orderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response postOrder(Order order) {
        return orderService.processOrder(order);
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


    @GET
    @Path("dispatch")
    @Transactional
    public Response dispatchBuffetOrder(@QueryParam("buffetName") String buffetName, @QueryParam("orderId") Long orderId){
        // If Buffetname or OrderId is not Found, return error not found
        if(buffetName == null || orderId == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity((buffetName == null)?"BuffetName":"OrderId"+" is missing")
                    .build();
        }

        if(orderService.DispatchOrder(buffetName, orderId)) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Order could not be dispatched").build();

    }

}
