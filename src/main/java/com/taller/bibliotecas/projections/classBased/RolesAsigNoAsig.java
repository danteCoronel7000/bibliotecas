package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesAsigNoAsig {
    private Long id_rol;
    private String nombre;
    private Long estado;
    private Long asig;
}
