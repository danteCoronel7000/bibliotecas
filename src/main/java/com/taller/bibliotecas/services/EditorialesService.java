package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Editoriales;

import java.util.Optional;

public interface EditorialesService {
    Optional<Editoriales> findById_editorial(Long id_editorial);

    Editoriales save(Editoriales editoriales);

    Editoriales deleteEditorial(Long id_editorial);

    Editoriales habilitarEditorial(Long id_editorial);
}
