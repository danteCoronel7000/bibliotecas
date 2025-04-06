package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Image;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.projections.interfaceBased.closed.PersonasClosedView;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsuariosAndPersonas;
import com.taller.bibliotecas.projections.interfaceBased.open.PersonasOpenView;
//import com.taller.bibliotecas.services.ImageService;
import com.taller.bibliotecas.repository.PersonasRepository;
import com.taller.bibliotecas.services.PersonasService;
import com.taller.bibliotecas.services.PersonasServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonasController {
    @Autowired
    PersonasService personasService;
    @Autowired
    PersonasServiceImpl personasServiceImpl;
    @Autowired
    PersonasRepository personasRepository;
    /*
    @Autowired
    ImageService imageService;
     */

    @GetMapping(value = "all")
    public List<Personas> findAll(){
        return personasService.findAll();
    }


    //closed view interface based
    @GetMapping(value = "allClosedView")
    public List<PersonasClosedView> findAllClosedView(){
        return personasService.findBy();
    }

    //open view interface based
    @GetMapping(value = "allOpenView")
    public List<PersonasOpenView> findAllOpenView(){
        return personasService.findAllPersonas();
    }

    @PostMapping("/estado")
    public ResponseEntity<Personas> actualizarEstado(@RequestBody Map<String, Long> payload) {
        Long idPersona = payload.get("id_persona"); // Extrae id_persona del cuerpo de la solicitud
        System.out.println(idPersona);
        Personas personaActualizada = personasService.actualizarEstado(idPersona);
        return ResponseEntity.ok(personaActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Personas> habilitarPersona(@RequestBody Map<String, Long> payload) {
        Long idPersona = payload.get("id_persona"); // Extrae id_persona del cuerpo de la solicitud
        System.out.println(idPersona);
        Personas personaActualizada = personasService.habilitarPersona(idPersona);
        return ResponseEntity.ok(personaActualizada);
    }

    //usuarios and personas
    @GetMapping(value = "/{id}")
    public Optional<Personas> usuariosById(@PathVariable Long id){
        return personasService.findById_persona(id);
    }

    //endPoint para buscar personas por nombre y apellido
    @GetMapping("/searchPersonasNative/{param}")
    public List<Personas> searchPersonas(@PathVariable("param") String xfiltro) {
        if (xfiltro == null || xfiltro.isEmpty()) {
            // Retorna lista vacía si el filtro está vacío o es null
            return new ArrayList<>();
        }

        // Divide el filtro en palabras
        String[] palabras = xfiltro.split(" ");
        String filtroNombre = palabras.length > 0 ? palabras[0] : "";
        String filtroAp = palabras.length > 1 ? palabras[1] : "";
        String filtroAm = palabras.length > 2 ? palabras[2] : "";

        return personasRepository.searchPersonasNative(filtroNombre, filtroAp, filtroAm);
    }




/*
    @PostMapping("/upload")
    public ResponseEntity<Personas> uploadPerson(@RequestBody Personas persona) {
        Personas savedPerson = personasService.savePerson(persona);
        return ResponseEntity.ok(savedPerson);
    }
*/

    //funciones crud para las imagenes de las personas..............................................................................
/*
    @PostMapping(value = "savePersona")
    public ResponseEntity<Personas> savePersona(@RequestPart("persona") Personas personas, @RequestPart("file") MultipartFile file) {
        try {
            Personas savedPersona = personasServiceImpl.savePersona(personas, file);
            return new ResponseEntity<>(savedPersona, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
 */

    /*metodo para crear una persona y subir su imagen a cloudInary --nofunciona
    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Personas> createPersona(
            @RequestParam("persona") String personaJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Convertir el JSON de la persona en un objeto Personas
        ObjectMapper objectMapper = new ObjectMapper();
        Personas persona = objectMapper.readValue(personaJson, Personas.class);

        // Si se proporciona un archivo, subirlo
        if (file != null && !file.isEmpty()) {
            Image uploadedImage = imageService.uploadImage(file);
            persona.setFoto(uploadedImage.getImageUrl()); // Asignar la URL de la imagen a la persona
        }

        // Guardar la persona en la base de datos
        Personas savedPersona = personasService.save(persona);

        return new ResponseEntity<>(savedPersona, HttpStatus.CREATED);
    }
     */


/*
    @PutMapping("/{id}/image")
    public ResponseEntity<Personas> updatePersonasImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
        Optional<Personas> personas = personasServiceImpl.getPersonaById(id);
        if (personas.isPresent()) {
            Personas updatedPersona = personasServiceImpl.updatePersonaImage(file, personas.get());
            return new ResponseEntity<>(updatedPersona, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<Personas> updatePersonas(@RequestBody Personas personas){
        try {
            Personas savedPersona = personasServiceImpl.updatePersona(personas);
            return new ResponseEntity<>(savedPersona, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Personas>> getAllPersonas(){
        return new ResponseEntity<>(personasServiceImpl.getPersona(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personas> getPersonaById(@PathVariable Long id) {
        Optional<Personas> persona = personasServiceImpl.getPersonaById(id);
        return persona.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) throws IOException {
        Optional<Personas> persona = personasServiceImpl.getPersonaById(id);
        if (persona.isPresent()){
            personasServiceImpl.deletePersona(persona.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
     */

}
