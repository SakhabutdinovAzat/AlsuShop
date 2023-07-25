package ru.clothingstore.model.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Person")
@EqualsAndHashCode(exclude = {"id", "role", "cart", "orders"})
@ToString(exclude = "orders")
@NoArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 30, message = "Username should be between 2 and 30")
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active = true;

    //@NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "firstname")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]\\w+", message = "Incorrect first name")
    private String firstName;

    @Column(name = "lastname")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 50 characters")
    @Pattern(regexp = "[A-Z]\\w+", message = "Incorrect last name")
    private String lastName;

    @Column(name = "age")
    @Min(value = 0, message = "Age should be more than 0")
    private int age;

    @Column(name = "address")
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Incorrect address")
    private String address;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Enumerated(EnumType.ORDINAL)
    private Reputation reputation;

    @OneToMany(mappedBy = "owner")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REFRESH})
    private List<Order> orders;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    public Person(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void addOrder(Order order) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }

        this.orders.add(order);
        order.setOwner(this);
    }
}
