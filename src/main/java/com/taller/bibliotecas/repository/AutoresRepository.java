package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Autores;
import com.taller.bibliotecas.entitys.Personas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutoresRepository extends JpaRepository<Autores, Long> {

    @Query(value = "SELECT * FROM autores WHERE " +
            "(:filtroNombre IS NULL OR autores.nombre ILIKE %:filtroNombre%) AND " +
            "(:filtroAp IS NULL OR autores.ap ILIKE %:filtroAp%) AND " +
            "(:filtroAm IS NULL OR autores.am ILIKE %:filtroAm%)",
            nativeQuery = true)
    List<Autores> searchAutoresNative(
            @Param("filtroNombre") String filtroNombre,
            @Param("filtroAp") String filtroAp,
            @Param("filtroAm") String filtroAm);

}
