package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Ejemplares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EjemplaresRepository extends JpaRepository<Ejemplares,Long> {


    @Query(value = "SELECT * FROM ejemplares WHERE id_texto = :idTexto", nativeQuery = true)
    List<Ejemplares> findEjemplaresByTextoId(@Param("idTexto") Long idTexto);

    @Query(value = "SELECT COALESCE(MAX(id_ejemplar), 0) + 1 FROM ejemplares", nativeQuery = true)
    Long obtenerSiguienteId();

    @Query(value = "SELECT COUNT(*) > 0 FROM ejemplares WHERE codinv = :codinv", nativeQuery = true)
    boolean existsByCodinv(@Param("codinv") Long codinv);
}
