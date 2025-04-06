package com.taller.bibliotecas.entitys;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
public class DetPrestamoPK implements Serializable {
    private Long id_ejemplar;
    private Long id_mprestamo;
    private Long estado;
}
