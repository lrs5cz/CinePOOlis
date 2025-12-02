
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;

public class Vendedor extends Empleado {
    // Atributos
    public String diaDescanso;

    // Constructor
    public Vendedor(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String turno, String diaDescanso) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno);
        this.diaDescanso = diaDescanso;
    }

    // Getters y Setters

    public String getDiaDescanso() {
        return diaDescanso;
    }   

    public void setDiaDescanso(String diaDescanso) {
        this.diaDescanso = diaDescanso;
    }
    public void menuVendedor() throws IOException{
        String[] op = {"Opcion 1", "Opcion 2"};
        GestorDeArchivos unGestor = new GestorDeArchivos();
        List<String> historialOrdenes = unGestor.cargarHistorialDeDulceria();
        int res = JOptionPane.showOptionDialog(null, "Elige una opcion\n1) Mostral historial de ordenes atendidas\n2) Cerrar Sesion", "Menu Vendedor", JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
        switch (res) {
            case 0:
                StringBuilder ordenes = new StringBuilder();
                ordenes.append("Historial de ordenes\n\n");

                for (String unaOrden : historialOrdenes) {
                    ordenes.append(unaOrden).append("\n");
                }
                JOptionPane.showMessageDialog(null,ordenes.toString());
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Cerrando sesion");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Error,Opcion invalida");
        }
    }
}
