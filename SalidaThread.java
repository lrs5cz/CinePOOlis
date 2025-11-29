import javax.swing.JOptionPane;

public class SalidaThread implements Runnable {

    public void terminarEjecucion() throws InterruptedException{
        JOptionPane.showMessageDialog(null, "Saliendo del programa...", "Salida", JOptionPane.PLAIN_MESSAGE);
        Thread.sleep(3000);
        JOptionPane.showMessageDialog(null, "Hasta luego!", "Salida", JOptionPane.PLAIN_MESSAGE);
        Thread.sleep(3000);
        System.exit(0);
    }

    @Override
    public void run() {
        try {
            terminarEjecucion();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}