package com.taller.bibliotecas.projections.classBased;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcesosAsigNoAsig {
    private Long id_proceso;
    private String nombre;
    private Long estado;
    private Long asig;
}
