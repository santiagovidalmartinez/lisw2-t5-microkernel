package co.edu.unicauca.microkernel.pipeline;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.CorrectAnswerValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.OptionsValidationFilter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de cada filtro de forma aislada y del pipeline completo
 * (patron Tuberias y Filtros).
 */
class QuestionPipelineTest {

    private static final List<String> OPCIONES_VALIDAS =
            Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    private QuestionRequest requestValida() {
        return new QuestionRequest(
                "Pregunta SOLID", "¿Que representa la S en SOLID?", "MULTIPLE_CHOICE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Single Responsibility");
    }

    // ---- ContentValidationFilter ----

    @Test
    void contentValidationFilterAceptaTituloYContenidoNoVacios() {
        assertTrue(new ContentValidationFilter().process(requestValida()));
    }

    @Test
    void contentValidationFilterRechazaTituloVacio() {
        QuestionRequest request = new QuestionRequest("", "contenido", "MULTIPLE_CHOICE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(new ContentValidationFilter().process(request));
    }

    @Test
    void contentValidationFilterRechazaContenidoNulo() {
        QuestionRequest request = new QuestionRequest("titulo", null, "MULTIPLE_CHOICE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(new ContentValidationFilter().process(request));
    }

    // ---- OptionsValidationFilter ----

    @Test
    void optionsValidationFilterAceptaExactamenteCuatroOpciones() {
        assertTrue(new OptionsValidationFilter().process(requestValida()));
    }

    @Test
    void optionsValidationFilterRechazaMenosDeCuatroOpciones() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "Arquitectura de software", Arrays.asList("A", "B"), "A");
        assertFalse(new OptionsValidationFilter().process(request));
    }

    @Test
    void optionsValidationFilterRechazaListaNula() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "Arquitectura de software", null, "A");
        assertFalse(new OptionsValidationFilter().process(request));
    }

    @Test
    void optionsValidationFilterRechazaOpcionVacia() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "Arquitectura de software", Arrays.asList("A", "", "C", "D"), "A");
        assertFalse(new OptionsValidationFilter().process(request));
    }

    // ---- ClassificationFilter ----

    @Test
    void classificationFilterAceptaClasificacionReconocida() {
        assertTrue(new ClassificationFilter().process(requestValida()));
    }

    @Test
    void classificationFilterRechazaClasificacionNoReconocida() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "Clasificacion inventada", OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(new ClassificationFilter().process(request));
    }

    @Test
    void classificationFilterEsInsensibleAMayusculasYMinusculas() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "ARQUITECTURA DE SOFTWARE", OPCIONES_VALIDAS, "Single Responsibility");
        assertTrue(new ClassificationFilter().process(request));
    }

    // ---- CorrectAnswerValidationFilter ----

    @Test
    void correctAnswerValidationFilterAceptaRespuestaDentroDeLasOpciones() {
        assertTrue(new CorrectAnswerValidationFilter().process(requestValida()));
    }

    @Test
    void correctAnswerValidationFilterRechazaRespuestaFueraDeLasOpciones() {
        QuestionRequest request = new QuestionRequest("t", "c", "MULTIPLE_CHOICE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Respuesta que no esta en las opciones");
        assertFalse(new CorrectAnswerValidationFilter().process(request));
    }

    // ---- QuestionPipeline (integracion de los 4 filtros) ----

    private QuestionPipeline pipelineCompleto() {
        QuestionPipeline pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
        return pipeline;
    }

    @Test
    void pipelineCompletoAceptaUnaSolicitudTotalmenteValida() {
        assertTrue(pipelineCompleto().execute(requestValida()));
    }

    @Test
    void pipelineCompletoRechazaSiFallaCualquierFiltro() {
        QuestionRequest requestConClasificacionInvalida = new QuestionRequest(
                "t", "c", "MULTIPLE_CHOICE", "clasificacion inventada", OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(pipelineCompleto().execute(requestConClasificacionInvalida));
    }

    @Test
    void pipelineCompletoSeDetieneEnElPrimerFiltroQueFalla() {
        // Titulo vacio (falla ContentValidationFilter) Y clasificacion invalida (fallaria ClassificationFilter);
        // el pipeline debe detenerse en el primer filtro (fail fast) y retornar false.
        QuestionRequest requestConDobleFallo = new QuestionRequest(
                "", "c", "MULTIPLE_CHOICE", "clasificacion inventada", Collections.emptyList(), "X");
        assertFalse(pipelineCompleto().execute(requestConDobleFallo));
    }
}
