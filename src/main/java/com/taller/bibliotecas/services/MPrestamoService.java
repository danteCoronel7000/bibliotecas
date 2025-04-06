package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.MPrestamo;
import com.taller.bibliotecas.projections.classBased.CrearPrestamoDTO;

import java.util.List;

public interface MPrestamoService {
    MPrestamo createPrestamo(CrearPrestamoDTO dto);
    List<MPrestamo> getPrestamosByIdPersona(Long id_persona);
}
