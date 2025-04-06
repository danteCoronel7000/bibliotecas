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
@Table(name = "areas")
public class Areas {
    @Id
    @SequenceGenerator(
            name = "areas_secuence",
            sequenceName = "areas_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "areas_secuence"
    )
    @Column(name = "id_area")
    Long id_area;
    @Column(nullable = false)
    String nombre;
    @Column(nullable = false)
    Long estado;
}
