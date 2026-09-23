package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del nucleo del Microkernel: verifican que la carga de
 * plugins por reflexion (a partir de plugins.properties, presente en
 * el classpath de pruebas) funciona correctamente, y que el nucleo
 * delega la generacion al plugin adecuado segun el tipo solicitado.
 */
class QuestionMicrokernelTest {

    private QuestionMicrokernel microkernel;

    @BeforeEach
    void configurar() {
        microkernel = new QuestionMicrokernel();
    }

    @Test
    void cargaLosTresPluginsRegistradosEnPluginsProperties() {
        assertEquals(3, microkernel.getPlugins().size(),
                "Deben cargarse por reflexion los 3 plugins listados en plugins.properties");
    }

    @Test
    void elBancoDePreguntasInicianVacio() {
        assertTrue(microkernel.getQuestions().isEmpty());
    }

    @Test
    void executePluginGeneraYAlmacenaUnaPreguntaValida() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta SOLID", "¿Que representa la S en SOLID?", "MULTIPLE_CHOICE",
                "Arquitectura de software",
                Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation"),
                "Single Responsibility");

        Question pregunta = microkernel.executePlugin("MULTIPLE_CHOICE", request);

        assertNotNull(pregunta);
        assertEquals(1, microkernel.getQuestions().size());
        assertTrue(microkernel.getQuestions().containsKey(pregunta.getId()));
    }

    @Test
    void executePluginRetornaNullSiLaValidacionDelPipelineFalla() {
        QuestionRequest requestInvalida = new QuestionRequest(
                "t", "c", "MULTIPLE_CHOICE", "clasificacion inexistente",
                Arrays.asList("A", "B", "C", "D"), "A");

        Question pregunta = microkernel.executePlugin("MULTIPLE_CHOICE", requestInvalida);

        assertNull(pregunta);
        assertTrue(microkernel.getQuestions().isEmpty(), "No debe agregarse al banco una pregunta invalida");
    }

    @Test
    void executePluginLanzaExcepcionSiNingunPluginSoportaElTipo() {
        QuestionRequest request = new QuestionRequest("t", "c", "TIPO_DESCONOCIDO",
                "Arquitectura de software", Arrays.asList("A", "B", "C", "D"), "A");

        assertThrows(IllegalArgumentException.class,
                () -> microkernel.executePlugin("TIPO_DESCONOCIDO", request));
    }

    @Test
    void generaPreguntaDeAnalisisDeCasoCorrectamente() {
        QuestionRequest request = new QuestionRequest(
                "Caso de estudio", "Una empresa enfrenta un problema de escalabilidad en su sistema monolitico.",
                "CASE_ANALYSIS", "Arquitectura de software", null, null);

        Question pregunta = microkernel.executePlugin("CASE_ANALYSIS", request);

        assertNotNull(pregunta);
        assertEquals("CASE_ANALYSIS", pregunta.getType());
    }

    @Test
    void reloadPluginsVuelveACargarLosPluginsSinDuplicarlos() {
        microkernel.reloadPlugins();
        assertEquals(3, microkernel.getPlugins().size());
    }
}
