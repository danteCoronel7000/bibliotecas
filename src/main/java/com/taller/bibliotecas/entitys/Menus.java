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
        name = "menus"
)
public class Menus {
    @Id
    @SequenceGenerator(
            name = "menus_secuence",
            sequenceName = "menus_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "menus_secuence"
    )
    @Column(name = "id_menu")
    private Long id_menu;
    private String nombre;
    private Long estado;


    //relacion manyToMany con la entidad procesos
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER// con EAGER traemos toda la informacion de la tabla tambien la informacion de la tabla con la que esta relacionada
            //fetch = FetchType.LAZY// con LAZY solo se trae iformacion de la tabla y no asi de sus relacion qeu tiene con otra tabla.... tambien tenemos que incluir @ToString(exclude = "procesosList" para evitar error al momento de compilar ya que lombok genera automaticatemente el metodo toString
    )
    @JoinTable(
            name = "menus_procesos",
            joinColumns = @JoinColumn(
                    name = "id_menu",
                    referencedColumnName = "id_menu"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_proceso",
                    referencedColumnName = "id_proceso"
            )
    )
    private List<Procesos> procesosList;
}
