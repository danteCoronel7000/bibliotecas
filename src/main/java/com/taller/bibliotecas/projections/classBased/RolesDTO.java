package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RolesDTO {
    private Long id_rol;
    private String nombre;
    private Long estado;
}
