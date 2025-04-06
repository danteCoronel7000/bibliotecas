package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Autores;

import java.util.Optional;

public interface AutoresService {
    Optional<Autores> findById_autor(Long id_autor);

    Autores save(Autores autor);

    Autores deleteAutores(Long id_autor);

    Autores habilitarAutor(Long id_autor);

}
