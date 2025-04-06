package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Editoriales;
import com.taller.bibliotecas.repository.EditorialesRepository;
import com.taller.bibliotecas.services.EditorialesService;
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
@RequestMapping("/api/editoriales")
public class EditorialesController {
    @Autowired
    EditorialesRepository editorialesRepository;
    @Autowired
    EditorialesService editorialesService;

    @GetMapping(value = "all")
    public List<Editoriales> findAll(){
        return editorialesRepository.findAll();
    }


    //endPoint para buscar personas por nombre y apellido
    @GetMapping("/searchEditorialesNative/{param}")
    public List<Editoriales> searchEditoriales(@PathVariable("param") String xfiltro) {
        if (xfiltro == null || xfiltro.isEmpty()) {
            // Retorna lista vacía si el filtro está vacío o es null
            return new ArrayList<>();
        }
        return editorialesRepository.searchEditorialesByNombre(xfiltro);
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Editoriales> createEditorial(@RequestBody Editoriales editorial) {
        Editoriales createdEditorial = editorialesRepository.save(editorial);
        return new ResponseEntity<>(createdEditorial, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Editoriales> editorialesPorId(@PathVariable Long id) {
        return editorialesService.findById_editorial(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
//metodo para actualizar el editorial
    @PostMapping("/actualizar")
    public ResponseEntity<Editoriales> actualizarEditorial(@RequestParam("editorial") String editorialJson) throws IOException {

        // Convertir el JSON de persona a la entidad Personas
        Editoriales editorial = new ObjectMapper().readValue(editorialJson, Editoriales.class);

        // Verificar si la persona ya existe en la base de datos
        if (editorial.getId_editorial() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Editoriales> existingEditorialOpt = editorialesService.findById_editorial(editorial.getId_editorial());
        if (existingEditorialOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra la persona, devolver 404
        }

        Editoriales existingEditorial = existingEditorialOpt.get();

        // Actualizar los datos de la persona existente con los nuevos valores
        existingEditorial.setNombre(editorial.getNombre());
        existingEditorial.setEstado(editorial.getEstado());

        // Guardar la persona actualizada
        Editoriales updatedEditorial = editorialesService.save(existingEditorial);

        // Devolver la respuesta con la persona actualizada
        return ResponseEntity.ok(updatedEditorial);
    }

    @PostMapping("/estado")
    public ResponseEntity<Editoriales> deleteEditorial(@RequestBody Map<String, Long> payload) {
        Long id_editorial = payload.get("id_editorial"); // Extrae id_persona del cuerpo de la solicitud

        Editoriales editorialActualizada = editorialesService.deleteEditorial(id_editorial);
        return ResponseEntity.ok(editorialActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Editoriales> habilitarEditorial(@RequestBody Map<String, Long> payload) {
        Long idEditorial = payload.get("id_editorial"); // Extrae id_persona del cuerpo de la solicitud
        System.out.println(idEditorial);
        Editoriales editorialActualizada = editorialesService.habilitarEditorial(idEditorial);
        return ResponseEntity.ok(editorialActualizada);
    }


}
