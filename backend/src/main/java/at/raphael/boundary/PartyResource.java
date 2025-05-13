package at.raphael.boundary;

import at.raphael.control.KeycloakService;
import at.raphael.entity.Party;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Path("party")
public class PartyResource {

    @Inject
    KeycloakService keycloakService;

    // Admin / Party-Admin Endpoint
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

    @GET
    @Path("id")
    public Response getBuffetById(@QueryParam("id") long id) {
        Party party = Party.findById(id);
        return Response.ok(party).build();
    }

    @GET
    @Path("user")
    public Response getBuffetById(@QueryParam("userName") String userName) {
        Party p = Party.find("SELECT p FROM Party p JOIN p.waiters w WHERE w.username = ?1", userName).firstResult();
        return Response.ok(p).build();
    }


    @GET
    @Path("adminEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartyByAdmin(@QueryParam("adminEmail") String adminEmail) {
        List<Party> partyList = Party.find("partyAdmin.email", adminEmail).list();

        return Response.ok(partyList).build();
    }

}
