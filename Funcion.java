public class Funcion {
    // Atributos
    String nombrePelicula, idPelicula, fecha, hora, sala;
    Pelicula pelicula;

    // Constructor
    public Funcion(Pelicula pelicula, String fecha, String hora, String sala) {
        this.pelicula = pelicula;
        this.nombrePelicula = pelicula.getNombrePelicula;
        this.idPelicula = pelicula.generarIdPelicula();
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
    }

    // Getters y Setters
    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

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

    // Función para convertir un valor numérico de fila a letra
    public String obtenerLetraFila(int fila) {
        return String.valueOf(letra);
        char letra = (char) ('A' + fila); // Convierte el número de fila a letra (0 -> A, 1 -> B, etc.)
    }

    public int obtenerNumeroFila(String letra) {
        return letra.charAt(0) - 'A'; // Convierte la letra de fila a número (A -> 0, B -> 1, etc.)
    }

    
    // Función para obtener los asientos de la sala, así como su disponibilidad
    public String[][] obtenerAsientosA() {
        String[][] asientosA = new String[10][15];
        for (int i = 0; i < asientosA.length; i++) {
            String fila = obtenerLetraFila(i);
            for (int j = 0; j < asientosA[i].length; j++) {
                int columna = j + 1;
                asientosA[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
            }
        }
        return asientosA;
    }

    public String[][] obtenerAsientosB() {
        String[][] asientosB = new String[10][15];
        for (int i = 0; i < asientosB.length; i++) {
            String fila = obtenerLetraFila(i);

            for (int j = 0; j < asientosB[i].length; j++) {

                boolean esFilaPasillo = (i >= 0 && i <= 3); 
                
                boolean esPasilloIzquierdo = (j >= 0 && j <= 3); 
                
                boolean esPasilloDerecho = (j >= 11 && j <= 14); 

                if (esFilaPasillo && (esPasilloIzquierdo || esPasilloDerecho)) {
                    asientosB[i][j] = "    "; 
                } else {
                    int columna = j + 1;
                    asientosB[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
                }
            }
        }
        return asientosB;
    }

    public String[][] obtenerAsientosVIP() {
        String[][] asientosVIP = new String[8][6];
        for (int i = 0; i < asientosA.length; i++) {
            String fila = obtenerLetraFila(i);
            for (int j = 0; j < asientosVIP[i].length; j++) {
                int columna = j + 1;
                asientosVIP[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
            }
        }
        return asientosVIP;
    }

    // Función para comprobar si una sala tiene disponibilidad
    public boolean disponibilidadSala(String[][] asientos) {
        return true;
    }

    // Genera un ID de la función
    @Override
    public String toString() {
        return idPelicula + ":" + fecha + ":" + hora + ":" + sala;
    }
}
