package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tuberia (Pipeline) que coordina la ejecucion secuencial de los
 * filtros de validacion. Se detiene en el primer filtro que falle
 * ("fail fast"), lo cual evita ejecutar validaciones innecesarias y
 * permite informar exactamente en que paso fallo la solicitud.
 */
public class QuestionPipeline {

    private final List<QuestionFilter> filters = new ArrayList<>();

    public void addFilter(QuestionFilter filter) {
        filters.add(filter);
    }

    public boolean execute(QuestionRequest request) {
        for (QuestionFilter filter : filters) {
            if (!filter.process(request)) {
                System.err.println("La validacion fallo en el filtro: " + filter.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
