package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Contrato de un filtro de validacion dentro del pipeline. Cada filtro
 * es independiente y solo sabe validar UN aspecto de la solicitud
 * (Principio de Responsabilidad Unica), de modo que se pueden
 * reordenar, reutilizar en distintos plugins, o agregar nuevos filtros
 * sin afectar a los existentes.
 */
public interface QuestionFilter {

    /**
     * @param request la solicitud a validar
     * @return true si la solicitud pasa este filtro; false si falla.
     */
    boolean process(QuestionRequest request);
}
