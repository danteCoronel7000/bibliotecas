package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenusRepository extends JpaRepository<Menus, Long> {

    @Query("select u "
            +" from Menus u "
            +" where 	u.id_menu=?1")
    List<Menus> obtenerMenuPorId(Long id);
}
