package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Tipos;

import java.util.Optional;

public interface TiposService {
    Optional<Tipos> findById_tipo(Long id_tipo);

    Tipos save(Tipos tipo);

    Tipos deleteTipos(Long id_tipo);

    Tipos habilitarTipo(Long id_tipo);
}
