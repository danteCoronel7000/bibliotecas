package com.taller.bibliotecas.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "telefonos")
public class Telefonos {
    @Id
    @SequenceGenerator(
            name = "telefonos_secuence",
            sequenceName = "telefonos_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "telefonos_secuence"
    )
    @Column(name = "id_telefono")
    private Long id_telefono;
    @Column(name = "numero")
    private Long numero;
    @Column(name = "id_persona")
    private Long id_persona;
}
