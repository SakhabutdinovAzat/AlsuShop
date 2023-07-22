package ru.clothingstore.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@Table(name = "role")
@ToString(exclude="people")
@EqualsAndHashCode(exclude={"id", "people"})
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 32)
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<Person> people;

}
