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
@Entity
@Table(name = "tipos")
public class Tipos {
    @Id
    @SequenceGenerator(
            name = "tipos_secuence",
            sequenceName = "tipos_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tipos_secuence"
    )
    @Column(name = "id_tipo")
    Long id_tipo;
    @Column(nullable = false)
    String nombre;
    @Column(name = "estado")
    Long estado; // Asigna un valor por defecto en la entidad
    @Column(nullable = false)
    Long sw;
}
