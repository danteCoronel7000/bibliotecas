package com.taller.bibliotecas.repository;

import com.taller.bibliotecas.entitys.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
