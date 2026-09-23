package co.edu.unicauca.microkernel.common.entities;

/**
 * Entidad que representa una pregunta ya generada y almacenada en el
 * banco de preguntas (dentro del Map de QuestionMicrokernel).
 *
 * Se mantiene deliberadamente simple (tal como lo plantea la guia del
 * taller): el nucleo (Microkernel) solo necesita conocer estos cuatro
 * datos para almacenar la pregunta; el detalle especifico de cada tipo
 * de pregunta (opciones, clasificacion, respuesta correcta, recurso
 * multimedia, etc.) vive en QuestionRequest, que es lo que cada plugin
 * recibe para generar la Question final.
 */
public class Question {

    private String id;
    private String title;
    private String content;
    private String type;

    public Question(String id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return "[" + type + "] " + id + " - " + title;
    }
}
