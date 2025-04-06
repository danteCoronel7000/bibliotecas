package com.taller.bibliotecas.controller;


import com.taller.bibliotecas.entitys.Menus;
import com.taller.bibliotecas.entitys.Procesos;
import com.taller.bibliotecas.projections.classBased.ProcesosAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.repository.ProcesosRepository;
import com.taller.bibliotecas.services.ProcesosService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/procesos")
public class ProcesosController {
    @Autowired
    ProcesosRepository procesosRepository;
    @Autowired
    ProcesosService procesosService;

    @GetMapping(value = "all")
    public List<Procesos> findAll(){
        return procesosRepository.findAll();
    }

    //filtro: si filtro llega 2 se devuelve toda la lista: si si llega 0  se devuelve los roles que no estan asignados a ese usuario
    // si llega uno se debuelve una lista con los roles que estan asignados a ese usuario del id que llega
    @GetMapping(value = "/filtrarProcesos/{id}/{filtro}")
    public List<ProcesosAsigNoAsig> filtrarListaProcesos(@PathVariable Long id, @PathVariable Long filtro){
        return procesosService.filtrarProcesos(id, filtro);
    }

}
