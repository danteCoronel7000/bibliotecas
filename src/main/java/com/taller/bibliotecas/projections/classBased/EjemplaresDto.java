package com.taller.bibliotecas.projections.classBased;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjemplaresDto {
    private Long id_ejemplar;
    private Long codinv;
    private Long disponible;
    private Long estado;
}
