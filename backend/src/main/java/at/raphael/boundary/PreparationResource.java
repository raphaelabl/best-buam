package at.raphael.boundary;

import at.raphael.entity.Buffet;
import at.raphael.entity.OrderPosition;
import at.raphael.entity.dto.PreparationDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("preparation")
public class PreparationResource {

    @GET
    public Response getAllPreparations(@QueryParam("buffetName") String buffetName) {
        // For this Rest Endpoint no Service needed
        List<PreparationDTO> preparations = getPreparationList(buffetName);

        if(preparations.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(preparations).build();
    }

    public List<PreparationDTO> getPreparationList(String buffetName){
        List<PreparationDTO> preparations = new ArrayList<>();

        Buffet buffet = Buffet.find("login", buffetName).firstResult();
        if(buffet == null) {return List.of();}

        List<Long> itemIds = buffet.items.stream()
                .map(item -> item.id)
                .toList();

        List<OrderPosition> allPositions = OrderPosition.find("item.id in ?1", itemIds).list();

        return buffet.items.stream().map(item -> {


            int dispatched = allPositions.stream()
                    .filter(p -> p.item.id.equals(item.id) && p.dispached)
                    .mapToInt(p -> p.amount).sum();

            int notDispatched = allPositions.stream()
                    .filter(p -> p.item.id.equals(item.id) && !p.dispached)
                    .mapToInt(p -> p.amount).sum();

            return new PreparationDTO(item.name, dispatched, notDispatched);
        }).toList();
    }



}
