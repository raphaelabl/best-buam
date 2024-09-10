package at.raphael.boundary;

import at.raphael.entity.Item;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("item")
public class ItemResource {

    @GET
    public Response getAllItems() {
        List<Item> items = Item.listAll();
        return Response.ok(items).build();
    }


}
