package com.taller.bibliotecas.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "procesos"
)
public class Procesos {
    @Id
    @SequenceGenerator(
            name = "procesos_secuence",
            sequenceName = "procesos_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "procesos_secuence"
    )
    @Column(name = "id_proceso")
    private Long id_proceso;
    private String nombre;
    private String enlace;
    private Long estado;
}
