package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.Menus;
import com.taller.bibliotecas.entitys.Procesos;
import com.taller.bibliotecas.entitys.Roles;
import com.taller.bibliotecas.entitys.Usuarios;
import com.taller.bibliotecas.projections.classBased.ProcesosAsigNoAsig;
import com.taller.bibliotecas.projections.classBased.RolesAsigNoAsig;
import com.taller.bibliotecas.repository.MenusRepository;
import com.taller.bibliotecas.repository.ProcesosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcesosServiceImpl implements ProcesosService {

    private final ProcesosRepository procesosRepository;
    private final MenusRepository menusRepository;

    public ProcesosServiceImpl(ProcesosRepository procesosRepository, MenusRepository menusRepository) {
        this.procesosRepository = procesosRepository;
        this.menusRepository = menusRepository;
    }

    @Override
    public List<ProcesosAsigNoAsig> filtrarProcesos(Long id_menu, Long filtro) {
        // Verificar si el filtro es null o no tiene un valor válido (0, 1, 2)
        if (filtro == null || (filtro != 0 && filtro != 1 && filtro != 2)) {
            return new ArrayList<>(); // Devuelve una lista vacía
        }

        // Obtener el menu por su id_menu utilizando el repositorio
        Optional<Menus> menuOptional = menusRepository.findById(id_menu);

        // Lista que se devolverá según los parámetros que lleguen
        List<ProcesosAsigNoAsig> procesosAsigNoAsigList = new ArrayList<>();

        // Verificar si el menu existe
        if (menuOptional.isPresent()) {
            Menus menu = menuOptional.get();

            // Obtener todos los procesos
            List<Procesos> todosLosProcesos = procesosRepository.findAll();

            // Procesar cada proceso para determinar su asignación
            for (Procesos proceso : todosLosProcesos) {
                ProcesosAsigNoAsig procesoDto = new ProcesosAsigNoAsig();
                procesoDto.setId_proceso(proceso.getId_proceso());
                procesoDto.setNombre(proceso.getNombre());
                procesoDto.setEstado(proceso.getEstado());

                // Verificar si el proceso está asignado al menu
                boolean estaAsignado = menu.getProcesosList().stream()
                        .anyMatch(p -> p.getId_proceso().equals(proceso.getId_proceso()));

                procesoDto.setAsig(estaAsignado ? 1L : 0L); // 1 si está asignado, 0 si no

                // Aplicar el filtro
                if (filtro == 2) {
                    // Si el filtro es 2, se devuelve toda la lista
                    procesosAsigNoAsigList.add(procesoDto);
                } else if (filtro == 1 && estaAsignado) {
                    // Si el filtro es 1, se devuelven solo los procesos asignados al menu
                    procesosAsigNoAsigList.add(procesoDto);
                } else if (filtro == 0 && !estaAsignado) {
                    // Si el filtro es 0, se devuelven solo los procesos no asignados al menu
                    procesosAsigNoAsigList.add(procesoDto);
                }
            }
        }

        return procesosAsigNoAsigList;
    }

}
