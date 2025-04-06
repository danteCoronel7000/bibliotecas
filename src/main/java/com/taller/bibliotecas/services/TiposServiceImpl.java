package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Editoriales;
import com.taller.bibliotecas.entitys.Tipos;
import com.taller.bibliotecas.repository.TiposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TiposServiceImpl implements TiposService{

    @Autowired
    TiposRepository tiposRepository;

    @Override
    public Optional<Tipos> findById_tipo(Long id_tipo) {
        return tiposRepository.findById(id_tipo);
    }

    @Override
    public Tipos save(Tipos tipo) {
        return tiposRepository.save(tipo);
    }

    @Override
    public Tipos deleteTipos(Long id_tipo) {
        Tipos tipos = tiposRepository.findById(id_tipo)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_tipo));

        // Actualizar el estado de la persona
        tipos.setEstado(0L);

        // Guardar los cambios en la base de datos
        return tiposRepository.save(tipos);
    }

    @Override
    public Tipos habilitarTipo(Long id_tipo) {
        Tipos tipos = tiposRepository.findById(id_tipo)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_tipo));

        // Actualizar el estado del tipo
        tipos.setEstado(1L);

        // Guardar los cambios en la base de datos
        return tiposRepository.save(tipos);
    }
}
