package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Editoriales;
import com.taller.bibliotecas.entitys.Tipos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TiposRepository extends JpaRepository<Tipos, Long> {
    @Query(value = "SELECT * FROM tipos WHERE " +
            "(:filtroNombre IS NULL OR tipos.nombre ILIKE %:filtroNombre%)",
            nativeQuery = true)
    List<Tipos> searchTiposByNombre(@Param("filtroNombre") String filtroNombre);
}
