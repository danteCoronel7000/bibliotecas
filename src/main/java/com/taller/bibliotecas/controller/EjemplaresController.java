package com.taller.bibliotecas.controller;

import com.taller.bibliotecas.entitys.*;
import com.taller.bibliotecas.projections.classBased.*;
import com.taller.bibliotecas.repository.AutoresRepository;
import com.taller.bibliotecas.repository.EjemplaresRepository;
import com.taller.bibliotecas.repository.TextosRepository;
import com.taller.bibliotecas.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ejemplares")
public class EjemplaresController {

    @Autowired
    EjemplaresRepository ejemplaresRepository;
    @Autowired
    TextosRepository textosRepository;
    @Autowired
    UsuariosRepository usuariosRepository;

    @GetMapping(value = "all")
    public List<Ejemplares> findAll(){
        return ejemplaresRepository.findAll();
    }

    @GetMapping("/filtrarPorIdTexto/{id}")
    public ResponseEntity<List<EjemplaresDto>> getEjemplaresByTexto(@PathVariable Long id) {
        List<Ejemplares> ejemplares = ejemplaresRepository.findEjemplaresByTextoId(id);
        // Mapear a EjemplaresDto
        List<EjemplaresDto> ejemplaresDtoList = ejemplares.stream()
                .map(e -> new EjemplaresDto(
                        e.getId_ejemplar(),
                        e.getCodinv(),
                        e.getDisponible(),
                        e.getEstado()
                )).toList();
        return ResponseEntity.ok(ejemplaresDtoList);
    }


    @PostMapping(value = "/crear")
    public ResponseEntity<Ejemplares> createEjemplar(@RequestBody CrearEjemplarDTO dto) {
        Ejemplares ejemplar = dto.getEjemplar();
        Long id_texto = dto.getId_texto();
        Long id_usuario = dto.getId_usuario();

        // Verificar si el codinv ya existe
        boolean codinvExiste = ejemplaresRepository.existsByCodinv(dto.getCodinv());
        if (codinvExiste) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        // Buscar el texto relacionado por su ID
        Optional<Textos> textoOptional = textosRepository.findById(id_texto);
        Optional<Usuarios> usuarioOptional = usuariosRepository.findById(id_usuario);
        if (textoOptional.isPresent() && usuarioOptional.isPresent()) {
            // Asignar el texto al ejemplar
            ejemplar.setTexto(textoOptional.get());
            ejemplar.setRegistra(usuarioOptional.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si el texto no existe
        }

        // Guardar el ejemplar con la relación establecida
        Ejemplares createdEjemplar = ejemplaresRepository.save(ejemplar);
        return new ResponseEntity<>(createdEjemplar, HttpStatus.CREATED);
    }



    @GetMapping("/obtenerSiguienteId")
    public ResponseEntity<Long> obtenerSiguienteId() {
        try {
            Long siguienteId = ejemplaresRepository.obtenerSiguienteId();
            return ResponseEntity.ok(siguienteId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/obtenerPorId/{id}")
    public ResponseEntity<EjemplaresDto> autoresEjemplarPorId(@PathVariable Long id) {
        Optional<Ejemplares> ejemplarOptional = ejemplaresRepository.findById(id);

        if (ejemplarOptional.isPresent()) {
            Ejemplares ejemplar = ejemplarOptional.get();
            EjemplaresDto ejemplarDto = new EjemplaresDto(
                    ejemplar.getId_ejemplar(),
                    ejemplar.getCodinv(),
                    ejemplar.getDisponible(),
                    ejemplar.getEstado()
            );

            return ResponseEntity.ok(ejemplarDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // O bien puedes devolver un mensaje de error
        }
    }

    @GetMapping(value = "allDto")
    public List<EjemplarDtoPPrestamos> allDetPrestamoDTo() {
        // Obtenemos los datos de la entidad dPrestamo (suponiendo que usas un repositorio)
        List<Ejemplares> ejemplares = ejemplaresRepository.findAll();

        // Mapeamos los datos de la entidad al DTO
        return ejemplares.stream()
                .map(ejemplar -> {
                    EjemplarDtoPPrestamos dto = new EjemplarDtoPPrestamos();
                    dto.setId_ejemplar(ejemplar.getId_ejemplar());
                    dto.setCodinv(ejemplar.getCodinv());
                    // Verificamos si el objeto Texto no es nulo antes de acceder a sus métodos
                    if (ejemplar.getTexto() != null) {
                        dto.setTitulo(ejemplar.getTexto().getTitulo());
                    } else {
                        // Manejo de caso en el que el Texto sea nulo, por ejemplo, asignar un valor por defecto
                        dto.setTitulo("texto sin titulo");
                    }

                    dto.setDisponible(ejemplar.getDisponible());
                    dto.setEstado((ejemplar.getEstado()));
                    return dto;
                })
                .toList();
    }


    /*
     * Modifica el atributo id_ejemplar de un ejemplar existente.
     *
     * @param currentId El ID actual del ejemplar que se quiere modificar.
     * @param newId     El nuevo ID que se quiere asignar.
     * @return ResponseEntity indicando el resultado de la operación.
     */

    @PutMapping("/modificarIdEjemplar")
    public ResponseEntity<Ejemplares> modificarIdEjemplar(@RequestBody ModificarIdEjemplarDTO dto) {

            // Buscar el ejemplar por su ID actual
            Optional<Ejemplares> ejemplarOptional = ejemplaresRepository.findById(dto.getId_ejemplar());

        // Verificar si el codinv ya existe
        boolean codinvExiste = ejemplaresRepository.existsByCodinv(dto.getNewcodinv());
        if (codinvExiste) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

            if (ejemplarOptional.isPresent()) {
                // Obtener la instancia del ejemplar y actualizar su ID
                Ejemplares ejemplar = ejemplarOptional.get();
                ejemplar.setCodinv(dto.getNewcodinv());

                // Guardar los cambios
                ejemplaresRepository.save(ejemplar);

                return ResponseEntity.ok(ejemplar);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // O bien puedes devolver un mensaje de error
            }
    }

    @PostMapping("/estado")
    public ResponseEntity<Ejemplares> deleteEjemplar(@RequestBody Map<String, Long> payload) {
        Long id_ejemplar = payload.get("id_ejemplar"); // Extrae id_texto del cuerpo de la solicitud

        Ejemplares ejemplar = ejemplaresRepository.findById(id_ejemplar)
                .orElseThrow(() -> new RuntimeException("Texto no encontrado con id: " + id_ejemplar));

        // Actualizar el estado del texto
        ejemplar.setEstado(0L);

        // Guardar los cambios en la base de datos
        ejemplaresRepository.save(ejemplar);

        return ResponseEntity.ok(ejemplar);
    }

    @PostMapping("/habilitar")
    public ResponseEntity<Ejemplares> habilitarEjemplar(@RequestBody Map<String, Long> payload) {
        Long id_ejemplar = payload.get("id_ejemplar"); // Extrae id_texto del cuerpo de la solicitud

                Ejemplares ejemplar = ejemplaresRepository.findById(id_ejemplar)
                .orElseThrow(() -> new RuntimeException("Texto no encontrado con id: " + id_ejemplar));

        // Actualizar el estado del texto
        ejemplar.setEstado(1L);
        
        ejemplaresRepository.save(ejemplar);
        return ResponseEntity.ok(ejemplar);
    }
}
