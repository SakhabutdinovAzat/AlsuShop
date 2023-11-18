package ru.clothingstore.model.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "Orders")
@EqualsAndHashCode(exclude = {"id", "owner"})
@ToString(exclude = "cart")
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Transient
    private boolean expired;

    public Order(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
