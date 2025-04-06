package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.repository.AutoresRepository;
import com.taller.bibliotecas.services.AutoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
public class AutoresController {
    @Autowired
    AutoresRepository autoresRepository;
    @Autowired
    AutoresService autoresService;

    @GetMapping(value = "all")
    public List<Autores> findAll(){
        return autoresRepository.findAll();
    }

    // EndPoint para buscar autores por nombre
    @GetMapping("/searchAutoresNative/{param}")
    public List<Autores> searchAutores(@PathVariable("param") String xfiltro) {
        if (xfiltro == null || xfiltro.isEmpty()) {
            // Retorna lista vacía si el filtro está vacío o es null
            return new ArrayList<>();
        }

        // Divide el filtro en palabras
        String[] palabras = xfiltro.split(" ");
        String filtroNombre = palabras.length > 0 ? palabras[0] : "";
        String filtroAp = palabras.length > 1 ? palabras[1] : "";
        String filtroAm = palabras.length > 2 ? palabras[2] : "";

        return autoresRepository.searchAutoresNative(filtroNombre, filtroAp, filtroAm);
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Autores> createAutor(@RequestBody Autores autor) {
        Autores createdAutor = autoresRepository.save(autor);
        return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Autores> autoresPorId(@PathVariable Long id) {
        return autoresService.findById_autor(id);
    }

    // Método para actualizar el autor
    @PostMapping("/actualizar")
    public ResponseEntity<Autores> actualizarAutor(@RequestParam("autor") String autorJson) throws IOException {

        // Convertir el JSON de autor a la entidad Autores
        Autores autor = new ObjectMapper().readValue(autorJson, Autores.class);

        // Verificar si el autor ya existe en la base de datos
        if (autor.getId_autor() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Autores> existingAutorOpt = autoresService.findById_autor(autor.getId_autor());
        if (existingAutorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra el autor, devolver 404
        }

        Autores existingAutor = existingAutorOpt.get();

        // Actualizar los datos del autor existente con los nuevos valores
        existingAutor.setNombre(autor.getNombre());
        existingAutor.setAp(autor.getAp());
        existingAutor.setAm(autor.getAm());
        existingAutor.setGenero(autor.getGenero());
        existingAutor.setEstado(autor.getEstado());


        // Guardar el autor actualizado
        Autores updatedAutor = autoresService.save(existingAutor);

        // Devolver la respuesta con el autor actualizado
        return ResponseEntity.ok(updatedAutor);
    }

    @PostMapping("/estado")
    public ResponseEntity<Autores> deleteAutor(@RequestBody Map<String, Long> payload) {
        Long id_autor = payload.get("id_autor"); // Extrae id_autor del cuerpo de la solicitud

        Autores autorActualizada = autoresService.deleteAutores(id_autor);
        return ResponseEntity.ok(autorActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Autores> habilitarAutor(@RequestBody Map<String, Long> payload) {
        Long idAutor = payload.get("id_autor"); // Extrae id_autor del cuerpo de la solicitud
        System.out.println(idAutor);
        Autores autorActualizada = autoresService.habilitarAutor(idAutor);
        return ResponseEntity.ok(autorActualizada);
    }

}
