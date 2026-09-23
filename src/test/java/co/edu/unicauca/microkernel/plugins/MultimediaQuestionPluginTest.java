package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultimediaQuestionPluginTest {

    private final MultimediaQuestionPlugin plugin = new MultimediaQuestionPlugin();

    @Test
    void supportsAceptaSoloElTipoMultimedia() {
        assertTrue(plugin.supports("MULTIMEDIA"));
        assertTrue(plugin.supports("multimedia")); // insensible a mayusculas/minusculas
        assertFalse(plugin.supports("MULTIPLE_CHOICE"));
    }

    @Test
    void generateRechazaSolicitudSinRecursoMultimedia() {
        QuestionRequest request = new QuestionRequest(
                "t", "c", "MULTIMEDIA", "Redes de computadores", null, null, null);
        assertNull(plugin.generate(request));
    }

    @Test
    void generateAceptaSolicitudConRecursoMultimedia() {
        QuestionRequest request = new QuestionRequest(
                "t", "c", "MULTIMEDIA", "Redes de computadores", null, null,
                "https://ejemplo.com/imagen.png");

        Question pregunta = plugin.generate(request);

        assertNotNull(pregunta);
        assertTrue(pregunta.getContent().contains("https://ejemplo.com/imagen.png"));
    }
}
