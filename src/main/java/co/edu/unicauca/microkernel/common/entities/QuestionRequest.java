package co.edu.unicauca.microkernel.common.entities;

import java.util.List;

/**
 * Objeto de solicitud que transporta los datos capturados en la
 * interfaz grafica hacia el plugin que va a generar la pregunta.
 *
 * Incluye el campo opcional "resourceUrl" (no exigido explicitamente
 * por la guia, pero necesario para que GeneradorPreguntaMultimedia
 * pueda recibir la referencia al recurso multimedia -imagen, audio o
 * video- sin romper el contrato de 6 argumentos que usan los demas
 * plugins).
 */
public class QuestionRequest {

    private String title;
    private String content;
    private String type;
    private String classification;
    private List<String> options;
    private String correctAnswer;
    private String resourceUrl;

    public QuestionRequest(String title, String content, String type, String classification,
                            List<String> options, String correctAnswer) {
        this(title, content, type, classification, options, correctAnswer, null);
    }

    public QuestionRequest(String title, String content, String type, String classification,
                            List<String> options, String correctAnswer, String resourceUrl) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.classification = classification;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.resourceUrl = resourceUrl;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getType() { return type; }
    public String getClassification() { return classification; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getResourceUrl() { return resourceUrl; }
}
