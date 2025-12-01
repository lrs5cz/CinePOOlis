public class Funcion {
    // Atributos
    String nombrePelicula, idPelicula, fecha, hora, sala;
    private Pelicula pelicula;
    private String[][] mapaAsientos;

    // Constructor
    public Funcion(String fecha, String hora, String sala, Pelicula pelicula) {
        this.nombrePelicula = pelicula.getNombrePelicula();
        this.idPelicula = pelicula.generarIdPelicula(); 
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
        this.mapaAsientos = null;
    }

    // Getters y Setters
    public String getIdPelicula() {
        return idPelicula;
    }

    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Pelicula getPelicula () {
        return pelicula;
    }

    public String[][] getMapaAsientos() {
        return mapaAsientos;
    }

    public void setMapaAsientos(String[][] mapaAsientos) {
        this.mapaAsientos = mapaAsientos;
    }

    // Genera un ID de la función
    @Override
    public String toString() {
        return idPelicula + "|" + fecha + "|" + hora + "|" + sala;
    }
}