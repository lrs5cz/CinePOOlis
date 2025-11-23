import javax.swing.*;

public class ThreadBarra implements Runnable {
    private final long duracionTotal;
    private JDialog dialog;
    private JProgressBar barra;

    public ThreadBarra (long duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public void barraProgreso() {
        // Creamos la Barra de Progresp
        this.barra = new JProgressBar(0, 100);
        this.barra.setValue(0); // Iniciamos en 0
        this.barra.setStringPainted(true); // Mostramos el porcentaje

        // Mostramos la barra
        JOptionPane panel = new JOptionPane(barra, JOptionPane.INFORMATION_MESSAGE);
        this.dialog = panel.createDialog(null, "Procesando Pago...");
        this.dialog.setModal(false);
        this.dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Evitar que el usuario lo cierre antes
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));

        // Ajustamos el tiempo para simular el proceso
        long tiempoInicio = System.currentTimeMillis();
        long tiempoActual;
        int porcentaje;

        while(dialog.isVisible()) {
            tiempoActual = System.currentTimeMillis();
            long tiempoTranscurrido = tiempoActual - tiempoInicio;

            // Calculamos porcentaje

            if (duracionTotal > 0) {
                porcentaje = (int) (100 * tiempoTranscurrido / duracionTotal);
                if (porcentaje > 100) porcentaje = 100;
            } else {
                porcentaje = 0;
            }

            // Actualizamos la barra
            final int progresoFinal = porcentaje;
            SwingUtilities.invokeLater(() -> barra.setValue(progresoFinal));

            try {
                Thread.sleep(50); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Método para cerrar el dialog
    public void terminar() {
        if (dialog != null) {
            SwingUtilities.invokeLater(() -> dialog.dispose());
        }
    }

    @Override
    public void run() {
        barraProgreso();
    }
}