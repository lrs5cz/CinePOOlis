public class Palomitas extends Alimento {
    // Atributos
    private String sabor;

    // Constructor
    public Palomitas(String nombreAlimento, String tamanio, int precio, String sabor) {
        super(nombreAlimento, tamanio, precio);
        this.sabor = sabor;
    }

    // Getters y Setters
    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }
}