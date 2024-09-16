package at.raphael.boundary;


import at.raphael.entity.Buffet;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("buffet")
public class BuffetResource {

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBuffet(Buffet buffet) {
        buffet = buffet.persistOrUpdate();

        return Response.ok(buffet).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBuffet() {
        List<Buffet> buffetList = Buffet.listAll();
        return Response.ok(buffetList).build();
    }

    @GET
    @Path("ById")
    public Response getBuffetById(@QueryParam("id") long id) {
        Buffet buffet = Buffet.findById(id);
        return Response.ok(buffet).build();
    }

}
