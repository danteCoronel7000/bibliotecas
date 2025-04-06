package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Datos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosRepository extends JpaRepository<Datos, Long>{
}
