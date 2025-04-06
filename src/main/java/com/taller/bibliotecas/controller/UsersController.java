package com.taller.bibliotecas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.entitys.Textos;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.UsuariosDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsersClosedView;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsuariosAndPersonas;
import com.taller.bibliotecas.repository.RolesRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import com.taller.bibliotecas.services.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsersController {

    @Autowired
    UsersService usersService;
    @Autowired
    UsuariosRepository usuariosRepository;
    @Autowired
    RolesRepository rolesRepository;


    @Value("${jwt.secret.key}")
    String secretKey;
    @Value("${jwt.time.expiration}")
    String timeExpiration;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String getJWTToken(String username) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +Long.parseLong(timeExpiration)))
                //.setExpiration(new Date(System.currentTimeMillis() + 600000 ))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return "Bearer " + token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> acceso(@RequestBody Map<String, String> loginData) {
        String xusername = loginData.get("username");
        String xpassword = loginData.get("password");

        // Obtenemos el usuario como Optional y lo manejamos
        Optional<Usuarios> userOptional = usuariosRepository.findByUsername(xusername);

        // Verificamos si el usuario existe y si la contraseña coincide
        if (userOptional.isPresent() && passwordEncoder.matches(xpassword, userOptional.get().getPassword())) {
            Usuarios user = userOptional.get();
            try {
                String xtoken = getJWTToken(xusername);
                System.out.println("este es mi TOKEN generado::" + xtoken);
                user.setToken(xtoken);
                return ResponseEntity.ok(user); // Devuelve el usuario con el token
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error al generar el token"); // Devuelve un mensaje de error
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado o contraseña incorrecta");
        }
    }


    @GetMapping(value = "all")
    public List<Usuarios> findAll(){
        return usersService.findAll();
    }

    @GetMapping(value = "allClosedView")
    public List<UsersClosedView> findBy(){
        return usersService.findBy();
    }

    @GetMapping(value = "classBased")
    public List<UsuariosDTO> findUsuariosClassBased(){
        return usersService.findUsuariosBy();
    }

    //usuarios and personas
    @GetMapping(value = "/usuariosById/{id}")
    public Optional<UsuariosAndPersonas> usuariosById(@PathVariable Long id){
        return usersService.usuariosPorId(id);
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuarios> crearUsuario(@RequestBody Usuarios usuario) {
        Usuarios nuevoUsuario = usersService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PutMapping("/editar")
    public ResponseEntity<Usuarios> editarUsuario(@RequestBody Usuarios usuario) {

        // Verifica si el ID del usuario existe en el objeto recibido
        if (usuario.getId_usuario() == null) {
            return ResponseEntity.badRequest().body(null);  // Retorna error si no hay ID
        }

        // Llama al método de servicio para actualizar el usuario
        try {
            Usuarios usuarioActualizado = usersService.actualizarUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();  //- Retorna error si el usuario no existe
        }
    }

//filtro: si filtro llega 2 se devuelve toda la lista: si si llega 0  se devuelve los roles que no estan asignados a ese usuario
    // si llega uno se debuelve una lista con los roles que estan asignados a ese usuario del id que llega
    @GetMapping(value = "/filtrarRoles/{id}/{filtro}")
    public List<RolesAsigNoAsig> usuariosPorId(@PathVariable Long id, @PathVariable Long filtro){
        return usersService.findById_usuario(id, filtro);
    }

    @GetMapping(value = "/obternerPorId/{id}")
    public Optional<Usuarios> usuarioPorId(@PathVariable Long id) {
        return usuariosRepository.findById(id);
    }


    //añadir roles a un usuario
    @PostMapping("/addRolesAUnUsuario/{id}")
    public ResponseEntity<Usuarios> addRolesAUnUsuario(
            @PathVariable Long id,
            @RequestParam("roles") String rolesJson
    ) throws IOException {
        System.out.println("JSON recibido: " + rolesJson);

        // Buscar el usuario existente por ID
        Usuarios existingUsuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertir el JSON recibido a una lista de IDs de roles
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> rolesIds;
        try {
            rolesIds = objectMapper.readValue(rolesJson, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el JSON de roles", e);
        }

        // Buscar los roles en la base de datos
        List<Roles> roles = rolesRepository.findAllById(rolesIds);

        if (roles.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Filtrar los roles que ya están asociados
        List<Roles> nuevosRoles = roles.stream()
                .filter(rol -> !existingUsuario.getRolesList().contains(rol))
                .toList();

        if (nuevosRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(existingUsuario); // No se añadió nada porque ya existían todas las relaciones
        }

        // Asociar solo los roles nuevos al usuario
        existingUsuario.getRolesList().addAll(nuevosRoles);

        // Guardar los cambios en la base de datos
        Usuarios updatedUsuario = usuariosRepository.save(existingUsuario);

        return ResponseEntity.ok(updatedUsuario);
    }


}
