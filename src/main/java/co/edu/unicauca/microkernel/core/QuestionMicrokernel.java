package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Nucleo central del patron Microkernel (Plug-in Architecture).
 *
 * Responsabilidades del nucleo (segun el taller):
 *   1) Almacenar el banco de preguntas (Map&lt;String, Question&gt;).
 *   2) Registrar los plugins leyendo plugins.properties.
 *   3) Ejecutar el plugin adecuado segun el tipo de pregunta solicitado.
 *   4) Gestionar el ciclo de vida de los plugins (cargarlos una sola
 *      vez al iniciar, y permitir recargarlos en caliente con reloadPlugins()).
 *
 * La carga de plugins usa REFLEXION de forma obligatoria (Class.forName +
 * newInstance), replicando exactamente la tecnica vista en la teoria
 * sobre el envio de paquetes a distintos paises (DeliveryPluginManager /
 * plugin.properties con el formato "delivery.&lt;pais&gt; = clase").
 * Aqui el equivalente es "plugin.&lt;n&gt; = clase" en plugins.properties.
 *
 * Gracias a la reflexion, este nucleo NUNCA necesita un "new" explicito
 * sobre ninguna clase de plugins.* : el nombre de la clase concreta se
 * lee en tiempo de ejecucion desde el archivo de configuracion, por lo
 * que se pueden agregar plugins nuevos sin recompilar ni modificar
 * QuestionMicrokernel (extensibilidad real del patron Microkernel).
 */
public class QuestionMicrokernel {

    private static final String ARCHIVO_CONFIGURACION = "plugins.properties";

    private final Map<String, Question> questions = new HashMap<>();
    private final List<QuestionPlugin> plugins = new ArrayList<>();

    public QuestionMicrokernel() {
        loadPlugins();
    }

    /**
     * Carga dinamicamente los plugins registrados en plugins.properties
     * usando Reflexion. Es el corazon del patron Microkernel: el nucleo
     * no conoce en tiempo de compilacion cuales plugins existen.
     */
    private void loadPlugins() {
        Properties propiedades = new Properties();

        try (InputStream entrada = getClass().getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                System.err.println("No se encontro el archivo " + ARCHIVO_CONFIGURACION);
                return;
            }
            propiedades.load(entrada);

            for (String clave : propiedades.stringPropertyNames()) {
                String nombreClase = propiedades.getProperty(clave);
                try {
                    // ---- Uso obligatorio de Reflexion ----
                    Class<?> clase = Class.forName(nombreClase);
                    Object instancia = clase.getDeclaredConstructor().newInstance();

                    if (instancia instanceof QuestionPlugin plugin) {
                        plugins.add(plugin);
                        System.out.println("Plugin cargado por reflexion: " + nombreClase
                                + " (" + plugin.getName() + ")");
                    } else {
                        System.err.println("La clase " + nombreClase + " no implementa QuestionPlugin");
                    }
                } catch (ReflectiveOperationException ex) {
                    System.err.println("Error al instanciar el plugin '" + nombreClase
                            + "' via reflexion: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar " + ARCHIVO_CONFIGURACION + ": " + ex.getMessage());
        }
    }

    /** Permite recargar los plugins en caliente (ciclo de vida del microkernel). */
    public void reloadPlugins() {
        plugins.clear();
        loadPlugins();
    }

    /**
     * Busca, entre los plugins cargados, el primero que soporte el tipo
     * solicitado, y le delega la generacion de la pregunta. Si la
     * pregunta se genera correctamente, se agrega al banco (Map).
     *
     * @throws IllegalArgumentException si ningun plugin soporta el tipo solicitado.
     */
    public Question executePlugin(String type, QuestionRequest request) {
        for (QuestionPlugin plugin : plugins) {
            if (plugin.supports(type)) {
                Question question = plugin.generate(request);
                if (question != null) {
                    questions.put(question.getId(), question);
                    System.out.println("Pregunta agregada exitosamente al banco con ID: " + question.getId());
                }
                return question;
            }
        }
        throw new IllegalArgumentException("No hay ningun plugin registrado que soporte el tipo: " + type);
    }

    public Map<String, Question> getQuestions() {
        return questions;
    }

    /** Lista los plugins actualmente cargados (para mostrarlos, por ejemplo, en el combo de la GUI). */
    public List<QuestionPlugin> getPlugins() {
        return plugins;
    }
}
