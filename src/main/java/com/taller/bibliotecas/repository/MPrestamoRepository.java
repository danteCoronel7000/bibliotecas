package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.MPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MPrestamoRepository extends JpaRepository<MPrestamo, Long> {
    //para la aplicacion
    @Query("SELECT m FROM MPrestamo m WHERE m.realiza.persona.id_persona = :idPersona")
    List<MPrestamo> findByPersonaId(Long idPersona);
}
