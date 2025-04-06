package com.taller.bibliotecas.entitys;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
public class TipoTextPK implements Serializable {
    protected Long id_texto;
    protected Long id_tipo;
    protected String docum;//nombre que se le dara al documento
}
