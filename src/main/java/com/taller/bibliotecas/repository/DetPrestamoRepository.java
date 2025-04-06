package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.DetPrestamo;
import com.taller.bibliotecas.entitys.Ejemplares;
import com.taller.bibliotecas.projections.classBased.EjemplarPrestamoById_prestamoDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetPrestamoRepository extends JpaRepository<DetPrestamo, Long> {

    @Query(value = "SELECT * FROM detprestamo WHERE id_mprestamo = :id", nativeQuery = true)
    List<DetPrestamo> findByIdMprestamo(@Param("id") Long id);


    @Query("""
        SELECT new com.taller.bibliotecas.projections.classBased.EjemplarPrestamoById_prestamoDTO(
            e.id_ejemplar, 
            e.codinv, 
            d.id_detPrestamoPK.estado, 
            t.titulo
        )
        FROM DetPrestamo d
        JOIN d.ejemplar e
        JOIN e.texto t
        WHERE d.id_detPrestamoPK.id_mprestamo = :idMprestamo
    """)
    List<EjemplarPrestamoById_prestamoDTO> findEjemplaresByMprestamo(Long idMprestamo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE detprestamo dp " +
            "SET estado = 0 " +
            "WHERE dp.id_mprestamo = :idMprestamo " +
            "AND dp.id_ejemplar = :idEjemplar", nativeQuery = true)
    void actualizarEstado(@Param("idMprestamo") Long idMprestamo,
                          @Param("idEjemplar") Long idEjemplar);

    //metodo anular devolucion actualiza el valor estado de la entidad dprestamo
    @Modifying
    @Transactional
    @Query(value = "UPDATE detprestamo dp " +
            "SET estado = 1 " +
            "WHERE dp.id_mprestamo = :idMprestamo " +
            "AND dp.id_ejemplar = :idEjemplar", nativeQuery = true)
    void actualizarEstadoAdev(@Param("idMprestamo") Long idMprestamo,
                          @Param("idEjemplar") Long idEjemplar);
}
