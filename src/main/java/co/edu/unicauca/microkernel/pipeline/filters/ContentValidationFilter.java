package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/** Valida que el titulo y el contenido de la pregunta no esten vacios. */
public class ContentValidationFilter implements QuestionFilter {

    @Override
    public boolean process(QuestionRequest request) {
        return request.getTitle() != null && !request.getTitle().trim().isEmpty()
                && request.getContent() != null && !request.getContent().trim().isEmpty();
    }
}
