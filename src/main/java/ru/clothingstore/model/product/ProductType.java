package ru.clothingstore.model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clothingstore.model.product.Product;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
@Table(name = "Product_types")
@EqualsAndHashCode(exclude = {"id", "products"})
@ToString(exclude = "products")
@NoArgsConstructor
public class ProductType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(name = "type_name")
    private String name;

    @OneToMany(mappedBy = "productType")
    private List<Product> products;

    public ProductType(String name) {
        this.name = name;
    }
}