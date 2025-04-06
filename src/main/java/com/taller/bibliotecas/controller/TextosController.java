package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Textos;
import com.taller.bibliotecas.repository.TextosRepository;
import com.taller.bibliotecas.services.TextosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/textos")
public class TextosController {
    @Autowired
    TextosRepository textosRepository;
    @Autowired
    TextosService textosService;

    @GetMapping(value = "all")
    public List<Textos> findAll(){
        return textosRepository.findAll();
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Textos> createTexto(@RequestBody Textos texto) {
        Textos createdTexto = textosRepository.save(texto);
        return new ResponseEntity<>(createdTexto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Textos> textosPorId(@PathVariable Long id) {
        return textosService.findById_texto(id);
    }

    // Método para actualizar el texto
    @PostMapping("/actualizar")
    public ResponseEntity<Textos> actualizarTexto(@RequestParam("texto") String textoJson) throws IOException {

        // Convertir el JSON de texto a la entidad Textos
        Textos texto = new ObjectMapper().readValue(textoJson, Textos.class);

        // Verificar si el texto ya existe en la base de datos
        if (texto.getId_texto() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Textos> existingTextoOpt = textosService.findById_texto(texto.getId_texto());
        if (existingTextoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra el texto, devolver 404
        }

        Textos existingTexto = existingTextoOpt.get();

        // Actualizar los datos del texto existente con los nuevos valores
        existingTexto.setTitulo(texto.getTitulo());
        existingTexto.setResumen(texto.getResumen());
        existingTexto.setIsbn(texto.getIsbn());
        existingTexto.setEdicion(texto.getEdicion());
        existingTexto.setFechapub(texto.getFechapub());


        // Guardar el texto actualizado
        Textos updatedTexto = textosService.save(existingTexto);

        // Devolver la respuesta con el texto actualizado
        return ResponseEntity.ok(updatedTexto);
    }

    @PostMapping("/estado")
    public ResponseEntity<Textos> deleteTexto(@RequestBody Map<String, Long> payload) {
        Long id_texto = payload.get("id_texto"); // Extrae id_texto del cuerpo de la solicitud

        Textos textoActualizada = textosService.deleteTextos(id_texto);
        return ResponseEntity.ok(textoActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Textos> habilitarTexto(@RequestBody Map<String, Long> payload) {
        Long idTexto = payload.get("id_texto"); // Extrae id_texto del cuerpo de la solicitud
        System.out.println(idTexto);
        Textos textoActualizada = textosService.habilitarTexto(idTexto);
        return ResponseEntity.ok(textoActualizada);
    }


}


/*
 // Si quieres usar el nombre del archivo como el valor de docum:
        Long tipodioimp = 15L;
        if (file == null) {
            tipodioimp = 16L;
        }

        // Crear y asignar el tipo "digital" o "impreso"
        Tipos tipoDigital = tiposRepository.findById(tipodioimp)  // Asume que el ID 15 es "digital"
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        TipoTex tipoTex = new TipoTex();
        TipoTextPK tipoTextPK = new TipoTextPK();
        tipoTextPK.setId_texto(savedTexto.getId_texto());
        tipoTextPK.setId_tipo(tipoDigital.getId_tipo());
        tipoTextPK.setDocum(fileName);  // Aquí asignamos el nombre del archivo al campo docum

        tipoTex.setIdTipotex(tipoTextPK);
        tipoTex.setTexto(savedTexto);
        tipoTex.setTipo(tipoDigital);

        // Guardar la relación en la tabla intermedia
        tipoTexRepository.save(tipoTex);
 */