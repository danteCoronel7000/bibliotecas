package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.entitys.Textos;

import java.util.Optional;

public interface TextosService {

    Optional<Textos> findById_texto(Long id_texto);

    Textos save(Textos texto);

    Textos deleteTextos(Long id_texto);

    Textos habilitarTexto(Long id_texto);

}
