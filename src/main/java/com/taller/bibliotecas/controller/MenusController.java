package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.projections.classBased.MenusAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.ProcesosAsigNoAsig;
import com.taller.bibliotecas.projections.interfaceBased.closed.RolesClosedView;
import com.taller.bibliotecas.repository.AutoresRepository;
import com.taller.bibliotecas.repository.MenusRepository;
import com.taller.bibliotecas.repository.ProcesosRepository;
import com.taller.bibliotecas.services.AutoresService;
import com.taller.bibliotecas.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/menus")
public class MenusController {
    @Autowired
    MenusRepository menusRepository;
    @Autowired
    ProcesosRepository procesosRepository;

    @Autowired
    MenuService menusService;

    @GetMapping(value = "all")
    public List<Menus> findAll(){
        return menusRepository.findAll();
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Menus> createMenu(@RequestBody Menus menu) {
        Menus createdMenu = menusRepository.save(menu);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Menus> menuPorId(@PathVariable Long id) {
        return menusService.findById_menu(id);
    }

    // Método para actualizar el menu
    @PostMapping("/actualizar")
    public ResponseEntity<Menus> actualizarMenu(@RequestParam("menu") String menuJson) throws IOException {

        // Convertir el JSON de menu a la entidad Menus
        Menus menu = new ObjectMapper().readValue(menuJson, Menus.class);

        // Verificar si el menu ya existe en la base de datos
        if (menu.getId_menu() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Menus> existingMenuOpt = menusService.findById_menu(menu.getId_menu());
        if (existingMenuOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra el menu, devolver 404
        }

        Menus existingMenu = existingMenuOpt.get();

        // Actualizar los datos del menu existente con los nuevos valores
        existingMenu.setNombre(menu.getNombre());
        existingMenu.setEstado(menu.getEstado());

        // Guardar el menu actualizado
        Menus updatedMenu = menusService.save(existingMenu);

        // Devolver la respuesta con el menu actualizado
        return ResponseEntity.ok(updatedMenu);
    }

    @PostMapping("/estado")
    public ResponseEntity<Menus> deleteMenu(@RequestBody Map<String, Long> payload) {
        Long id_menu = payload.get("id_menu"); // Extrae id_menu del cuerpo de la solicitud

        Menus menuActualizado = menusService.deleteMenu(id_menu);
        return ResponseEntity.ok(menuActualizado);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Menus> habilitarMenu(@RequestBody Map<String, Long> payload) {
        Long idMenu = payload.get("id_menu"); // Extrae id_menu del cuerpo de la solicitud
        System.out.println(idMenu);
        Menus menuActualizado = menusService.habilitarMenu(idMenu);
        return ResponseEntity.ok(menuActualizado);
    }

    //añadir procesos a un menu
    @PostMapping("/addProcesosAUnMenu/{id}")
    public ResponseEntity<Menus> addProcesosAUnMenu(
            @PathVariable Long id,
            @RequestParam("procesos") String procesosJson
    ) throws IOException {
        System.out.println("JSON recibido: " + procesosJson);

        // Buscar el menú existente por ID
        Menus existingMenu = menusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu no encontrado"));

        // Convertir el JSON recibido a una lista de IDs de procesos
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> procesosIds;
        try {
            procesosIds = objectMapper.readValue(procesosJson, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el JSON de procesos", e);
        }

        // Buscar los procesos en la base de datos
        List<Procesos> procesos = procesosRepository.findAllById(procesosIds);

        if (procesos.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Filtrar los procesos que ya están asociados
        List<Procesos> nuevosProcesos = procesos.stream()
                .filter(proceso -> !existingMenu.getProcesosList().contains(proceso))
                .toList();

        if (nuevosProcesos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(existingMenu); // No se añadió nada porque ya existían todas las relaciones
        }

        // Asociar solo los procesos nuevos al menú
        existingMenu.getProcesosList().addAll(nuevosProcesos);

        // Guardar los cambios en la base de datos
        Menus updatedMenu = menusRepository.save(existingMenu);

        return ResponseEntity.ok(updatedMenu);
    }

    // dto based in class view based
    @GetMapping(value = "alldto")
    public List<MenusAsigNoAsig> findAllDTO() {
        return menusService.findAllAsigNoAsig();
    }

    @GetMapping(value = "alldtoPro")
    public List<ProcesosAsigNoAsig> findAllDTOpro() {
        return menusService.findAllProcesosAsigNoAsig();
    }

    @GetMapping(value = "/filtrarMenus/{id}/{filtro}")
    public List<MenusAsigNoAsig> filtrarListaMenus(@PathVariable Long id, @PathVariable Long filtro){
        return menusService.filtrarMenus(id, filtro);
    }

}
