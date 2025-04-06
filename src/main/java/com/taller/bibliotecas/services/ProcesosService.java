package com.taller.bibliotecas.services;

import com.taller.bibliotecas.projections.classBased.ProcesosAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;

import java.util.List;

public interface ProcesosService {
    List<ProcesosAsigNoAsig> filtrarProcesos(Long id_menu, Long filtro);
}
