import java.io.IOException;
import javax.swing.JOptionPane;

public class CinePOOlis {
    public static void main(String[] args) {
        try {
            // Creamos un administrador de prueba
            Cuenta cuentaAdmin = new Cuenta("admin", "1234","ajndkd"); // Asumiendo que tienes la clase Cuenta
            Administrador admin = new Administrador(
                    "Andres", "Lopez", "Perez", 25, "5551234567",
                    cuentaAdmin, "Matutino", "Lunes a Viernes"
            );

            int opcion = -1;
            do {
                String opcionStr = JOptionPane.showInputDialog(null, 
                        "1. Agregar Pelicula a Cartelera\n" +
                        "2. Agregar Funcion\n" +
                        "3. Salir\n" +
                        "Ingrese su opcion:", "Menú del administrador",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (opcionStr == null) break; // Cancelar

                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un numero valido");
                    continue;
                }

                switch (opcion) {
                    case 1 -> admin.agregarPeliculaACartelera();
                    case 2 -> admin.agregarFuncion();
                    case 3 -> JOptionPane.showMessageDialog(null, "Saliendo del programa...");
                    default -> JOptionPane.showMessageDialog(null, "Opcion no valida, intente de nuevo");
                }

            } while (opcion != 3);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }
}
