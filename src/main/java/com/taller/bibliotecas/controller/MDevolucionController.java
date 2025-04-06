package com.taller.bibliotecas.controller;

import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.entitys.MDevolucion;
import com.taller.bibliotecas.repository.MDevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mdevolucion")
public class MDevolucionController {
@Autowired
    MDevolucionRepository mDevolucionRepository;

    @GetMapping(value = "all")
    public List<MDevolucion> findAll(){
        return mDevolucionRepository.findAll();
    }
}
