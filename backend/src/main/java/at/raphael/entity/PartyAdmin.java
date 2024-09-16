package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class PartyAdmin extends PanacheEntity {

    public String firstName;
    public String lastName;
    public String email;
    public String phone;

    public PartyAdmin() {
    }

    public PartyAdmin(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void update(PartyAdmin newEntity){
        this.firstName = newEntity.firstName;
        this.lastName = newEntity.lastName;
        this.email = newEntity.email;
        this.phone = newEntity.phone;
    }

    public PartyAdmin persistOrUpdate(){

        if(this.id != null && this.id != 0){
            PartyAdmin persisted = PartyAdmin.findById(this.id);
            persisted.update(this);
            return persisted;
        }

        this.id = null;
        this.persist();
        return this;

    }

}
