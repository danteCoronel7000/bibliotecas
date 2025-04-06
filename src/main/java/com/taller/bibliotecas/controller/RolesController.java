package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.entitys.Menus;
import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.RolesClosedView;
import com.taller.bibliotecas.repository.AutoresRepository;
import com.taller.bibliotecas.repository.MenusRepository;
import com.taller.bibliotecas.repository.RolesRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import com.taller.bibliotecas.services.AutoresService;
import com.taller.bibliotecas.services.RolesService;
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
@RequestMapping("/api/roles")
public class RolesController {
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    RolesService rolesService;
    @Autowired
    MenusRepository menusRepository;
    @Autowired
    UsuariosRepository usuariosRepository;


    @GetMapping(value = "all")
    public List<Roles> findAll() {
        return rolesRepository.findAll();
    }

    // dto based in class view based
    @GetMapping(value = "alldto")
    public List<RolesClosedView> findAllDTO() {
        return rolesService.findRolesDto();
    }

    // dto based in class view based
    @GetMapping(value = "allAsig")
    public List<RolesAsigNoAsig> finAllAsig() {
        return rolesService.findRolesAsig();
    }


    @PostMapping(value = "/crear")
    public ResponseEntity<Roles> createRol(@RequestBody Roles rol) {
        Roles createdRol = rolesRepository.save(rol);
        return new ResponseEntity<>(createdRol, HttpStatus.CREATED);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Roles> rolesPorId(@PathVariable Long id) {
        return rolesService.findById_rol(id);
    }

    // Método para actualizar el rol
    @PostMapping("/actualizar")
    public ResponseEntity<Roles> actualizarRol(@RequestParam("rol") String rolJson) throws IOException {

        // Convertir el JSON de rol a la entidad Roles
        Roles rol = new ObjectMapper().readValue(rolJson, Roles.class);

        // Verificar si el rol ya existe en la base de datos
        if (rol.getId_rol() == null) {
            return ResponseEntity.badRequest().body(null);  // Si no tiene ID, es un error de solicitud
        }

        Optional<Roles> existingRolOpt = rolesService.findById_rol(rol.getId_rol());
        if (existingRolOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Si no se encuentra el rol, devolver 404
        }

        Roles existingRol = existingRolOpt.get();

        // Actualizar los datos del rol existente con los nuevos valores
        existingRol.setNombre(rol.getNombre());
        existingRol.setEstado(rol.getEstado());

        // Guardar el rol actualizado
        Roles updatedRol = rolesService.save(existingRol);

        // Devolver la respuesta con el rol actualizado
        return ResponseEntity.ok(updatedRol);
    }

    @PostMapping("/estado")
    public ResponseEntity<Roles> deleteRol(@RequestBody Map<String, Long> payload) {
        Long id_rol = payload.get("id_rol"); // Extrae id_rol del cuerpo de la solicitud

        Roles rolActualizada = rolesService.deleteRoles(id_rol);
        return ResponseEntity.ok(rolActualizada);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Roles> habilitarRol(@RequestBody Map<String, Long> payload) {
        Long idRol = payload.get("id_rol"); // Extrae id_rol del cuerpo de la solicitud
        System.out.println(idRol);
        Roles rolActualizada = rolesService.habilitarRol(idRol);
        return ResponseEntity.ok(rolActualizada);
    }

    //añadir procesos a un rol
    //añadir menus a un rol
    @PostMapping("/addMenusAUnRol/{id}")
    public ResponseEntity<Roles> addMenusAUnRol(
            @PathVariable Long id,
            @RequestParam("menus") String menusJson
    ) throws IOException {
        System.out.println("JSON recibido: " + menusJson);

        // Buscar el rol existente por ID
        Roles existingRol = rolesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Convertir el JSON recibido a una lista de IDs de menus
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> menusIds;
        try {
            menusIds = objectMapper.readValue(menusJson, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el JSON de menus", e);
        }

        // Buscar los menus en la base de datos
        List<Menus> menus = menusRepository.findAllById(menusIds);

        if (menus.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Filtrar los menus que ya están asociados
        List<Menus> nuevosMenus = menus.stream()
                .filter(menu -> !existingRol.getMenusList().contains(menu))
                .toList();

        if (nuevosMenus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(existingRol); // No se añadió nada porque ya existían todas las relaciones
        }

        // Asociar solo los menus nuevos al rol
        existingRol.getMenusList().addAll(nuevosMenus);

        // Guardar los cambios en la base de datos
        Roles updatedRol = rolesRepository.save(existingRol);

        return ResponseEntity.ok(updatedRol);
    }

}
