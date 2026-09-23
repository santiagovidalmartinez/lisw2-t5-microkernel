package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.util.UUID;

/**
 * Plugin que genera preguntas de analisis de caso
 * (GeneradorPreguntaCaso en el enunciado del taller): presentan un
 * escenario o caso de estudio mas extenso que una pregunta directa.
 *
 * A diferencia de MultipleChoiceQuestionPlugin, este plugin NO ejecuta
 * el pipeline completo de 4 filtros (el taller solo exige integrarlo
 * en "al menos uno" de los plugins); en su lugar aplica una validacion
 * minima propia, suficiente para su tipo de contenido.
 */
public class CaseQuestionPlugin implements QuestionPlugin {

    private static final int LONGITUD_MINIMA_CASO = 40;

    @Override
    public String getName() {
        return "case-analysis";
    }

    @Override
    public boolean supports(String type) {
        return "CASE_ANALYSIS".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta de analisis de caso...");

        if (request.getContent() == null || request.getContent().trim().length() < LONGITUD_MINIMA_CASO) {
            System.err.println("El caso de estudio debe tener al menos " + LONGITUD_MINIMA_CASO + " caracteres");
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
