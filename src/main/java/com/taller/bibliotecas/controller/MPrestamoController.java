package com.taller.bibliotecas.controller;

import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.entitys.MPrestamo;
import com.taller.bibliotecas.projections.classBased.CrearPrestamoDTO;
import com.taller.bibliotecas.repository.MPrestamoRepository;
import com.taller.bibliotecas.services.MPrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mprestamo")
public class MPrestamoController {
    @Autowired
    MPrestamoRepository mPrestamoRepository;
    @Autowired
    MPrestamoService mPrestamoService;

    @GetMapping(value = "all")
    public List<MPrestamo> findAll(){
        return mPrestamoRepository.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<MPrestamo> crearPrestamo(@RequestBody CrearPrestamoDTO prestamoDTO) {
        try {
            MPrestamo nuevoPrestamo = mPrestamoService.createPrestamo(prestamoDTO);
            return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/idpersona/{idPersona}")
    public ResponseEntity<List<MPrestamo>> getPrestamosByPersonaId(@PathVariable Long idPersona) {
        List<MPrestamo> prestamos = mPrestamoService.getPrestamosByIdPersona(idPersona);
        if (prestamos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prestamos);
    }
}
