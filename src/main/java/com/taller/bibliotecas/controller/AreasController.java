package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.repository.AreasRepository;
import com.taller.bibliotecas.services.AreasService;
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
@RequestMapping("/api/areas")
public class AreasController {
    @Autowired
    AreasRepository areasRepository;
    @Autowired
    AreasService areasService;

    @GetMapping(value = "all")
    public List<Areas> findAll() {
        return areasRepository.findAll();
    }

    //endPoint para buscar personas por nombre y apellido
    @GetMapping("/searchAreasNative/{param}")
    public List<Areas> searchPersonas(@PathVariable("param") String xfiltro) {
        if (xfiltro == null || xfiltro.isEmpty()) {
            // Retorna lista vacía si el filtro está vacío o es null
            return new ArrayList<>();
        }
        return areasRepository.searchAreasByNombre(xfiltro);
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Areas> createArea(@RequestBody Areas area) {
        Areas createdArea = areasRepository.save(area);
        return new ResponseEntity<>(createdArea, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Areas> areasPorId(@PathVariable Long id) {
        return areasService.findById_area(id);
    }

//metodo para actualizar el area
    @PostMapping("/actualizar")
    public ResponseEntity<Areas> actualizarArea(@RequestParam("area") String areaJson) throws IOException {

        // Convertir el JSON de persona a la entidad Personas
        Areas areas = new ObjectMapper().readValue(areaJson, Areas.class);

        // Verificar si la persona ya existe en la base de datos
        if (areas.getId_area() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Areas> existingAreaOpt = areasService.findById_area(areas.getId_area());
        if (existingAreaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra la persona, devolver 404
        }

        Areas existingArea = existingAreaOpt.get();

        // Actualizar los datos de la persona existente con los nuevos valores
        existingArea.setNombre(areas.getNombre());
        existingArea.setEstado(areas.getEstado());

        // Guardar la persona actualizada
        Areas updatedArea = areasService.save(existingArea);

        // Devolver la respuesta con la persona actualizada
        return ResponseEntity.ok(updatedArea);
    }

    @PostMapping("/estado")
    public ResponseEntity<Areas> deleteArea(@RequestBody Map<String, Long> payload) {
        Long id_area = payload.get("id_area"); // Extrae id_persona del cuerpo de la solicitud

        Areas areaActualizada = areasService.deleteArea(id_area);
        return ResponseEntity.ok(areaActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Areas> habilitarArea(@RequestBody Map<String, Long> payload) {
        Long idArea = payload.get("id_area"); // Extrae id_persona del cuerpo de la solicitud
        System.out.println(idArea);
        Areas areaActualizada = areasService.habilitarArea(idArea);
        return ResponseEntity.ok(areaActualizada);
    }


}
