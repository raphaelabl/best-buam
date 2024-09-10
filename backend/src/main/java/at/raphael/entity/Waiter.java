package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Waiter extends PanacheEntity {

    public String firstName;
    public String lastName;
    public String role;

    public String email;
    public String username;
    public String password;

    public Waiter() {
    }

    public void updateEntity(Waiter newEntity) {

        this.firstName = newEntity.firstName;
        this.lastName = newEntity.lastName;

        this.role = newEntity.role;
        this.email = newEntity.email;

        this.username = newEntity.username;
        this.password = newEntity.password;

    }

    public Waiter persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;

            this.persist();
            return this;
        }

        Waiter waiter = Waiter.findById(this.id);
        waiter.updateEntity(this);
        return waiter;
    }

}
