package com.taller.bibliotecas.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
public class Personas {
    @Id
    @SequenceGenerator(
            name = "personas_secuence",
            sequenceName = "personas_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "personas_secuence"
    )
            @Column(name = "id_persona")
    Long id_persona;
    String nombre;
    String ap;
    String am;
    String genero;
    @Column(name = "estado", columnDefinition = "BIGINT DEFAULT 1")
    Long estado;
    String tipo_per;
    String foto;
    @OneToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "id_dato",
            referencedColumnName = "id_dato"
    )
    @JsonBackReference
    Datos datos;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "id_persona",
            referencedColumnName = "id_persona"
    )
    private List<Telefonos> telefonos;
}
