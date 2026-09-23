package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/** Valida que existan exactamente 4 opciones de respuesta, todas con contenido. */
public class OptionsValidationFilter implements QuestionFilter {

    private static final int NUMERO_OPCIONES_ESPERADO = 4;

    @Override
    public boolean process(QuestionRequest request) {
        if (request.getOptions() == null || request.getOptions().size() != NUMERO_OPCIONES_ESPERADO) {
            return false;
        }
        return request.getOptions().stream()
                .allMatch(opcion -> opcion != null && !opcion.trim().isEmpty());
    }
}
