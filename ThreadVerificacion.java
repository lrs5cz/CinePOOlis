import javax.swing.JOptionPane;

public class ThreadVerificacion implements Runnable {
    public String correo;

    public ThreadVerificacion(String correo) {
        this.correo = correo;
    }

    @Override
    public void run() {
        try {
            JOptionPane.showMessageDialog(null, "Verificando correo electrónico...", "Verificación", JOptionPane.INFORMATION_MESSAGE);
            if (!(correo.endsWith("@gmail.com") || correo.endsWith("@hotmail.com") ||
            correo.endsWith("@yahoo.com") || correo.endsWith("@outlook.com"))) {
                Thread.sleep(3000);
                throw new MailMismatchException("Error. El correo ingresado es inválido");
            } else {
                Thread.sleep(3000);
                JOptionPane.showMessageDialog(null, "Correo validad", "Verificación", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (MailMismatchException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}