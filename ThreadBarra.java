import javax.swing.*;

public class ThreadBarra implements Runnable {
    private final long duracionTotal;
    private JDialog dialog;
    private JProgressBar barra;

    public ThreadBarra (long duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public void barraProgreso() {
        SwingUtilities.invokeLater(() -> {
            // Creamos barra de progreso
            this.barra = new JProgressBar(0, 100);
            this.barra.setValue(0);
            this.barra.setStringPainted(true);

            JOptionPane panel = new JOptionPane(barra, JOptionPane.INFORMATION_MESSAGE);
            this.dialog = panel.createDialog(null, "Procesando Pago...");
            this.dialog.setModal(false);
            this.dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            // Situamos la barra 200px arriba
            dialog.setLocationRelativeTo(null);
            int currentX = dialog.getX();
            int currentY = dialog.getY();
            dialog.setLocation(currentX, currentY + 200);
            
            // Hacemos visible el diálogo
            dialog.setVisible(true);
        });

        try {
        Thread.sleep(100); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // Ajustamos el tiempo para simular el proceso
        long tiempoInicio = System.currentTimeMillis();

        while(this.dialog == null || !this.dialog.isVisible()) {
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        while(dialog.isVisible()) {
        long tiempoActual = System.currentTimeMillis();
        long tiempoTranscurrido = tiempoActual - tiempoInicio;
        int porcentaje;
        // Calculamos porcentaje

            if (duracionTotal > 0) {
                porcentaje = (int) (100 * tiempoTranscurrido / duracionTotal);
                if (porcentaje > 100) porcentaje = 100;
            } else {
                porcentaje = 0;
            }

            // Actualizamos la barra
            final int progresoFinal = porcentaje;
            SwingUtilities.invokeLater(() -> {
                // Solo actualizamos si el diálogo no ha sido cerrado por terminar()
                if (dialog.isVisible()) {
                    barra.setValue(progresoFinal);
                }
            });

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