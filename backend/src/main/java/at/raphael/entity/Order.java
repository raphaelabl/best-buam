package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bill_order")
public class Order extends PanacheEntity {

    public int tableNr;

    @ManyToOne
    public Waiter waiter;

    @OneToMany
    public List<OrderPosition> positions;

    public boolean status;


    public Order() {

    }

    public void updateEntity(Order newEntity) {
        this.tableNr = newEntity.tableNr;
        this.waiter = newEntity.waiter.persistOrUpdate();
        this.status = newEntity.status;

        this.positions = new ArrayList<>();
        for(OrderPosition pos : newEntity.positions) {
            this.positions.add(pos.persistOrUpdate());
        }

    }

    public Order persistOrUpdate(){
        if(this.id == null || this.id == 0) {
            this.id = null;
            this.persist();

            this.waiter = this.waiter.persistOrUpdate();

            for (OrderPosition pos : this.positions) {
                pos.persistOrUpdate();
            }

            return this;
        }

        Order order = Order.findById(this.id);
        order.updateEntity(this);
        return order;
    }
}
