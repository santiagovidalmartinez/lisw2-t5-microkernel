package co.edu.unicauca.microkernel.common.interfaces;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Contrato comun que todo plugin del microkernel debe cumplir.
 *
 * QuestionMicrokernel (el nucleo) solo conoce esta interfaz, nunca las
 * clases concretas de los plugins: es lo que permite que el nucleo NO
 * necesite modificarse cada vez que se agrega un nuevo tipo de
 * pregunta (Principio de Inversion de Dependencias aplicado al patron
 * Microkernel).
 */
public interface QuestionPlugin {

    /** Nombre identificador del plugin (para fines de registro/logging). */
    String getName();

    /** Indica si este plugin sabe generar preguntas del tipo indicado. */
    boolean supports(String type);

    /** Genera la pregunta a partir de la solicitud, o null si la validacion falla. */
    Question generate(QuestionRequest request);
}
