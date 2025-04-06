package com.taller.bibliotecas.services;

import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.projections.classBased.CrearPrestamoDTO;
import com.taller.bibliotecas.projections.classBased.EjemplarDtoPPrestamos;
import com.taller.bibliotecas.projections.classBased.EjemplaresDto;
import com.taller.bibliotecas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MprestamoServiceImpl implements MPrestamoService{
    @Autowired
    private MPrestamoRepository mPrestamoRepository;

    @Autowired
    private DatosRepository datosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DetPrestamoRepository detPrestamoRepository;

    @Autowired
    private EjemplaresRepository ejemplaresRepository;

    @Autowired
    private  MDevolucionRepository mDevolucionRepository;


    @Override
    public MPrestamo createPrestamo(CrearPrestamoDTO prestamoDTO) {
        // 1. Obtener el "Datos" y "Usuarios" usando sus respectivos IDs
        Datos datos = datosRepository.findById(prestamoDTO.getId_dato())
                .orElseThrow(() -> new RuntimeException("Datos no encontrados"));
        Usuarios usuario = usuariosRepository.findById(prestamoDTO.getId_usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la entidad "MPrestamo"
        MPrestamo mprestamo = new MPrestamo();
        mprestamo.setFecha(prestamoDTO.getFecha());
        mprestamo.setFechaini(prestamoDTO.getFechaini());
        mprestamo.setFechafin(prestamoDTO.getFechafin());
        mprestamo.setTipopres(prestamoDTO.getTipopres());
        mprestamo.setEstado(prestamoDTO.getEstado());
        mprestamo.setRealiza(datos);
        mprestamo.setPresta(usuario);

        // 3. Guardar el "MPrestamo"
        MPrestamo savedPrestamo = mPrestamoRepository.save(mprestamo);

        // 4. Relacionar los ejemplares con el "MPrestamo"
        for (EjemplarDtoPPrestamos ejemplarDTO : prestamoDTO.getListEjemplares()) {
            Ejemplares ejemplar = ejemplaresRepository.findById(ejemplarDTO.getId_ejemplar())
                    .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

            // Crear la relación entre "MPrestamo" y "Ejemplares" (detaller de préstamo)
            DetPrestamo detPrestamo = new DetPrestamo();

            // Crear la clave compuesta utilizando el ID del préstamo y del ejemplar
            DetPrestamoPK detPrestamoPK = new DetPrestamoPK();
            detPrestamoPK.setId_mprestamo(savedPrestamo.getId_mprestamo());
            detPrestamoPK.setId_ejemplar(ejemplar.getId_ejemplar());
            detPrestamoPK.setEstado(1L); // Asumiendo que el estado es parte de la relación

            detPrestamo.setId_detPrestamoPK(detPrestamoPK);
            detPrestamo.setMprestamo(savedPrestamo);
            detPrestamo.setEjemplar(ejemplar);

            // Guardar la relación de "DetPrestamo" (muchos a muchos)
            detPrestamoRepository.save(detPrestamo);
        }


        List<Ejemplares> ejemplaresList = new ArrayList<>();

        MDevolucion mDevolucion = new MDevolucion();
        mDevolucion.setEstado(1L);
        mDevolucion.setMprestamo(mprestamo);
        for (EjemplarDtoPPrestamos ejemplarDTO : prestamoDTO.getListEjemplares()) {
            Ejemplares ejemplar = ejemplaresRepository.findById(ejemplarDTO.getId_ejemplar())
                    .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));
            ejemplaresList.add(ejemplar);
        }
        mDevolucion.setEjemplaresList(ejemplaresList);
        mDevolucionRepository.save(mDevolucion);


            return savedPrestamo;
    }

    @Override
    public List<MPrestamo> getPrestamosByIdPersona(Long id_persona) {
        return mPrestamoRepository.findByPersonaId(id_persona);
    }
}
