package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Item extends PanacheEntity {

    public String name;
    public double price;

    public Item() {
    }

    public void updateEntity(Item newEntity) {
        this.name = newEntity.name;
        this.price = newEntity.price;
    }

    public Item persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;

            this.persist();
            return this;
        }

        Item item = Item.findById(this.id);
        item.updateEntity(this);
        return item;
    }

}
