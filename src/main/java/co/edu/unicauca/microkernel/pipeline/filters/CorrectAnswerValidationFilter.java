package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/** Valida que la respuesta correcta este presente dentro de la lista de opciones. */
public class CorrectAnswerValidationFilter implements QuestionFilter {

    @Override
    public boolean process(QuestionRequest request) {
        if (request.getCorrectAnswer() == null || request.getCorrectAnswer().trim().isEmpty()) {
            return false;
        }
        if (request.getOptions() == null) {
            return false;
        }
        return request.getOptions().contains(request.getCorrectAnswer());
    }
}
