package com.taller.bibliotecas.entitys;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "mdevolucion")
public class MDevolucion {
    @Id
    @SequenceGenerator(
            name = "mdevolucion_secuence",
            sequenceName = "mdevolucion_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mdevolucion_secuence"
    )
    @Column(name = "id_mdevolucion")
    Long id_mdevolucion;
    @Column(name = "fecha")
    String fecha;
    @Column(name = "estado")
    Long estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuarios reserva;

    @ManyToOne
    @JoinColumn(name = "id_mprestamo", referencedColumnName = "id_mprestamo")
    private MPrestamo mprestamo;

    //relacion manyToMany con la entidad roles
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "det_devolucion",
            joinColumns = @JoinColumn(
                    name = "id_mdevolucion",
                    referencedColumnName = "id_mdevolucion"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_ejemplar",
                    referencedColumnName = "id_ejemplar"
            )
    )
    private List<Ejemplares> ejemplaresList;
}
