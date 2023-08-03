package ru.clothingstore.model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.model.order.Order;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Products")
@EqualsAndHashCode(exclude = {"id", "cart"})
@ToString(exclude = {"cart"})
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Product name should not be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters ")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "Price should be greater than 0")
    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "good_id")
    private Good good;

    @Column(name = "count")
    private Integer count;

    // TODO Убрать
    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    @Column(name = "added_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;

    @Transient
    private boolean expired;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
