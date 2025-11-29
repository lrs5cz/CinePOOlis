import java.util.ArrayList;
import java.util.List;

public class Orden {
    private List<Combo> orden = new ArrayList<>();
    
    public Orden (List<Combo> orden) {
        this.orden = orden;
    }

    public List<Combo> getOrden () {
        return orden;
    }

    public void setOrden(List<Combo> orden) {
        this.orden = orden;
    }
    
    public List<Combo> generarOrden() {
        return new ArrayList<>();
    }

    public int calcularTotalOrden(List<Combo> combos) {
        int total = 0;
        for (Combo combo : combos) {
            total += combo.getPrecioCombo();
        }
        return total;
    }

    public void mostrarOrden(List<Combo> combos) {
        System.out.println("Detalles de la orden:");
        for (Combo combo : combos) {
            System.out.println("Combo: " + combo.getNombreCombo() + " - Precio: $" + combo.getPrecioCombo());
            System.out.println("Alimentos incluidos:");
            for (Alimento alimento : combo.getAlimentosCombo()) {
                System.out.println("  - " + alimento.getNombreAlimento() + " (" + alimento.getTamanio() + ")");
            }
        }
        System.out.println("Total de la orden: $" + calcularTotalOrden(combos));
    }
}
