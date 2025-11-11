public class Funcion {
    // Atributos
    String idPelicula, fecha, hora, sala;
    Pelicula pelicula;

    // Constructor
    public Funcion(String idPelicula, String fecha, String hora, String sala) {
        this.idPelicula = pelicula.generarIdPelicula();
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
    }

    // Getters y Setters
    public String getIdPelicula() {
        return idPelicula;
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

    // Genera un ID de la función
    @Override
    public String toString() {
        return idPelicula + ":" + fecha + ":" + hora + ":" + sala;
    }
}