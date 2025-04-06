package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.RolesClosedView;
import com.taller.bibliotecas.repository.RolesRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService{
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    UsuariosRepository usuariosRepository;

    @Override
    public Optional<Roles> findById_rol(Long id_rol) {
        return rolesRepository.findById(id_rol);
    }

    @Override
    public Roles save(Roles rol) {
        return rolesRepository.save(rol);
    }

    @Override
    public Roles deleteRoles(Long id_rol) {
        Roles rol = rolesRepository.findById(id_rol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id_rol));

        // Actualizar el estado del rol
        rol.setEstado(0L);

        // Guardar los cambios en la base de datos
        return rolesRepository.save(rol);
    }

    @Override
    public Roles habilitarRol(Long id_rol) {
        Roles rol = rolesRepository.findById(id_rol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id_rol));

        // Actualizar el estado del rol
        rol.setEstado(1L);

        // Guardar los cambios en la base de datos
        return rolesRepository.save(rol);
    }

    @Override
    public List<RolesClosedView> findRolesDto() {
        return rolesRepository.findBy();
    }

    @Override
    public List<RolesAsigNoAsig> findRolesAsig() {
        // Obtener todas las entidades Roles y Usuarios
        List<Roles> roles = rolesRepository.findAll();
        List<Usuarios> usuarios = usuariosRepository.findAll();

        List<RolesAsigNoAsig> rolesAsigNoAsigList = new ArrayList<>();

        // Procesar cada rol para determinar su asignación
        for (Roles rol : roles) {
            RolesAsigNoAsig rolDto = new RolesAsigNoAsig();
            rolDto.setId_rol(rol.getId_rol());
            rolDto.setNombre(rol.getNombre());
            rolDto.setEstado(rol.getEstado());
            rolDto.setAsig(0L); // Por defecto, no asignado

            // Verificar si el rol está asignado a algún usuario
            for (Usuarios usuario : usuarios) {
                if (usuario.getRolesList().stream().anyMatch(r -> r.getId_rol().equals(rol.getId_rol()))) {
                    rolDto.setAsig(usuario.getId_usuario()); // Asignar el ID del usuario
                    break;
                }
            }

            rolesAsigNoAsigList.add(rolDto);
        }

        return rolesAsigNoAsigList;
    }

}
