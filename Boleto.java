public class Boleto extends Funcion {
    // Atributos
    private String asiento;

    // Constructor
    public Boleto(String pelicula,String fecha, String horario, String sala, String asiento) {
        super(pelicula,fecha, horario, sala);
        this.asiento = asiento;
    }

    // Getters y Setters
    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + asiento;
    }
}
