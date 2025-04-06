package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetPrestamoDTO {
    private Long codinv;
    private String titejemplar;
    private Long estado;
}
