package com.taller.bibliotecas.projections.interfaceBased.open;

import org.springframework.beans.factory.annotation.Value;

public interface PersonasOpenView {
    @Value("#{target.nombre +' '+ target.ap +' '+ target.am}")
    String getNombreAndAp();
}
