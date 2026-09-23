package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.util.UUID;

/**
 * Plugin que genera preguntas con recurso multimedia (imagen, audio o
 * video), correspondiente a GeneradorPreguntaMultimedia en el
 * enunciado del taller.
 *
 * Su validacion propia exige que la solicitud incluya la referencia al
 * recurso multimedia (QuestionRequest.getResourceUrl()).
 */
public class MultimediaQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "multimedia";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        System.out.println("Generando pregunta con recurso multimedia...");

        if (request.getResourceUrl() == null || request.getResourceUrl().trim().isEmpty()) {
            System.err.println("Debe indicar la URL o ruta del recurso multimedia (imagen, audio o video)");
            return null;
        }

        String contenidoConRecurso = request.getContent() + "\n[Recurso multimedia: " + request.getResourceUrl() + "]";

        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                contenidoConRecurso,
                request.getType()
        );
    }
}
