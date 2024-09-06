package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.util.Date;

@Entity
public class Party extends PanacheEntity {

    // Generally Event information
    public String name;
    public String organization;

    @OneToOne
    public PartyAdmin partyAdmin;

    // Date from - to the waiters can access
    public Date startDate;
    public Date endDate;

    // Party Admin insight and organization Time
    public int leadTime;
    public int followUpTime;


    public Party() {
    }


    public void updateEntity(Party newEntity) {

        this.name = newEntity.name;
        this.organization = newEntity.organization;
        this.startDate = newEntity.startDate;
        this.endDate = newEntity.endDate;
        this.leadTime = newEntity.leadTime;
        this.followUpTime = newEntity.followUpTime;

        this.partyAdmin = newEntity.partyAdmin.persistOrUpdate();

    }

    public Party persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;
            this.partyAdmin = this.partyAdmin.persistOrUpdate();

            this.persist();
            return this;
        }

        Party party = Party.findById(this.id);
        party.updateEntity(this);
        return party;
    }
}
