package com.taller.bibliotecas.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Datos")
public class Datos {
    @Id
    @SequenceGenerator(
            name = "datos_secuence",
            sequenceName = "datos_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "datos_secuence"
    )
    @Column(name = "id_dato")
    private Long id_dato;
    @Column(name = "ci")
    private String ci;

    @OneToOne(
            mappedBy = "datos",
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    Personas persona;
}
