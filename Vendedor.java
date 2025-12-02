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

    public static void menuVendedor(GestorDeArchivos unGestor, Vendedor vendedor) {
        int res = 0;
        do {
            try {
                String[] op = {"Ver órdenes preparadas", "Cerrar sesión"};
                List<String> historialOrdenes = unGestor.cargarHistorialVendedor(vendedor.generarIdNombre());
                if (historialOrdenes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bienvenido/a, " + vendedor.getNombre() + 
                    "\n\n No has atendido ninguna orden todavía, se te redigirá al menú principal...", "Vendedores", JOptionPane.PLAIN_MESSAGE);
                    return;
                } else {
                    res = JOptionPane.showOptionDialog(null, "Bienvenido/a, " + vendedor.getNombre() + "\n\nQué acción desea realizar?\n", 
                    "Vendedores", JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
                    if (res == 0 && !historialOrdenes.isEmpty()) {
                        StringBuilder ordenes = new StringBuilder();
                            ordenes.append("Historial de ordenes\n\n");
                            int i = 1;
                            for (String unaOrden : historialOrdenes) {
                                ordenes.append(i). append(". ").append(unaOrden).append("\n");
                                i++;
                            }
                            
                            JOptionPane.showMessageDialog(null, ordenes.toString(), "Historial de " + vendedor.getNombre(), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Cerrando sesion...", "Hasta luego", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error de archivo: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                break;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error desconocido: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                break;
            }
        } while (res == 0);
    }

    // Métodos de preparación
    public String prepararComboAmix(Combo combo) {
        if(combo.getNombreCombo().toUpperCase().equals("COMBO AMIX")) {
            JOptionPane.showMessageDialog(null, "Preparando un Combo Amix...", "Preparación", JOptionPane.INFORMATION_MESSAGE);
            return "Combo Amix";
        } else return null;
    }

    public String prepararComboNachos(Combo combo) {
        if(combo.getNombreCombo().toUpperCase().equals("COMBO NACHOS")) {
            JOptionPane.showMessageDialog(null, "Preparando un Combo Nachos...", "Preparación", JOptionPane.INFORMATION_MESSAGE);
            return "Combo Nachos";
        } else return null;
    }

    public String prepararComboBuenTrio(Combo combo) {
        if(combo.getNombreCombo().toUpperCase().equals("COMBO BUEN TRÍO")) {
            JOptionPane.showMessageDialog(null, "Preparando un Combo Buen Trío...", "Preparación", JOptionPane.INFORMATION_MESSAGE);
            return "Combo Buen Trío";
        } else return null;
    }

    public String prepararComboQueMeVes(Combo combo) {
        if(combo.getNombreCombo().toUpperCase().equals("COMBO QUÉ ME VES")) {
            JOptionPane.showMessageDialog(null, "Preparando un Combo Qué Me Ves...", "Preparación", JOptionPane.INFORMATION_MESSAGE);
            return "Combo Qué Me Ves";
        } else return null;
    }

    public String prepararOrdenPersonalizada(Combo combo) {
        if(combo.getNombreCombo().toUpperCase().equals("ORDEN PERSONALIZADA")) {
            JOptionPane.showMessageDialog(null, "Preparando una Orden Personalizada...", "Preparación", JOptionPane.INFORMATION_MESSAGE);
            return "Orden Personalizada";
        } else return null;
    }
}