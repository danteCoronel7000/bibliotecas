package com.taller.bibliotecas.projections.classBased;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CrearPrestamoDTO {


        private Long id_mprestamo;
        private String fecha;
        private String fechaini;
        private String fechafin;
        private Long tipopres;
        private Long estado;
        private Long id_dato;  // id_dato en "realiza"
        private Long id_usuario;  // id_usuario en "presta"
        private List<EjemplarDtoPPrestamos> listEjemplares;  // Ejemplares asociados

        // Getters y Setters

}
