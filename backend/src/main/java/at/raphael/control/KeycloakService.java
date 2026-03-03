package at.raphael.control;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class KeycloakService {

    @ConfigProperty(name="keycloak.host")
    String host;
    @ConfigProperty(name="keycloak.realm")
    String realm;

    @ConfigProperty(name="keycloak.admin.username")
    String adminUsername;
    @ConfigProperty(name="keycloak.admin.password")
    String adminPassword;
    

    private Keycloak keycloak;


    public UserRepresentation getUser(String username) {

        return keycloak.realm(realm).users().list()
                .stream()
                .filter(element -> element.getUsername().equals(username))
                .findFirst()
                .orElse(null);

    }

    public boolean createUser(UserRepresentation user, String group, String password) {

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            // Passwort für den neu erstellten Benutzer setzen
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(true);

            keycloak.realm(realm).users().get(userId).resetPassword(credential);

            keycloak.realm(realm).groups().groups()
                    .stream()
                    .filter(element-> element.getName().equals(group))
                    .findFirst()
                    .ifPresent(groupRepresentation -> keycloak.realm(realm).users().get(userId).joinGroup(groupRepresentation.getId()));

        } else {
            System.out.println("Fehler beim Erstellen des Benutzers: " + response.getStatus());
            response.close();
            return false;
        }

        response.close();
        return true;
    }

    public boolean updateUser(UserRepresentation user) {
        keycloak.realm(realm).users().get(user.getId()).update(user);
        return true;
    }

    public boolean removeUserPerName(String username) {
        UserRepresentation user = getUser(username);

        if (user != null) {
            keycloak.realm(realm).users().delete(user.getId());
            return true;
        } else {
            System.out.println("Benutzer nicht gefunden: " + username);
            return false;
        }
    }

    @PostConstruct
    public void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(host)
                .realm("master")
                .clientId("admin-cli")
                .grantType("password")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    @PreDestroy
    public void closeKeycloak(){
        keycloak.close();
    }

}
