package com.taller.bibliotecas.entitys;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_secuence",
            sequenceName = "image_secuence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_secuence"
    )
    @Column(name = "id")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String imageId;


    public Image(String name, String imageUrl, String imageId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }
}

