package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

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


    public Order persistOrder(){
        if(this.id == null || this.id == 0) {
            this.id = null;

            if (this.waiter != null && this.waiter.username != null) {
                this.waiter = Waiter.find("username", this.waiter.username).firstResult();
            }

            this.persist();

            for (OrderPosition pos : this.positions) {
                pos.persistOrUpdate();
            }

            return this;
        }

        return null;
    }

}
