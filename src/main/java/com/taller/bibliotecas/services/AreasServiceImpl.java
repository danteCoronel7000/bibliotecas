package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.repository.AreasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AreasServiceImpl implements AreasService{
    @Autowired
    AreasRepository areasRepository;

    @Override
    public Optional<Areas> findById_area(Long id_area) {
        return areasRepository.findById(id_area);
    }

    @Override
    public Areas save(Areas area) {
        return areasRepository.save(area);
    }

    @Override
    public Areas deleteArea(Long id_area) {
        Areas area = areasRepository.findById(id_area)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_area));

        // Actualizar el estado de la persona
        area.setEstado(0L);

        // Guardar los cambios en la base de datos
        return areasRepository.save(area);
    }

    @Override
    public Areas habilitarArea(Long id_area) {
        Areas area = areasRepository.findById(id_area)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_area));

        // Actualizar el estado de la persona
        area.setEstado(1L);

        // Guardar los cambios en la base de datos
        return areasRepository.save(area);
    }
}
