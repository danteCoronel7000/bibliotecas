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
@Table(name = "editoriales")
public class Editoriales {
    @Id
    @SequenceGenerator(
            name = "editoriales_secuence",
            sequenceName = "editoriales_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "editoriales_secuence"
    )
    @Column(name = "id_editorial")
    Long id_editorial;
    @Column(nullable = false)
    String nombre;
    @Column(name = "estado")
    Long estado; // Asigna un valor por defecto en la entidad
}
