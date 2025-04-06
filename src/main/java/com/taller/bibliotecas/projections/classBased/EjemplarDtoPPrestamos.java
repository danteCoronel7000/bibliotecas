package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class EjemplarDtoPPrestamos {
        private Long id_ejemplar;
        private Long codinv;
        private String titulo;
        private Long disponible;
        private Long estado;
}