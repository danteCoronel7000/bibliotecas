package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Tipos;
import com.taller.bibliotecas.repository.TiposRepository;
import com.taller.bibliotecas.services.TiposService;
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
@RequestMapping("/api/tipos")
public class TiposController {
    @Autowired
    TiposRepository tiposRepository;
    @Autowired
    TiposService tiposService;

    @GetMapping(value = "all")
    public List<Tipos> findAll(){
        return tiposRepository.findAll();
    }

    // EndPoint para buscar tipos por nombre
    @GetMapping("/searchTiposNative/{param}")
    public List<Tipos> searchTipos(@PathVariable("param") String xfiltro) {
        if (xfiltro == null || xfiltro.isEmpty()) {
            // Retorna lista vacía si el filtro está vacío o es null
            return new ArrayList<>();
        }
        return tiposRepository.searchTiposByNombre(xfiltro);
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Tipos> createTipo(@RequestBody Tipos tipo) {
        Tipos createdTipo = tiposRepository.save(tipo);
        return new ResponseEntity<>(createdTipo, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Tipos> tiposPorId(@PathVariable Long id) {
        return tiposService.findById_tipo(id);
    }

    // Método para actualizar el tipo
    @PostMapping("/actualizar")
    public ResponseEntity<Tipos> actualizarTipo(@RequestParam("tipo") String tipoJson) throws IOException {

        // Convertir el JSON de tipo a la entidad Tipos
        Tipos tipo = new ObjectMapper().readValue(tipoJson, Tipos.class);

        // Verificar si el tipo ya existe en la base de datos
        if (tipo.getId_tipo() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Tipos> existingTipoOpt = tiposService.findById_tipo(tipo.getId_tipo());
        if (existingTipoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra el tipo, devolver 404
        }

        Tipos existingTipo = existingTipoOpt.get();

        // Actualizar los datos del tipo existente con los nuevos valores
        existingTipo.setNombre(tipo.getNombre());
        existingTipo.setEstado(tipo.getEstado());
        existingTipo.setSw(tipo.getSw());

        // Guardar el tipo actualizado
        Tipos updatedTipo = tiposService.save(existingTipo);

        // Devolver la respuesta con el tipo actualizado
        return ResponseEntity.ok(updatedTipo);
    }

    @PostMapping("/estado")
    public ResponseEntity<Tipos> deleteTipo(@RequestBody Map<String, Long> payload) {
        Long id_tipo = payload.get("id_tipo"); // Extrae id_tipo del cuerpo de la solicitud

        Tipos tipoActualizada = tiposService.deleteTipos(id_tipo);
        return ResponseEntity.ok(tipoActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Tipos> habilitarTipo(@RequestBody Map<String, Long> payload) {
        Long idTipo = payload.get("id_tipo"); // Extrae id_tipo del cuerpo de la solicitud
        System.out.println(idTipo);
        Tipos tipoActualizada = tiposService.habilitarTipo(idTipo);
        return ResponseEntity.ok(tipoActualizada);
    }


}
