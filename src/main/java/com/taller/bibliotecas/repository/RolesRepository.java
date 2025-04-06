package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.projections.classBased.RolesDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.RolesClosedView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Query("select u "
            +" from Roles u "
            +" where 	u.id_rol=?1")
    List<Roles> obtenerRolPorId(Long id_rol);

    /*
    @Query(value = "SELECT id_rol, nombre, estado FROM roles", nativeQuery = true)
    List<RolesDTO> findAllRolesNative();
    */

    List<RolesClosedView> findBy();
}
