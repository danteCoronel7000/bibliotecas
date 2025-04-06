package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.entitys.Usuarios;

import java.util.Optional;

public interface AreasService {
    Optional<Areas> findById_area(Long id_area);

    Areas save(Areas area);

    Areas deleteArea(Long id_area);

    Areas habilitarArea(Long id_area);
}
