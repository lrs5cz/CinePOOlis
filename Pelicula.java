public class Pelicula {
    private String nombrePelicula, genero, sinopsis, duracion;

    // Constructor
    public Pelicula(String nombrePelicula, String genero, String sinopsis, String duracion) {
        this.nombrePelicula = nombrePelicula;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
    }

    // Getters y Setters
    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    // Métodos

    public String generarIdPelicula() {
        // Genera un ID con las iniciales de la película
        String[] palabras = nombrePelicula.split(" ");
        StringBuilder id = new StringBuilder();
        for (String palabra : palabras) {
            id.append(palabra.charAt(0));
        }
        return id.toString().toUpperCase();
    }

    @Override
    public String toString() {
        return nombrePelicula + "|" + genero + "|" + sinopsis + "|" + duracion;
    }
}
