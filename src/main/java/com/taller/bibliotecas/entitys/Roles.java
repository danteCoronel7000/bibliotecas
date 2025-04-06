package com.taller.bibliotecas.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "roles"
)
public class Roles {
    @Id
    @SequenceGenerator(
            name = "roles_secuence",
            sequenceName = "roles_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "roles_secuence"
    )
    @Column(name = "id_rol")
    private Long id_rol;
    private String nombre;
    private Long estado;


    //relacion manyToMany con la entidad menus
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "roles_menus",
            joinColumns = @JoinColumn(
                    name = "id_rol",
                    referencedColumnName = "id_rol"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_menu",
                    referencedColumnName = "id_menu"
            )
    )
    private List<Menus> menusList;
}
