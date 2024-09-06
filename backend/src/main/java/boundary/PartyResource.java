package boundary;

import at.raphael.entity.Party;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("party")
public class PartyResource {

    @POST
    @Transactional
    public Response postParty(Party party) {
        party = party.persistOrUpdate();

        return Response.ok(party).build();
    }



}
