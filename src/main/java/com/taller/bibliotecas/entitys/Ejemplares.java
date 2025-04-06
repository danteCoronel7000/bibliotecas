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
@Table(name = "ejemplares")
public class Ejemplares {
    @Id
    @SequenceGenerator(
            name = "ejemplares_secuence",
            sequenceName = "ejemplares_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ejemplares_secuence"
    )
    @Column(name = "id_ejemplar")
    Long id_ejemplar;
    @Column(name = "codinv")
    Long codinv;
    @Column(nullable = false)
    Long disponible;
    @Column(name = "estado")
    Long estado;

    @ManyToOne
    @JoinColumn(name = "id_texto", referencedColumnName = "id_texto")
    private Textos texto;

    @ManyToOne
    @JoinColumn(name = "id_usuario_anula", referencedColumnName = "id_usuario")
    private Usuarios anula;

    @ManyToOne
    @JoinColumn(name = "id_usuario_resgistra", referencedColumnName = "id_usuario")
    private Usuarios registra;


}
