package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

import java.util.Set;

/**
 * Valida que la clasificacion (area de conocimiento) de la pregunta
 * sea una de las reconocidas por el banco de preguntas.
 * Ejemplo: "Arquitectura de software".
 */
public class ClassificationFilter implements QuestionFilter {

    private static final Set<String> CLASIFICACIONES_VALIDAS = Set.of(
            "arquitectura de software",
            "ingenieria de requisitos",
            "bases de datos",
            "estructuras de datos",
            "redes de computadores",
            "matematicas discretas"
    );

    @Override
    public boolean process(QuestionRequest request) {
        String clasificacion = request.getClassification();
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            return false;
        }
        return CLASIFICACIONES_VALIDAS.contains(clasificacion.trim().toLowerCase());
    }
}
