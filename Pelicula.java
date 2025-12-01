import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private String nombrePelicula, genero, sinopsis, duracion, idPelicula;

    // Constructor
    public Pelicula(String nombrePelicula, String genero, String sinopsis, String duracion) {
        this.nombrePelicula = nombrePelicula;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.idPelicula = generarIdPelicula();
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

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    // Métodos

    public boolean unicoIdPelicula(List<Pelicula> cartelera, String id) {
        Pelicula pelicula = null;
        for (Pelicula p : cartelera) {
            if (p.getIdPelicula().equals(id.toUpperCase())) {
                pelicula = p;
                break;
            }
        }
        
        if (pelicula == null) return true;
        else return false;
    }

    public String generarIdPelicula() {
        GestorDeArchivos gestor = new GestorDeArchivos();
        // Inicializamos
        StringBuilder id = new StringBuilder();
        List<Pelicula> cartelera = new ArrayList<>();

        try {
            // Genera un ID con las iniciales de la película
            cartelera = gestor.cargarPeliculas();
            String[] palabras = nombrePelicula.split(" "); 
            for (String palabra : palabras) {
                if (!palabra.isEmpty()) id.append(palabra.charAt(0));
            }
        } catch (IOException e) {
            
        }

        // Almacenamos el id actual
        String idBase = id.toString().toUpperCase();
        String idFinal = idBase;
        int contador = 1;

        // Si el id existe, le añade un número al nuevo ID para que sea diferente
        while (!unicoIdPelicula(cartelera, idFinal)) {
            idFinal = idBase + contador;
            contador++;
        }

        return idFinal;
    }

    @Override
    public String toString() {
        return nombrePelicula + "|" + genero + "|" + sinopsis + "|" + duracion + "|" + idPelicula;
    }
}