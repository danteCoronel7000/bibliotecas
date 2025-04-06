package com.taller.bibliotecas.repository;


import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.UsuariosDTO;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsersClosedView;
import com.taller.bibliotecas.projections.interfaceBased.closed.UsuariosAndPersonas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByUsername(String username);

    @Query("select u "
            +" from Usuarios u "
            +" where 	u.username=?1 and "
            +"			u.password=?2 ")
    Usuarios verificarCuentaUsuario(String xusername, String xpassword);

    List<UsersClosedView> findBy();

    @Query("select u "
            +" from Usuarios u "
            +" where 	u.id_usuario=?1")
    Optional<UsuariosAndPersonas> usuariosPorId(Long id_usuario);


    @Query("select u "
            +" from Usuarios u "
            +" where 	u.id_usuario=?1")
    Optional<Usuarios> obtenerUsuarioPorId(Long id);

    List<UsuariosDTO> findUsuariosBy();

}

