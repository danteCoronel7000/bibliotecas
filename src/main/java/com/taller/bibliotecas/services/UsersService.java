package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.UsuariosDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsersClosedView;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsuariosAndPersonas;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<Usuarios> findAll();

    List<UsersClosedView> findBy();

    Optional<UsuariosAndPersonas> usuariosPorId(Long id);

    Usuarios crearUsuario(Usuarios usuario);

    Usuarios actualizarUsuario(Usuarios usuario);

    List<UsuariosDTO> findUsuariosBy();

    List<RolesAsigNoAsig> findById_usuario(Long id_usuario, Long filtro);

}
