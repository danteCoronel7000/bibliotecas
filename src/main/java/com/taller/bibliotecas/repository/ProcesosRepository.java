package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Procesos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcesosRepository extends JpaRepository<Procesos, Long> {
}
