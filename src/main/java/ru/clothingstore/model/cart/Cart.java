package ru.clothingstore.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.clothingstore.model.user.User;
import ru.clothingstore.model.product.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "cart")
@EqualsAndHashCode(exclude = {"id", "products", "user"})
@ToString(exclude = {"user"})
@NoArgsConstructor
public class Cart {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private Set<Product> products;

    @Column(name = "sum")
    private Double sum = 0d;

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product getProduct(int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }
}
