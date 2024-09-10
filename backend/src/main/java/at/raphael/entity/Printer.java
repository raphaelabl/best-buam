package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Printer extends PanacheEntity {

    public String name;
    public String ipAddress;

    public Printer() {
    }

    public void updateEntity(Printer newEntity) {
        this.name = newEntity.name;
        this.ipAddress = newEntity.ipAddress;
    }

    public Printer persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;

            this.persist();
            return this;
        }

        Printer printer = Printer.findById(this.id);
        printer.updateEntity(this);
        return printer;
    }

}
