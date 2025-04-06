package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.entitys.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AreasRepository extends JpaRepository<Areas, Long> {
    @Query(value = "SELECT * FROM areas WHERE " +
            "(:filtroNombre IS NULL OR areas.nombre ILIKE %:filtroNombre%)",
            nativeQuery = true)
    List<Areas> searchAreasByNombre(@Param("filtroNombre") String filtroNombre);
}
