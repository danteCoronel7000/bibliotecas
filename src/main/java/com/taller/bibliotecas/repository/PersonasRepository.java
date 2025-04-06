package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Personas;
import com.taller.bibliotecas.projections.interfaceBased.closed.PersonasClosedView;
import com.taller.bibliotecas.projections.interfaceBased.open.PersonasOpenView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonasRepository extends JpaRepository<Personas, Long> {
    //closed view interface based
    List<PersonasClosedView> findBy();

    //open view interface based
    @Query("SELECT p FROM Personas p") // Consulta JPQL
    List<PersonasOpenView> findAllPersonas();

    @Query(
           value = "select * "
            +" from personas u "
            +" where 	u.id_persona=?1",
            nativeQuery = true
    )
    Personas obtenerPersonaPorId(Long id_persona);

    @Query(value = "SELECT * FROM personas WHERE " +
            "(:filtroNombre IS NULL OR personas.nombre ILIKE %:filtroNombre%) AND " +
            "(:filtroAp IS NULL OR personas.ap ILIKE %:filtroAp%) AND " +
            "(:filtroAm IS NULL OR personas.am ILIKE %:filtroAm%)",
            nativeQuery = true)
    List<Personas> searchPersonasNative(
            @Param("filtroNombre") String filtroNombre,
            @Param("filtroAp") String filtroAp,
            @Param("filtroAm") String filtroAm);


}
