public class Boleto extends Funcion {
    // Atributos
    private String asiento, precio;

    // Constructor
    public Boleto(Pelicula pelicula, String fecha, String hora, String sala, String asiento) {
        super(pelicula, fecha, hora, sala);
        this.asiento = asiento;
        if ((sala.toLowerCase()).equals("sala vip")) {
            this.precio = 190;
        } else { // Salas A y B
            this.precio = 90;
        }
    }

    // Getters y Setters
    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public int getPrecio() {
        return precio;
    }

    public void sePrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + asiento;
    }
}
