# Banco de Preguntas — Taller Microkernel + Tuberías y Filtros

Taller de Laboratorio de Ingeniería de Software II. Aplicación Java SE de
escritorio (Swing) que implementa el **patrón arquitectónico Microkernel**
(Plug-in Architecture), con carga dinámica de plugins mediante **Reflexión**,
combinado con el **patrón Tuberías y Filtros** para la validación de
preguntas del banco.

## Arquitectura

```
QuestionMicrokernel (core)
 ├── Map<String, Question>            banco de preguntas
 ├── List<QuestionPlugin>             plugins cargados por REFLEXIÓN
 │    (leídos desde plugins.properties: plugin.1=..., plugin.2=..., plugin.3=...)
 │
 ├── MultipleChoiceQuestionPlugin ──▶ ejecuta el QuestionPipeline completo:
 │                                     ContentValidationFilter →
 │                                     OptionsValidationFilter →
 │                                     ClassificationFilter →
 │                                     CorrectAnswerValidationFilter
 ├── CaseQuestionPlugin               (validación propia, sin el pipeline completo)
 └── MultimediaQuestionPlugin         (validación propia: exige recurso multimedia)
```

El núcleo (`QuestionMicrokernel`) **nunca** usa `new` sobre ninguna clase
concreta de `plugins.*`: el nombre de cada clase se lee en tiempo de
ejecución desde `plugins.properties` y se instancia con
`Class.forName(...).getDeclaredConstructor().newInstance()`. Esto replica
exactamente la técnica vista en la teoría sobre el envío de paquetes a
distintos países (`delivery.<país> = clase`), aplicada aquí como
`plugin.<n> = clase`.

## Estructura de paquetes (la exigida por la guía del taller)

```
co.edu.unicauca.microkernel
 ├── app/              Main.java (punto de entrada), GUIMicrokernel.java (interfaz Swing)
 ├── common/
 │    ├── entities/    Question.java, QuestionRequest.java
 │    └── interfaces/  QuestionPlugin.java (contrato común de los plugins)
 ├── core/             QuestionMicrokernel.java (núcleo: Map + carga por reflexión)
 ├── pipeline/
 │    ├── base/        QuestionFilter.java (interfaz), QuestionPipeline.java
 │    └── filters/     ContentValidationFilter, OptionsValidationFilter,
 │                     ClassificationFilter, CorrectAnswerValidationFilter
 └── plugins/          MultipleChoiceQuestionPlugin, CaseQuestionPlugin,
                       MultimediaQuestionPlugin
```

## Cómo compilar y ejecutar

```bash
mvn clean package
java -jar target/banco-preguntas-microkernel.jar
```

Se abre la ventana principal: selecciona el tipo de pregunta, llena el
formulario y presiona "Generar pregunta". El tipo `MULTIPLE_CHOICE` pasa
por las 4 validaciones del pipeline (por ejemplo, la clasificación debe
ser una de: Arquitectura de software, Ingeniería de requisitos, Bases de
datos, Estructuras de datos, Redes de computadores, Matemáticas
discretas). El tipo `MULTIMEDIA` exige indicar una URL de recurso.

## Cómo correr las pruebas unitarias

```bash
mvn test
```

25 pruebas (JUnit 5) que cubren:
- Cada uno de los 4 filtros de forma aislada, y el pipeline completo (fail-fast).
- La carga de los 3 plugins por reflexión desde `plugins.properties`.
- `QuestionMicrokernel.executePlugin(...)`: generación exitosa, rechazo por
  validación fallida, y excepción cuando ningún plugin soporta el tipo solicitado.
- El plugin `MultimediaQuestionPlugin` (acepta/rechaza según el recurso multimedia).

## Extender el sistema con un plugin nuevo (demostración de extensibilidad)

Gracias al patrón Microkernel, agregar un plugin nuevo (por ejemplo,
`GeneradorPreguntasIA`) no requiere modificar `QuestionMicrokernel` ni
ninguna clase existente:

1. Crear la clase en el paquete `plugins`, implementando `QuestionPlugin`.
2. Agregar una línea en `plugins.properties`:
   `plugin.4=co.edu.unicauca.microkernel.plugins.GeneradorPreguntasIA`

Eso es todo — el núcleo la cargará automáticamente por reflexión en el
próximo arranque.
