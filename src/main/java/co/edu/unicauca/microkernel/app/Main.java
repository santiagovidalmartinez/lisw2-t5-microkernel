package co.edu.unicauca.microkernel.app;

import co.edu.unicauca.microkernel.core.QuestionMicrokernel;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicacion. Crea el nucleo (que carga los
 * plugins por Reflexion a partir de plugins.properties) y arranca la
 * interfaz grafica de escritorio (Swing) requerida por el taller.
 */
public class Main {

    public static void main(String[] args) {
        QuestionMicrokernel microkernel = new QuestionMicrokernel();

        SwingUtilities.invokeLater(() -> {
            GUIMicrokernel ventana = new GUIMicrokernel(microkernel);
            ventana.setVisible(true);
        });
    }
}
