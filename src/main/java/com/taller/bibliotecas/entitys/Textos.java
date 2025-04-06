package com.taller.bibliotecas.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "textos")
public class Textos {
    @Id
    @SequenceGenerator(
            name = "textos_secuence",
            sequenceName = "textos_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "textos_secuence"
    )
    @Column(name = "id_texto")
    Long id_texto;
    @Column(nullable = false)
    String titulo;
    @Column(nullable = false)
    String resumen;
    @Column(name = "isbn")
    String isbn;
    @Column(name = "edicion")
    Long edicion; // Asigna un valor por defecto en la entidad
    @Column(name = "fechapub")
    String fechapub;
    @Column(name = "url")
    String url;
    @Column(name = "estado")
    Long estado;

    //relacion manyToMany con la entidad autores
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "textos_autores",
            joinColumns = @JoinColumn(
                    name = "id_texto",
                    referencedColumnName = "id_texto"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_autor",
                    referencedColumnName = "id_autor"
            )
    )
    private List<Autores> autoresList;

    @ManyToOne
    @JoinColumn(
            name = "id_area",
            referencedColumnName = "id_area"
    )
    private Areas area;

    @ManyToOne
    @JoinColumn(
            name = "id_editorial",
            referencedColumnName = "id_editorial"
    )
    private Editoriales editorial;

}
