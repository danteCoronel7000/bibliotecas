package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.UsuariosDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsersClosedView;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsuariosAndPersonas;
import com.taller.bibliotecas.repository.RolesRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class usersServiceImpl implements UsersService{

    private final UsuariosRepository usuariosRepository;
    private final PersonasService personasService;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public usersServiceImpl(RolesRepository rolesRepository, UsuariosRepository usuariosRepository, PersonasService personasService, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.personasService =personasService;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuarios> findAll() {
        return usuariosRepository.findAll();
    }

    @Override
    public List<UsersClosedView> findBy() {
        return usuariosRepository.findBy();
    }

    @Override
    public Optional<UsuariosAndPersonas> usuariosPorId(Long id) {
        return usuariosRepository.usuariosPorId(id);
    }

    @Override
    public Usuarios crearUsuario(Usuarios usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Validar y asociar la persona
        if (usuario.getPersona() != null && usuario.getPersona().getId_persona() != null) {
            Long idPersona = usuario.getPersona().getId_persona();
            Personas persona = personasService.findById_persona(idPersona)
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + idPersona));
            usuario.setPersona(persona);
        } else {
            throw new RuntimeException("ID de persona no proporcionado");
        }

        // Guardar el usuario en la base de datos
        return usuariosRepository.save(usuario);
    }

    // Método para actualizar un usuario
    @Override
    public Usuarios actualizarUsuario(Usuarios usuario) {
        // Verifica si el usuario existe en la base de datos
        Optional<Usuarios> usuarioExistente = usuariosRepository.obtenerUsuarioPorId(usuario.getId_usuario());

        if (usuarioExistente.isPresent()) {
            // Si el usuario existe, copia los cambios y guárdalo
            Usuarios usuarioParaActualizar = usuarioExistente.get();
            usuarioParaActualizar.setPassword(usuario.getPassword());
            // Agrega todos los demás campos que deseas actualizar
            return usuariosRepository.save(usuarioParaActualizar);
        } else {
            throw new NoSuchElementException("Usuario no encontrado");
        }
    }

    @Override
    public List<UsuariosDTO> findUsuariosBy() {
        return usuariosRepository.findUsuariosBy();
    }

    @Override
    public List<RolesAsigNoAsig> findById_usuario(Long id_usuario, Long filtro) {
        // Verificar si el filtro es null o no tiene un valor válido (0, 1, 2)
        if (filtro == null || (filtro != 0 && filtro != 1 && filtro != 2)) {
            return new ArrayList<>(); // Devuelve una lista vacía
        }

        // Obtener el usuario por su id_usuario utilizando el repositorio
        Optional<Usuarios> usuarioOptional = usuariosRepository.findById(id_usuario);

        // Lista que se devolverá según los parámetros que lleguen
        List<RolesAsigNoAsig> rolesAsigNoAsigList = new ArrayList<>();

        // Verificar si el usuario existe
        if (usuarioOptional.isPresent()) {
            Usuarios usuario = usuarioOptional.get();

            // Obtener todos los roles
            List<Roles> todosLosRoles = rolesRepository.findAll();

            // Procesar cada rol para determinar su asignación
            for (Roles rol : todosLosRoles) {
                RolesAsigNoAsig rolDto = new RolesAsigNoAsig();
                rolDto.setId_rol(rol.getId_rol());
                rolDto.setNombre(rol.getNombre());
                rolDto.setEstado(rol.getEstado());

                // Verificar si el rol está asignado al usuario
                boolean estaAsignado = usuario.getRolesList().stream()
                        .anyMatch(r -> r.getId_rol().equals(rol.getId_rol()));

                rolDto.setAsig(estaAsignado ? 1L : 0L); // 1 si está asignado, 0 si no

                // Aplicar el filtro
                if (filtro == 2) {
                    // Si el filtro es 2, se devuelve toda la lista
                    rolesAsigNoAsigList.add(rolDto);
                } else if (filtro == 1 && estaAsignado) {
                    // Si el filtro es 1, se devuelven solo los roles asignados al usuario
                    rolesAsigNoAsigList.add(rolDto);
                } else if (filtro == 0 && !estaAsignado) {
                    // Si el filtro es 0, se devuelven solo los roles no asignados al usuario
                    rolesAsigNoAsigList.add(rolDto);
                }
            }
        }

        return rolesAsigNoAsigList;
    }


}
