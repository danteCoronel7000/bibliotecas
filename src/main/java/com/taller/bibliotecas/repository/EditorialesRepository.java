package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Areas;
import com.taller.bibliotecas.entitys.Editoriales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EditorialesRepository extends JpaRepository<Editoriales, Long> {
    @Query(value = "SELECT * FROM editoriales WHERE " +
            "(:filtroNombre IS NULL OR editoriales.nombre ILIKE %:filtroNombre%)",
            nativeQuery = true)
    List<Editoriales> searchEditorialesByNombre(@Param("filtroNombre") String filtroNombre);
}
