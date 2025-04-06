package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EjemplarPrestamoById_prestamoDTO {
    private Long idEjemplar;
    private Long codinv;
    private Long estado; // Estado de la relación many-to-many
    private String titulo; // Título desde la tabla texto

    // Constructor
    public EjemplarPrestamoById_prestamoDTO(Long idEjemplar, Long codinv, Long estado, String titulo) {
        this.idEjemplar = idEjemplar;
        this.codinv = codinv;
        this.estado = estado;
        this.titulo = titulo;
    }
}
