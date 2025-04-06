package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Textos;
import com.taller.bibliotecas.repository.TextosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TextosServiceImpl implements TextosService {

    @Autowired
    TextosRepository textosRepository;

    @Override
    public Optional<Textos> findById_texto(Long id_texto) {
        return textosRepository.findById(id_texto);
    }

    @Override
    public Textos save(Textos texto) {
        return textosRepository.save(texto);
    }

    @Override
    public Textos deleteTextos(Long id_texto) {
        Textos texto = textosRepository.findById(id_texto)
                .orElseThrow(() -> new RuntimeException("Texto no encontrado con id: " + id_texto));
        // Actualizar el estado del texto
        texto.setEstado(0L);

        // Guardar los cambios en la base de datos
        return textosRepository.save(texto);
    }

    @Override
    public Textos habilitarTexto(Long id_texto) {
        Textos texto = textosRepository.findById(id_texto)
                .orElseThrow(() -> new RuntimeException("Texto no encontrado con id: " + id_texto));

        // Actualizar el estado del texto
        texto.setEstado(1L);

        // Guardar los cambios en la base de datos
        return textosRepository.save(texto);
    }

}
