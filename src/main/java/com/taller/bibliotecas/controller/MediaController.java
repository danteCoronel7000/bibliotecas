package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.repository.*;
import com.taller.bibliotecas.services.PersonasService;
import com.taller.bibliotecas.services.StorageService;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
@AllArgsConstructor
public class MediaController {
    private final StorageService storageService;
    private final HttpServletRequest request;
    @Autowired
    TelefonosRepository telefonosRepository;
    @Autowired
    DatosRepository datosRepository;
    @Autowired
    AreasRepository areasRepository;
    @Autowired
    AutoresRepository autoresRepository;
    @Autowired
    EditorialesRepository editorialesRepository;
    @Autowired
    TextosRepository textosRepository;
    @Autowired
    TiposRepository tiposRepository;
    @Autowired
    TipoTexRepository tipoTexRepository;

    private final PersonasService personasService;

    @PostMapping("/createtext")
    public ResponseEntity<Textos> crearTexto(
            @RequestParam("texto") String textoJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        System.out.println("JSON recibido: " + textoJson);
        Textos texto = new ObjectMapper().readValue(textoJson, Textos.class);

        // Relacionar el área si se envía
        if (texto.getArea() != null && texto.getArea().getId_area() != null) {
            Areas area = areasRepository.findById(texto.getArea().getId_area())
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
            texto.setArea(area);
        }

        // Relacionar la editorial si se envía
        if (texto.getEditorial() != null && texto.getEditorial().getId_editorial() != null) {
            Editoriales editorial = editorialesRepository.findById(texto.getEditorial().getId_editorial())
                    .orElseThrow(() -> new RuntimeException("Editorial no encontrada"));
            texto.setEditorial(editorial);
        }

        // Relacionar los autores si se envían
        if (texto.getAutoresList() != null && !texto.getAutoresList().isEmpty()) {
            List<Autores> autores = new ArrayList<>();
            for (Autores autor : texto.getAutoresList()) {
                Autores fetchedAutor = autoresRepository.findById(autor.getId_autor())
                        .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
                autores.add(fetchedAutor);
            }
            texto.setAutoresList(autores);
        }
        // Manejar el archivo si existe
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            String path = storageService.store(file);
            String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/media/")
                    .path(path)
                    .toUriString();
            texto.setUrl(url);
            fileName = file.getOriginalFilename();
        }

        // Guardar el texto en la base de datos
        Textos savedTexto = textosRepository.save(texto);

        // Determinar el tipo (digital o impreso)
        Long tipodioimp = (file == null || file.isEmpty()) ? 16L : 15L;
        Tipos tipoDigital = tiposRepository.findById(tipodioimp)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        // Configurar y guardar el objeto TipoTex
        TipoTex tipoTex = new TipoTex();
        TipoTextPK tipoTextPK = new TipoTextPK();
        tipoTextPK.setId_texto(savedTexto.getId_texto());
        tipoTextPK.setId_tipo(tipoDigital.getId_tipo());
        tipoTextPK.setDocum(fileName != null ? fileName : "No disponible");  // Valor por defecto si file es nulo

        tipoTex.setIdTipotex(tipoTextPK);
        tipoTex.setTexto(savedTexto);
        tipoTex.setTipo(tipoDigital);

        tipoTexRepository.save(tipoTex);

        return ResponseEntity.ok(savedTexto);
    }

    @PutMapping("/updatetext/{id}")
    public ResponseEntity<Textos> actualizarTexto(
            @PathVariable Long id,
            @RequestParam("texto") String textoJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        System.out.println("JSON recibido: " + textoJson);
        // Buscar el texto existente por ID
        Textos existingTexto = textosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Texto no encontrado"));

        // Deserializar el JSON recibido
        Textos texto = new ObjectMapper().readValue(textoJson, Textos.class);

        // Actualizar las propiedades de existingTexto con los nuevos datos
        existingTexto.setTitulo(texto.getTitulo());
        existingTexto.setResumen(texto.getResumen());
        existingTexto.setEdicion(texto.getEdicion());
        existingTexto.setFechapub(texto.getFechapub());
        existingTexto.setIsbn(texto.getIsbn());
        existingTexto.setEstado(texto.getEstado());

        // Relacionar el área si se envía
        if (texto.getArea() != null && texto.getArea().getId_area() != null) {
            Areas area = areasRepository.findById(texto.getArea().getId_area())
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
            existingTexto.setArea(area);
        }

        // Relacionar la editorial si se envía
        if (texto.getEditorial() != null && texto.getEditorial().getId_editorial() != null) {
            Editoriales editorial = editorialesRepository.findById(texto.getEditorial().getId_editorial())
                    .orElseThrow(() -> new RuntimeException("Editorial no encontrada"));
            existingTexto.setEditorial(editorial);
        }

        // Relacionar los autores si se envían
        if (texto.getAutoresList() != null && !texto.getAutoresList().isEmpty()) {
            List<Autores> autores = new ArrayList<>();
            for (Autores autor : texto.getAutoresList()) {
                Autores fetchedAutor = autoresRepository.findById(autor.getId_autor())
                        .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
                autores.add(fetchedAutor);
            }
            existingTexto.setAutoresList(autores);
        }

        // Manejar el archivo si existe
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            String path = storageService.store(file);
            String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/media/")
                    .path(path)
                    .toUriString();
            existingTexto.setUrl(url);
            fileName = file.getOriginalFilename();
        }

        // Guardar el texto actualizado en la base de datos
        Textos savedTexto = textosRepository.save(existingTexto);

        // Actualizar el tipo (digital o impreso) si cambia el archivo
        Long tipodioimp = (file == null || file.isEmpty()) ? 16L : 15L;
        Tipos tipoDigital = tiposRepository.findById(tipodioimp)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        // Configurar y guardar el objeto TipoTex actualizado
        TipoTex tipoTex = new TipoTex();
        TipoTextPK tipoTextPK = new TipoTextPK();
        tipoTextPK.setId_texto(savedTexto.getId_texto());
        tipoTextPK.setId_tipo(tipoDigital.getId_tipo());
        tipoTextPK.setDocum(fileName != null ? fileName : "No disponible");

        tipoTex.setIdTipotex(tipoTextPK);
        tipoTex.setTexto(savedTexto);
        tipoTex.setTipo(tipoDigital);

        tipoTexRepository.save(tipoTex);

        return ResponseEntity.ok(savedTexto);
    }


    @PostMapping("/crear")
    public ResponseEntity<Personas> crearPersona(
            @RequestParam("persona") String personaJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Imprimir el JSON de la persona en la consola
        System.out.println("JSON recibido: " + personaJson);
        // Convertir el JSON de persona a la entidad Personas
        Personas persona = new ObjectMapper().readValue(personaJson, Personas.class);

        // Guardar Datos
        Datos datos = persona.getDatos();
        if (datos != null) {
            Datos savedDatos = datosRepository.save(datos);
            persona.setDatos(savedDatos);  // Asociar Datos a la Persona
        }

        // Guardar Telefonos
        List<Telefonos> telefonos = persona.getTelefonos();
        if (telefonos != null) {
            for (Telefonos telefono : telefonos) {
                telefono.setId_persona(persona.getId_persona());
            }
            List<Telefonos> savedTelefonos = telefonosRepository.saveAll(telefonos);
            persona.setTelefonos(savedTelefonos);  // Asociar los teléfonos a la Persona
        }

        // Verificar si hay un archivo de imagen
        if (file != null && !file.isEmpty()) {
            // Subir la imagen y obtener la URL
            String path = storageService.store(file);
            String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/media/")
                    .path(path)
                    .toUriString();

            // Establecer la URL de la imagen en el campo 'foto'
            persona.setFoto(url);
        }

        // Guardar la persona en la base de datos
        Personas savedPersona = personasService.save(persona);

        // Devolver la respuesta con la persona creada
        return ResponseEntity.ok(savedPersona);
    }


//metodo para actualizar la persona
    @PostMapping("/actualizar")
    public ResponseEntity<Personas> actualizarPersona(
            @RequestParam("persona") String personaJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Convertir el JSON de persona a la entidad Personas
        Personas persona = new ObjectMapper().readValue(personaJson, Personas.class);

        // Verificar si la persona ya existe en la base de datos
        if (persona.getId_persona() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Personas> existingPersonaOpt = personasService.findById_persona(persona.getId_persona());
        if (existingPersonaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra la persona, devolver 404
        }

        Personas existingPersona = existingPersonaOpt.get();

        // Actualizar los datos de la persona existente con los nuevos valores
        existingPersona.setNombre(persona.getNombre());
        existingPersona.setAp(persona.getAp());
        existingPersona.setAm(persona.getAm());
        existingPersona.setGenero(persona.getGenero());
        existingPersona.setEstado(persona.getEstado());
        existingPersona.setTipo_per(persona.getTipo_per());

        // Actualizar Datos
        Datos datos = persona.getDatos();
        if (datos != null) {
            datos.setId_dato(existingPersona.getDatos().getId_dato());  // Asegura el ID de datos
            Datos savedDatos = datosRepository.save(datos);
            existingPersona.setDatos(savedDatos);
        }

        // Actualizar o guardar teléfonos
        List<Telefonos> telefonos = persona.getTelefonos();
        if (telefonos != null) {
            telefonos.forEach(telefono -> telefono.setId_persona(existingPersona.getId_persona()));
            List<Telefonos> savedTelefonos = telefonosRepository.saveAll(telefonos);
            existingPersona.setTelefonos(savedTelefonos);
        }

        // Manejar la imagen si se sube un nuevo archivo
        if (file != null && !file.isEmpty()) {
            String path = storageService.store(file);
            String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/media/")
                    .path(path)
                    .toUriString();
            existingPersona.setFoto(url);
        }

        // Guardar la persona actualizada
        Personas updatedPersona = personasService.save(existingPersona);

        // Devolver la respuesta con la persona actualizada
        return ResponseEntity.ok(updatedPersona);
    }




    @PutMapping("/updatefoto")
    public ResponseEntity<Map<String, String>> updateFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("id_persona") Long idPersona) {

        // Guardar el archivo y obtener el path
        String path;
        try {
            path = storageService.store(multipartFile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo", e);
        }

        // Construir la URL completa del archivo
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/media/")
                .path(path)
                .toUriString();

        // Buscar la persona en la base de datos por ID
        Optional<Personas> optionalPersona = personasService.findById_persona(idPersona);
        if (optionalPersona.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }

        // Actualizar el campo 'foto' de la persona y guardar los cambios
        Personas persona = optionalPersona.get();
        persona.setFoto(url);
        personasService.save(persona);

        // Responder con la URL de la nueva foto
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id_persona") Long idPersona) {
        // Guardar el archivo y obtener el path
        String path = storageService.store(multipartFile);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/media/")
                .path(path)
                .toUriString();

        // Buscar la persona en la base de Datos
        Optional<Personas> optionalPersona = personasService.findById_persona(idPersona);
        if (optionalPersona.isPresent()) {
            // Actualizar el campo 'foto' con la URL
            Personas persona = optionalPersona.get();
            persona.setFoto(url);

            // Guardar los cambios en la base de Datos
            personasService.save(persona);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }

        // Devolver la URL de la imagen
        return Map.of("url", url);
    }



    /*
    @PostMapping("upload")
    public Map<String, String> uploadFile(@RequestParam("file")MultipartFile multipartFile){
        String path = storageService.store(multipartFile);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/media/")
                .path(path)
                .toUriString();

        return Map.of("url", url);
    }
     */

    /*
    @PostMapping("upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            // Intentamos almacenar el archivo
            String path = storageService.store(multipartFile);
            String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/media/")
                    .path(path)
                    .toUriString();

            // Si el almacenamiento fue exitoso, devolvemos un mensaje de éxito
            return ResponseEntity.ok(new ResponseMessage("profile photo successfully updated"));

        } catch (Exception e) {
            // En caso de error, devolvemos un mensaje con un código de estado HTTP adecuado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error al subir el archivo: " + e.getMessage()));
        }
    }
*/

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename ) throws IOException{
        Resource file = storageService.loadAsResource(filename);
        String contenType = Files.probeContentType((file.getFile().toPath()));
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contenType)
                .body(file);
    }

}
