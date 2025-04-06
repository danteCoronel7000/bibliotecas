package com.taller.bibliotecas.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipotex")
public class TipoTex {

    @EmbeddedId
    private TipoTextPK idTipotex;

    @ManyToOne
    @MapsId("id_texto")
    @JoinColumn(name = "id_texto")
    Textos texto;

    @ManyToOne
    @MapsId("id_tipo")
    @JoinColumn(name = "id_tipo")
    Tipos tipo;
}
