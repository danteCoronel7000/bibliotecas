package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.projections.interfaceBased.closed.PersonasClosedView;
import com.taller.bibliotecas.projections.interfaceBased.open.PersonasOpenView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PersonasService {
    List<Personas> findAll();

    //closed view interface based
    List<PersonasClosedView> findBy();
    //open view interface based
    List<PersonasOpenView> findAllPersonas();

    Personas save(Personas persona);

    Optional<Personas> findById_persona(Long id_persona);

    Personas actualizarEstado(Long id_persona);

    Personas habilitarPersona(Long id_persona);

    //metodos para la foto del usuario
    /*
    Personas savePersona(Personas personas, MultipartFile file) throws IOException;
    Personas updatePersona(Personas personas);
    List<Personas> getPersona();
    Optional<Personas> getPersonaById(Long id);
    void deletePersona(Personas personas) throws IOException;
    Personas updatePersonaImage(MultipartFile file, Personas personas) throws IOException;
     */
}
