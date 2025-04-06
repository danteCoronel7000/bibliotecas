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
@Table(name = "autores")
public class Autores {
    @Id
    @SequenceGenerator(
            name = "autores_secuence",
            sequenceName = "autores_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "autores_secuence"
    )
    @Column(name = "id_autor")
    Long id_autor;
    @Column(nullable = false)
    String nombre;
    @Column(nullable = false)
    String ap;
    @Column(nullable = false)
    String am;
    @Column(nullable = false)
    String genero;
    @Column(nullable = false)
    Long estado;
}
