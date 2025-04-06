package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.repository.AutoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutoresServiceImpl implements AutoresService{
    @Autowired
    AutoresRepository autoresRepository;

    @Override
    public Optional<Autores> findById_autor(Long id_autor) {
        return autoresRepository.findById(id_autor);
    }

    @Override
    public Autores save(Autores autor) {
        return autoresRepository.save(autor);
    }

    @Override
    public Autores deleteAutores(Long id_autor) {
        Autores autor = autoresRepository.findById(id_autor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id_autor));

        // Actualizar el estado del autor
        autor.setEstado(0L);

        // Guardar los cambios en la base de datos
        return autoresRepository.save(autor);
    }

    @Override
    public Autores habilitarAutor(Long id_autor) {
        Autores autor = autoresRepository.findById(id_autor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id_autor));

        // Actualizar el estado del autor
        autor.setEstado(1L);

        // Guardar los cambios en la base de datos
        return autoresRepository.save(autor);
    }

}
