import java.util.Random;
import javax.swing.JOptionPane;

public class ThreadBancario implements Runnable {

    public void cargoTarjeta() throws InterruptedException {
        // Genera un valor entre 2 y 5 segundos para la pausa
        Random random = new Random();
        int intervalo = random.nextInt(2000, 5001);
        
        // Asignamos el valor de las pausas a la duración total
        long duracionTotalCalculada = (intervalo * 2L) + 3000;

        // Creamos el segundo hilo (el de la barra de progreso)
        ThreadBarra progreso = new ThreadBarra(duracionTotalCalculada);
        Thread hiloProgreso = new Thread(progreso);
        hiloProgreso.start();

        // Muestra el primer mensaje
        JOptionPane.showMessageDialog(null, "Estableciendo conexión con el banco..." ,
        "Validación del pago", JOptionPane.INFORMATION_MESSAGE); 
        // Pausa
        Thread.sleep(intervalo);

        JOptionPane.showMessageDialog(null, "Haciendo el cargo correspondiente..." ,
        "Validación del pago", JOptionPane.INFORMATION_MESSAGE); 

        Thread.sleep(intervalo);

        JOptionPane.showMessageDialog(null, "Transacción finalizada." ,
        "Pago realizado", JOptionPane.INFORMATION_MESSAGE); 
        Thread.sleep(3000);

        // Terminamos el progreso
        progreso.terminar();
    }


    @Override
    public void run() {
        try {
            cargoTarjeta();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }    
}