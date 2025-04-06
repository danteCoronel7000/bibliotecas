package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.RolesClosedView;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    Optional<Roles> findById_rol(Long id_rol);

    Roles save(Roles rol);

    Roles deleteRoles(Long id_rol);

    Roles habilitarRol(Long id_rol);

    List<RolesClosedView> findRolesDto();

    List<RolesAsigNoAsig> findRolesAsig();


}
