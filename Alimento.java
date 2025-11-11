public class Alimento {
    // Atributos
    private String nombreAlimento, tamanio;
    private int precio;

    // Constructor
    public Alimento(String nombreAlimento, String tamanio, int precio) {
        this.nombreAlimento = nombreAlimento;
        this.tamanio = tamanio;
        this.precio = precio;
    }

    // Getters y Setters

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}