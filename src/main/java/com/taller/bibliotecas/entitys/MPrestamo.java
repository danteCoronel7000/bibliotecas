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
@Table(name = "mprestamo")
public class MPrestamo {
    @Id
    @SequenceGenerator(
            name = "mprestamo_secuence",
            sequenceName = "mprestamo_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mprestamo_secuence"
    )
    @Column(name = "id_mprestamo")
    Long id_mprestamo;
    @Column(nullable = false)
    String fecha;
    @Column(nullable = false)
    String fechaini;
    @Column(nullable = false)
    String fechafin;
    @Column(nullable = false)
    Long tipopres;
    @Column(nullable = false)
    Long estado;

    @ManyToOne
    @JoinColumn(name = "id_dato", referencedColumnName = "id_dato")
    private Datos realiza;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuarios presta;

}
