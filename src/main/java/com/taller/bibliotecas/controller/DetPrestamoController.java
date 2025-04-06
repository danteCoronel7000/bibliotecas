package com.taller.bibliotecas.controller;

import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.projections.classBased.DetPrestamoDTO;
import com.taller.bibliotecas.projections.classBased.EjemplarPrestamoById_prestamoDTO;
import com.taller.bibliotecas.projections.classBased.EjemplaresDto;
import com.taller.bibliotecas.repository.DetPrestamoRepository;
import com.taller.bibliotecas.repository.MDevolucionRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/detprestamo")
public class DetPrestamoController {
    @Autowired
    DetPrestamoRepository detPrestamoRepository;
    @Autowired
    MDevolucionRepository mDevolucionRepository;
    @Autowired
    UsuariosRepository usuariosRepository;

    @GetMapping(value = "all")
    public List<DetPrestamo> findAll(){
        return detPrestamoRepository.findAll();
    }

    @GetMapping(value = "allDto")
    public List<DetPrestamoDTO> allDetPrestamoDTo() {
        // Obtenemos los datos de la entidad dPrestamo (suponiendo que usas un repositorio)
        List<DetPrestamo> dPrestamos = detPrestamoRepository.findAll();

        // Mapeamos los datos de la entidad al DTO
        return dPrestamos.stream()
                .map(dPrestamo -> {
                    DetPrestamoDTO dto = new DetPrestamoDTO();
                    dto.setCodinv(dPrestamo.getEjemplar().getCodinv()); // Código del ejemplar
                    dto.setTitejemplar(dPrestamo.getEjemplar().getTexto().getTitulo()); // Título del texto
                    dto.setEstado(dPrestamo.getId_detPrestamoPK().getEstado()); // Estado de la llave compuesta
                    return dto;
                })
                .toList();
    }

    @GetMapping("/filtrarPorIdPrestamo/{id}")
    public ResponseEntity<List<DetPrestamoDTO>> getdetPrestamoByPreastamo(@PathVariable Long id) {
        // Buscar los detalles de préstamo por el id del préstamo
        List<DetPrestamo> detPrestamos = detPrestamoRepository.findByIdMprestamo(id);

        // Mapear los resultados a DetPrestamoDTO
        List<DetPrestamoDTO> detPrestamoDTOList = detPrestamos.stream()
                .map(d -> new DetPrestamoDTO(
                        d.getEjemplar().getCodinv(),  // Obtener el código del ejemplar
                        d.getEjemplar().getTexto().getTitulo(),  // Obtener el título del texto relacionado
                        d.getId_detPrestamoPK().getEstado()  // Obtener el estado del préstamo
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(detPrestamoDTOList);
    }



    @GetMapping("/{idmprestamo}")
    public List<EjemplarPrestamoById_prestamoDTO> getEjemplaresByMprestamo(@PathVariable Long idmprestamo) {
        return detPrestamoRepository.findEjemplaresByMprestamo(idmprestamo);
    }

    @PutMapping("/updateEstado/{idMprestamo}/{idEjemplar}/{idmdevolucion}/{idusuario}")
    public ResponseEntity<Void> actualizarEstado(@PathVariable Long idMprestamo,
                                                   @PathVariable Long idEjemplar,
                                                 @PathVariable Long idmdevolucion,
                                                 @PathVariable Long idusuario
    ) {
        try {
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            String fechaFormateada = fechaHoraActual.format(formatter);

            Optional<Usuarios> usuario = usuariosRepository.findById(idusuario);
            Optional<MDevolucion> mDevolucion = mDevolucionRepository.findById(idmdevolucion);

            if(mDevolucion.isPresent() && usuario.isPresent()){
                MDevolucion md = mDevolucion.get();
                Usuarios us = usuario.get();
                md.setFecha(fechaFormateada);
                md.setEstado(0L);
                md.setReserva(us);
            }

            // Lógica para actualizar estado
            detPrestamoRepository.actualizarEstado(idMprestamo, idEjemplar);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/updateEstadoDev/{idMprestamo}/{idEjemplar}/{idmdevolucion}/{idusuario}")
    public ResponseEntity<Void> actualizarEstadoAnulacionDev(@PathVariable Long idMprestamo,
                                                 @PathVariable Long idEjemplar,
                                                 @PathVariable Long idmdevolucion,
                                                 @PathVariable Long idusuario
    ) {
        try {
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            String fechaFormateada = fechaHoraActual.format(formatter);

            Optional<Usuarios> usuario = usuariosRepository.findById(idusuario);
            Optional<MDevolucion> mDevolucion = mDevolucionRepository.findById(idmdevolucion);

            if(mDevolucion.isPresent() && usuario.isPresent()){
                MDevolucion md = mDevolucion.get();
                Usuarios us = usuario.get();
                md.setFecha(fechaFormateada);
                md.setEstado(1L);
                md.setReserva(us);
            }

            // Lógica para actualizar estado
            detPrestamoRepository.actualizarEstadoAdev(idMprestamo, idEjemplar);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
