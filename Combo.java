import java.util.ArrayList;
import java.util.List;

public class Combo {
    private List<Alimento> alimentosCombo;
    private String nombreCombo;
    private int precioCombo;

    public Combo(String nombreCombo, int precioCombo) {
        this.nombreCombo = nombreCombo;
        this.alimentosCombo = new ArrayList<>();
        this.precioCombo = precioCombo;
    }

    // Getters y Setters
    public String getNombreCombo() {
        return nombreCombo;
    }

    public void setNombreCombo(String nombreCombo) {
        this.nombreCombo = nombreCombo;
    }

    public int getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(int precioCombo) {
        this.precioCombo = precioCombo;
    }

    public List<Alimento> getAlimentosCombo() {
        return alimentosCombo;
    }

    public void agregarAlimento(Alimento alimentoCombo) {
        this.alimentosCombo.add(alimentoCombo);
    }

    // Métodos

    public Combo crearComboAmix() {
        this.nombreCombo = "Combo Amix";
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 80)); // Precio original: 80
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 60));
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 60)); // Precio original: 60
        int precio = 0;
        // Sumamos los precios y los seteamos en 0, ya que nos pueden molestar en el futuro
        for (Alimento a : this.alimentosCombo) {
            precio += a.getPrecio();
            a.setPrecio(0);
        }
        // 10% de descuento
        this.precioCombo = (int)Math.round(precio * 0.9);
        return this; // Retorna el combo creado
    }

    public Combo crearComboNachos() {
        this.nombreCombo = "Combo Nachos";
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 80)); 
        this.alimentosCombo.add(new Alimento("Nachos", "Jumbo", 90)); 
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 60));
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 60));
        int precio = 0;
        // Sumamos los precios y los seteamos en 0, ya que nos pueden molestar en el futuro
        for (Alimento a : this.alimentosCombo) {
            precio += a.getPrecio();
            a.setPrecio(0);
        }
        // 10% de descuento
        this.precioCombo = (int)Math.round(precio * 0.9);
        return this; // Retorna el combo creado
    }

    public Combo crearComboBuenTrio() {
        this.nombreCombo = "Combo Buen Trío"; 
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Mega", 90)); 
        this.alimentosCombo.add(new Alimento("Nachos", "Mega", 100)); 
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 70)); 
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 70));
        this.alimentosCombo.add(new Alimento("Refresco", "Mega", 70));
        int precio = 0;
        // Sumamos los precios y los seteamos en 0, ya que nos pueden molestar en el futuro
        for (Alimento a : this.alimentosCombo) {
            precio += a.getPrecio();
            a.setPrecio(0);
        }
        // 10% de descuento
        this.precioCombo = (int)Math.round(precio * 0.9);
        return this; // Retorna el combo creado
    }

    public Combo crearComboQueMeVes() {
        this.nombreCombo = "Combo Qué Me Ves";
        this.alimentosCombo = new ArrayList<>();
        this.alimentosCombo.add(new Alimento("Palomitas", "Jumbo", 80)); 
        this.alimentosCombo.add(new Alimento("Nachos", "Jumbo", 90)); 
        this.alimentosCombo.add(new Alimento("Refresco", "Jumbo", 60));
        int precio = 0;
        // Sumamos los precios y los seteamos en 0, ya que nos pueden molestar en el futuro
        for (Alimento a : this.alimentosCombo) {
            precio += a.getPrecio();
            a.setPrecio(0);
        }
        // 10% de descuento
        this.precioCombo = (int)Math.round(precio * 0.9);
        return this; // Retorna el combo creado
    }

    public Combo crearOrdenPersonalizada(List<Alimento> alimentosPersonalizados) {
        this.nombreCombo = "Orden Personalizada";
        this.alimentosCombo = alimentosPersonalizados;
        this.precioCombo = 0;
        for (Alimento a : this.alimentosCombo) {
            this.precioCombo += a.getPrecio();
        }
        return this; // Retorna el combo creado
    }

    public void mostrarOrden () {
        StringBuilder ordenStr = new StringBuilder("Detalles de la orden:\n\nAlimentos: ");
        for (Alimento a : getAlimentosCombo()) {
            ordenStr.append(a).append("\t");
        }
        ordenStr.append("\n\nPrecio: ").append(getPrecioCombo());
    }
}
