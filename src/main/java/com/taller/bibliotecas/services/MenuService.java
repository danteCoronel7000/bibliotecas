package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Menus;
import com.taller.bibliotecas.projections.classBased.MenusAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.ProcesosAsigNoAsig;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    Optional<Menus> findById_menu(Long id_menu);

    Menus save(Menus menu);

    Menus deleteMenu(Long id_menu);

    Menus habilitarMenu(Long id_menu);

    List<MenusAsigNoAsig> findAllAsigNoAsig();

    List<ProcesosAsigNoAsig> findAllProcesosAsigNoAsig();

    List<MenusAsigNoAsig> filtrarMenus(Long id_rol, Long filtro);
}
