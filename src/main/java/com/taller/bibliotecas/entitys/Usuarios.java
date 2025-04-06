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
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @SequenceGenerator(
            name = "personas_secuence",
            sequenceName = "personas_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "personas_secuence"
    )
            @Column(name = "id_usuario")
    Long id_usuario;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column(name = "token")
    String token;
    @Column(name = "estado")
    Long estado; // Asigna un valor por defecto en la entidad

    @OneToOne
    @JoinColumn(
            name = "id_persona",
            referencedColumnName = "id_persona"
    )
    Personas persona;

    //relacion manyToMany con la entidad roles
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(
                    name = "id_usuario",
                    referencedColumnName = "id_usuario"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_rol",
                    referencedColumnName = "id_rol"
            )
    )
    private List<Roles> rolesList;
}

