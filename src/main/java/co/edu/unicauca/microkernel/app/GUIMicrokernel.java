package co.edu.unicauca.microkernel.app;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.core.QuestionMicrokernel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Interfaz de escritorio (Swing) requerida por el taller. Permite
 * seleccionar el tipo de pregunta a generar, diligenciar el formulario
 * correspondiente y ejecutar el plugin adecuado a traves del nucleo
 * (QuestionMicrokernel), sin que esta clase sepa nada de los plugins
 * concretos: solo conoce QuestionMicrokernel y le delega la ejecucion.
 */
public class GUIMicrokernel extends JFrame {

    private final QuestionMicrokernel microkernel;

    private final JComboBox<String> comboTipo = new JComboBox<>(
            new String[]{"MULTIPLE_CHOICE", "CASE_ANALYSIS", "MULTIMEDIA"});
    private final JTextField campoTitulo = new JTextField();
    private final JTextArea campoContenido = new JTextArea(3, 20);
    private final JTextField campoClasificacion = new JTextField();
    private final JTextField[] campoOpciones = new JTextField[4];
    private final JTextField campoRespuestaCorrecta = new JTextField();
    private final JTextField campoRecursoMultimedia = new JTextField();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Id", "Tipo", "Titulo", "Contenido"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }
    };

    public GUIMicrokernel(QuestionMicrokernel microkernel) {
        super("Banco de Preguntas Saber Pro - Microkernel de Plugins");
        this.microkernel = microkernel;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(construirPanelFormulario(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);

        refrescarTabla();
    }

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generar nueva pregunta (via plugin del Microkernel)"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        c.gridx = 0; c.gridy = fila; panel.add(new JLabel("Tipo de pregunta:"), c);
        c.gridx = 1; c.gridwidth = 3; panel.add(comboTipo, c); c.gridwidth = 1;
        fila++;

        c.gridx = 0; c.gridy = fila; panel.add(new JLabel("Titulo:"), c);
        c.gridx = 1; c.gridwidth = 3; panel.add(campoTitulo, c); c.gridwidth = 1;
        fila++;

        campoContenido.setLineWrap(true);
        campoContenido.setWrapStyleWord(true);
        c.gridx = 0; c.gridy = fila; c.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Contenido:"), c);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1; c.gridwidth = 3; panel.add(new JScrollPane(campoContenido), c); c.gridwidth = 1;
        fila++;

        c.gridx = 0; c.gridy = fila; panel.add(new JLabel("Clasificacion:"), c);
        c.gridx = 1; c.gridwidth = 3; panel.add(campoClasificacion, c); c.gridwidth = 1;
        JLabel ayudaClasificacion = new JLabel("ej. Arquitectura de software (requerido para MULTIPLE_CHOICE)");
        ayudaClasificacion.setFont(ayudaClasificacion.getFont().deriveFont(10f));
        fila++;
        c.gridx = 1; c.gridy = fila; c.gridwidth = 3; panel.add(ayudaClasificacion, c); c.gridwidth = 1;
        fila++;

        String[] etiquetasOpciones = {"Opcion A:", "Opcion B:", "Opcion C:", "Opcion D:"};
        for (int i = 0; i < 4; i++) {
            campoOpciones[i] = new JTextField();
            c.gridx = 0; c.gridy = fila; panel.add(new JLabel(etiquetasOpciones[i]), c);
            c.gridx = 1; c.gridwidth = 3; panel.add(campoOpciones[i], c); c.gridwidth = 1;
            fila++;
        }

        c.gridx = 0; c.gridy = fila; panel.add(new JLabel("Respuesta correcta:"), c);
        c.gridx = 1; c.gridwidth = 3; panel.add(campoRespuestaCorrecta, c); c.gridwidth = 1;
        fila++;

        c.gridx = 0; c.gridy = fila; panel.add(new JLabel("Recurso multimedia (URL):"), c);
        c.gridx = 1; c.gridwidth = 3; panel.add(campoRecursoMultimedia, c); c.gridwidth = 1;
        JLabel ayudaMultimedia = new JLabel("requerido solo para el tipo MULTIMEDIA");
        ayudaMultimedia.setFont(ayudaMultimedia.getFont().deriveFont(10f));
        fila++;
        c.gridx = 1; c.gridy = fila; c.gridwidth = 3; panel.add(ayudaMultimedia, c); c.gridwidth = 1;
        fila++;

        JButton botonGenerar = new JButton("Generar pregunta");
        botonGenerar.addActionListener(e -> generarPregunta());
        c.gridx = 0; c.gridy = fila; c.gridwidth = 4;
        panel.add(botonGenerar, c);

        return panel;
    }

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 10, 10, 10));
        JLabel titulo = new JLabel("Banco de preguntas (Map<String, Question> del nucleo)");
        titulo.setBorder(new EmptyBorder(6, 4, 6, 4));
        panel.add(titulo, BorderLayout.NORTH);

        JTable tabla = new JTable(modeloTabla);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void generarPregunta() {
        String tipo = (String) comboTipo.getSelectedItem();
        List<String> opciones = Arrays.stream(campoOpciones)
                .map(JTextField::getText)
                .toList();

        QuestionRequest request = new QuestionRequest(
                campoTitulo.getText(),
                campoContenido.getText(),
                tipo,
                campoClasificacion.getText(),
                opciones,
                campoRespuestaCorrecta.getText(),
                campoRecursoMultimedia.getText().isBlank() ? null : campoRecursoMultimedia.getText()
        );

        try {
            Question pregunta = microkernel.executePlugin(tipo, request);
            if (pregunta == null) {
                JOptionPane.showMessageDialog(this,
                        "La solicitud no paso la validacion del plugin (revise la consola para el detalle "
                                + "del filtro que fallo, o los campos obligatorios de este tipo de pregunta).",
                        "Validacion fallida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Pregunta generada correctamente con id:\n" + pregunta.getId(),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Ningun plugin disponible", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Question pregunta : microkernel.getQuestions().values()) {
            modeloTabla.addRow(new Object[]{
                    pregunta.getId(), pregunta.getType(), pregunta.getTitle(), pregunta.getContent()
            });
        }
    }
}
