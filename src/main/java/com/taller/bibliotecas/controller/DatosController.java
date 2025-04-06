package com.taller.bibliotecas.controller;


import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Datos;
import com.taller.bibliotecas.repository.DatosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/datos")
public class DatosController {
@Autowired
    DatosRepository datosRepository;

    @GetMapping(value = "all")
    public List<Datos> findAll() {
        return datosRepository.findAll();
    }

}
