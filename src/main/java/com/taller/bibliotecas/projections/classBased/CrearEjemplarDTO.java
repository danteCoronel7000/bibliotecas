package com.taller.bibliotecas.projections.classBased;

import com.taller.bibliotecas.entitys.Ejemplares;
import lombok.Data;

@Data
public class CrearEjemplarDTO {
    private Ejemplares ejemplar;
    private Long id_texto;
    private Long id_usuario;
    private Long codinv;
}
