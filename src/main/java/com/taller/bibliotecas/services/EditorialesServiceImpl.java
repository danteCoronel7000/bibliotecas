package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Editoriales;
import com.taller.bibliotecas.repository.EditorialesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorialesServiceImpl implements EditorialesService{
    @Autowired
    EditorialesRepository editorialesRepository;

    @Override
    public Optional<Editoriales> findById_editorial(Long id_editorial) {
        return editorialesRepository.findById(id_editorial);
    }

    @Override
    public Editoriales save(Editoriales editoriales) {
        return editorialesRepository.save(editoriales);
    }

    @Override
    public Editoriales deleteEditorial(Long id_editorial) {
        Editoriales editoriales = editorialesRepository.findById(id_editorial)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_editorial));

        // Actualizar el estado de la persona
        editoriales.setEstado(0L);

        // Guardar los cambios en la base de datos
        return editorialesRepository.save(editoriales);
    }

    @Override
    public Editoriales habilitarEditorial(Long id_editorial) {
        Editoriales editorial = editorialesRepository.findById(id_editorial)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_editorial));

        // Actualizar el estado de la persona
        editorial.setEstado(1L);

        // Guardar los cambios en la base de datos
        return editorialesRepository.save(editorial);
    }
}
