package at.raphael.boundary;

import at.raphael.control.KeycloakService;
import at.raphael.entity.Buffet;
import at.raphael.entity.PartyAdmin;
import at.raphael.entity.Waiter;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

@Path("keycloak")
public class KeycloakResource {


    @Inject
    KeycloakService keycloakService;

    @POST
    @Path("buffet")
    public Response createOrUpdateBuffet(Buffet buffet) {
        UserRepresentation existingUser = keycloakService.getUser(buffet.login);

        if ((buffet.id == null || buffet.id == 0) && existingUser == null) {

            UserRepresentation newUser = new UserRepresentation();
            newUser.setFirstName(buffet.name);
            newUser.setLastName(buffet.name);
            newUser.setEmail(buffet.name + "@" + buffet.name);
            newUser.setEnabled(true);
            newUser.setUsername(buffet.login.toLowerCase());

            if (this.keycloakService.createUser(newUser, "buffet", buffet.password)) {
                return Response.ok(true).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht erstellt werden").build();
        }

        if (existingUser != null) {
            return updateBuffet(buffet, existingUser);
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Benutzer nicht gefunden").build();
        }
    }

    private Response updateBuffet(Buffet buffet, UserRepresentation user) {
        user.setFirstName(buffet.name);
        user.setLastName(buffet.name);
        user.setEmail(buffet.name + "@" + buffet.name);

        if (this.keycloakService.updateUser(user)) {
            return Response.ok("Benutzer erfolgreich aktualisiert").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht aktualisiert werden").build();
        }
    }

    @DELETE
    @Path("buffet/{buffetLogin}")
    public Response deleteBuffet(@PathParam("buffetLogin")String buffet){

        if (this.keycloakService.removeUserPerName(buffet)) {
            return Response.ok("Benutzer erfolgreich entfernt").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht entfernt werden").build();
        }
    }



    @POST
    @Path("party-admin")
    public Response createOrUpdatePartyAdmin(PartyAdmin partyAdmin) {
        UserRepresentation existingUser = keycloakService.getUser(partyAdmin.username);

        if ((partyAdmin.id == null || partyAdmin.id == 0) && existingUser == null) {

            UserRepresentation newUser = new UserRepresentation();
            newUser.setFirstName(partyAdmin.firstName);
            newUser.setLastName(partyAdmin.lastName);
            newUser.setEmail(partyAdmin.email);
            newUser.setEnabled(true);
            newUser.setUsername(partyAdmin.username.toLowerCase());

            if (this.keycloakService.createUser(newUser, "party-admin", partyAdmin.password)) {
                return Response.ok(true).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht erstellt werden").build();
        }

        if (existingUser != null) {
            return updatePartyAdmin(partyAdmin, existingUser);
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Benutzer nicht gefunden").build();
        }
    }

    private Response updatePartyAdmin(PartyAdmin partyAdmin, UserRepresentation user) {
        user.setFirstName(partyAdmin.firstName);
        user.setLastName(partyAdmin.lastName);
        user.setEmail(partyAdmin.email);

        if (this.keycloakService.updateUser(user)) {
            return Response.ok("Benutzer erfolgreich aktualisiert").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht aktualisiert werden").build();
        }
    }


    @DELETE
    @Path("party-admin/{partyAdminUsername}")
    public Response deletePartyAdmin(@PathParam("partyAdminUsername")String partyAdmin) {

        if (this.keycloakService.removeUserPerName(partyAdmin)) {
            return Response.ok("Benutzer erfolgreich entfernt").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht entfernt werden").build();
        }
    }

    @POST
    @Path("waiter")
    public Response createOrUpdateWaiter(Waiter waiter) {
        UserRepresentation existingUser = keycloakService.getUser(waiter.username);

        if ((waiter.id == null || waiter.id == 0) && existingUser == null) {

                UserRepresentation newUser = new UserRepresentation();
                newUser.setFirstName(waiter.firstName);
                newUser.setLastName(waiter.lastName);
                newUser.setEmail(waiter.email);
                newUser.setEnabled(true);
                newUser.setUsername(waiter.username.toLowerCase());

                if (this.keycloakService.createUser(newUser, "waiter", waiter.password)) {
                    return Response.ok(true).build();
                }

                return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht erstellt werden").build();
        }

        if (existingUser != null) {
            return updateWaiter(waiter, existingUser);
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Benutzer nicht gefunden").build();
        }
    }

    private Response updateWaiter(Waiter waiter, UserRepresentation user) {
        user.setFirstName(waiter.firstName);
        user.setLastName(waiter.lastName);
        user.setEmail(waiter.email);

        if (this.keycloakService.updateUser(user)) {
            return Response.ok("Benutzer erfolgreich aktualisiert").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht aktualisiert werden").build();
        }
    }

    @DELETE
    @Path("waiter/{waiterUserName}")
    public Response deleteWaiter(@PathParam("waiterUserName") String waiter){

        if (this.keycloakService.removeUserPerName(waiter)) {
            return Response.ok("Benutzer erfolgreich entfernt").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Benutzer konnte nicht entfernt werden").build();
        }
    }

}
