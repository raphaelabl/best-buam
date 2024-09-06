package boundary;

import at.raphael.entity.Party;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("party")
public class PartyResource {

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postParty(Party party) {
        party = party.persistOrUpdate();

        return Response.ok(party).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParty() {
        List<Party> partyList = Party.listAll();
        return Response.ok(partyList).build();
    }

}
