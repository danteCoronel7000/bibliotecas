package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Image;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.projections.interfaceBased.closed.PersonasClosedView;
import com.taller.bibliotecas.projections.interfaceBased.open.PersonasOpenView;
import com.taller.bibliotecas.repository.PersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Service
public class PersonasServiceImpl implements PersonasService{


    // de aqui se borro en autowired
    private final PersonasRepository personasRepository;
    public PersonasServiceImpl(PersonasRepository personasRepository) {
        this.personasRepository = personasRepository;
    }
    //private final ImageService imageService;
/*
    public PersonasServiceImpl(PersonasRepository personasRepository, ImageService imageService) {
        this.personasRepository = personasRepository;
        this.imageService = imageService;
    }
*/

    @Override
    public List<Personas> findAll() {
        return personasRepository.findAll();
    }


    @Override
    public List<PersonasClosedView> findBy() {
        return personasRepository.findBy();
    }

    @Override
    public List<PersonasOpenView> findAllPersonas() {
        return personasRepository.findAllPersonas();
    }

    @Override
    public Personas save(Personas persona) {
        return personasRepository.save(persona);
    }

    @Override
    public Optional<Personas> findById_persona(Long id_persona) {
        return personasRepository.findById(id_persona);
    }

    @Override
    public Personas actualizarEstado(Long id_persona) {
        Personas persona = personasRepository.findById(id_persona)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_persona));

        // Actualizar el estado de la persona
        persona.setEstado(0L);

        // Guardar los cambios en la base de datos
        return personasRepository.save(persona);
    }

    @Override
    public Personas habilitarPersona(Long id_persona) {
        Personas persona = personasRepository.findById(id_persona)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id_persona));

        // Actualizar el estado de la persona
        persona.setEstado(1L);

        // Guardar los cambios en la base de datos
        return personasRepository.save(persona);
    }


    //metodos crud para las fotos de perfil de los usuarios;
    /*
    @Override
    public Personas savePersona(Personas personas, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()){
            Image image = imageService.uploadImage(file);
            personas.setImage(image);
        }
        return personasRepository.save(personas);
    }

    @Override
    public Personas updatePersona(Personas personas) {
        return personasRepository.save(personas);
    }

    @Override
    public List<Personas> getPersona() {
        return personasRepository.findAll();
    }

    @Override
    public Optional<Personas> getPersonaById(Long id) {
        return personasRepository.findById(id);
    }

    @Override
    public void deletePersona(Personas personas) throws IOException {
        if (personas.getImage() != null) {
            imageService.deleteImage(personas.getImage());
        }
        personasRepository.deleteById(personas.getId_persona());
    }

    @Override
    public Personas updatePersonaImage(MultipartFile file, Personas personas) throws IOException {
        if (personas.getImage() != null) {
            imageService.deleteImage(personas.getImage());
        }
        Image newImage = imageService.uploadImage(file);
        personas.setImage(newImage);
        return personasRepository.save(personas);
    }
    */

}
