package com.taller.bibliotecas.projections.classBased;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenusAsigNoAsig {
        private Long id_menu;
        private String nombre;
        private Long estado;
        private Long asig;
}
