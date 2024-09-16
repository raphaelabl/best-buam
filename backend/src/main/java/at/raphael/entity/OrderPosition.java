package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderPosition extends PanacheEntity {

    public String editId;

    @ManyToOne
    public Item item;

    public int amount;
    public String spezialText;
    public boolean isSpezial;

    public OrderPosition() {
    }

    public void updateEntity(OrderPosition newEntity) {
        this.editId = newEntity.editId;
        this.amount = newEntity.amount;
        this.spezialText = newEntity.spezialText;
        this.isSpezial = newEntity.isSpezial;

        this.item = newEntity.item.persistOrUpdate();
    }

    public OrderPosition persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;
            this.persist();

            this.item = this.item.persistOrUpdate();

            return this;
        }

        OrderPosition orderPosition = OrderPosition.findById(this.id);
        orderPosition.updateEntity(this);
        return orderPosition;
    }

}
