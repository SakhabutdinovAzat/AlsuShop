package ru.clothingstore.model.good;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clothingstore.model.product.Product;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Good")
@EqualsAndHashCode(exclude = {"id", "category", "cartProducts"})
@ToString(exclude = "cartProducts")
@NoArgsConstructor
public class Good {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "title")
    private String title;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active = true;

    @NotNull
    @Size(min = 3)
    @Column(name = "description")
    private String description;

    @Column(name = "small_image_link")
    private String smallImageLink;

    @Min(value = 0, message = "Age should be more than 0")
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    @Digits(integer = 9, fraction = 2)
    private Double price;

    @Column(name = "added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date added;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "good", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> cartProducts = new HashSet<>();
}
