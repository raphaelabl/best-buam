package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Party extends PanacheEntity {

    // Generally Event information
    public String name;
    public String organization;

    @OneToOne
    public PartyAdmin partyAdmin;

    @OneToMany
    public List<Waiter> waiters;

    @OneToMany
    public List<Buffet> buffets;

    // Date from - to the waiters can access
    public Date startDate;
    public Date endDate;

    // Party Admin insight and organization Time
    public int leadTime;
    public int followUpTime;


    public Party() {
    }


    public void updateEntity(Party newEntity) {

        // Update Party Data
        this.name = newEntity.name;
        this.organization = newEntity.organization;
        this.startDate = newEntity.startDate;
        this.endDate = newEntity.endDate;
        this.leadTime = newEntity.leadTime;
        this.followUpTime = newEntity.followUpTime;

        // Update PartyAdmin
        this.partyAdmin = newEntity.partyAdmin.persistOrUpdate();

        // Update Waiters
        this.waiters = new ArrayList<>();
        for(Waiter w : newEntity.waiters) {
            this.waiters.add(w.persistOrUpdate());
        }

        // Update Buffets
        this.buffets = new ArrayList<>();
        for(Buffet b : newEntity.buffets) {
            this.buffets.add(b.persistOrUpdate());
        }


    }

    public Party persistOrUpdate(){
        // Check if Party is already existing
        if(this.id == null || this.id == 0) {
            this.id = null;
            this.persist();

            // Persist Party-Admin
            this.partyAdmin = this.partyAdmin.persistOrUpdate();

            // Persist Waiters
            for(Waiter w : this.waiters) {
                w.persistOrUpdate();
            }

            // Persist Buffets
            for(Buffet b : this.buffets) {
                b.persistOrUpdate();
            }

            return this;
        }

        // Update if Party is already existing
        Party party = Party.findById(this.id);
        party.updateEntity(this);
        return party;
    }
}
