package ru.clothingstore.model.good;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@Table(name = "Category")
@ToString(exclude="goods")
@EqualsAndHashCode(exclude={"id", "goods"})
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Size(min = 3, max = 64)
    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private Set<Good> goods;

}
