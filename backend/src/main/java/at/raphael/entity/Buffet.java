package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Buffet extends PanacheEntity {

    public String name;

    @OneToMany
    public List<Item> items;

    @OneToMany
    public List<Printer> printers;


    public Buffet() {
    }

    public void updateEntity(Buffet newEntity) {
        this.name = newEntity.name;

        this.items = new ArrayList<>();
        for (Item item : newEntity.items) {
            this.items.add(item.persistOrUpdate());
        }

        this.printers = new ArrayList<>();
        for (Printer printer : newEntity.printers) {
            this.printers.add(printer.persistOrUpdate());
        }

    }

    public Buffet persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;
            this.persist();

            for (Item item : this.items) {
                item.persistOrUpdate();
            }

            for (Printer printer : this.printers) {
                printer.persistOrUpdate();
            }


            return this;
        }

        Buffet buffet = Buffet.findById(this.id);
        buffet.updateEntity(this);
        return buffet;
    }


}
