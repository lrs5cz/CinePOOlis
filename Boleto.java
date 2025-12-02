public class Boleto extends Funcion {
    // Atributos
    private String asiento;
    private int precio;
    private String nicknameComprador;

    // Constructor
    public Boleto(String fecha, String hora, String sala, Pelicula pelicula, String asiento, String nicknameComprador) {
        super(fecha, hora, sala, pelicula);
        this.asiento = asiento;
        this.nicknameComprador = nicknameComprador;
        if (sala.toUpperCase().equals("SALA VIP") || sala.toUpperCase().equals("VIP") ) {
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

    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public String getNicknameComprador(){
        return nicknameComprador;
    }

    public void setNickname(String nicknameComprador){
        this.nicknameComprador= nicknameComprador;
    }

    @Override
    public String toString() {
        return super.toString() + "|" + asiento;
    }

}

