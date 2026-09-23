package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.CorrectAnswerValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.OptionsValidationFilter;

import java.util.UUID;

/**
 * Plugin que genera preguntas de seleccion multiple
 * (GeneradorPreguntaSeleccionMultiple en el enunciado del taller).
 *
 * Este es el plugin que integra OBLIGATORIAMENTE el pipeline completo
 * de Tuberias y Filtros: antes de generar la pregunta, ejecuta los
 * cuatro filtros de validacion en orden. Si cualquiera falla, no se
 * genera la pregunta (se retorna null y el nucleo no la agrega al banco).
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    private final QuestionPipeline pipeline;

    public MultipleChoiceQuestionPlugin() {
        pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
    }

    @Override
    public String getName() {
        return "multiple-choice";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta de seleccion multiple...");

        // Ejecucion obligatoria del pipeline de validacion antes de generar la pregunta
        if (!pipeline.execute(request)) {
            return null;
        }

        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
