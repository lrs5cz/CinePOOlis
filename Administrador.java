import javax.swing.JOptionPane;

public class Administrador extends Empleado {
    // Atributos
    private String diasTrabajo;

    // Constructor
    public Administrador(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String turno, String diasTrabajo) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno);
        this.diasTrabajo = diasTrabajo;
    }
    
    // Getters y Setters

    public String getDiasTrabajo() {
        return diasTrabajo;
    }

    public void setDiasTrabajo(String diasTrabajo) {
        this.diasTrabajo = diasTrabajo;
    }

    // Métodos
    
    public void agregarPeliculaACartelera(List<Pelicula> cartelera) {
        try {
            String nombrePelicula = JOptionPane.showInputDialog("Ingrese el nombre de la película:");
            String generoPelicula = JOptionPane.showInputDialog("Ingrese el género de la película:");
            String sinopsis = JOptionPane.showInputDialog("Ingrese la sinopsis de la película:");
            String duracion = JOptionPane.showInputDialog("Ingrese la duración de la película (formato hh:mm):");
            Pelicula nuevaPelicula = new Pelicula(nombrePelicula, generoPelicula, sinopsis, duracion);
            cartelera.add(nuevaPelicula);
            JOptionPane.showMessageDialog(null, "La película " + nombrePelicula + " ha sido agregada a la cartelera.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar la película. Por favor, intente de nuevo.");
        }
    }

    public void agregarFuncion(List<Funcion> funciones, List<Pelicula> cartelera, Película pelicula){
        String idPelicula = pelicula.generarIdPelicula();
        String[] opcionesSalas = {"Sala 1", "Sala 2", "Sala 3D",  "Sala 4DX", "Sala VIP", "Sala Junior"};
        if (idPelicula != null) {
            String fecha = JOptionPane.showInputDialog("Ingrese la fecha de la función (formato dd/mm/yyyy):");
            String hora = JOptionPane.showInputDialog("Ingrese la hora de la función (formato hh:mm):");
            int salaIndex = JOptionPane.showOptionDialog(null, "Seleccione la sala para la función:", "Selección de Sala",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcionesSalas, opcionesSalas[0]);
            String sala = opcionesSalas[salaIndex];
            Funcion nuevaFuncion = new Funcion(idPelicula, fecha, hora, sala);
            funciones.add(nuevaFuncion);
            JOptionPane.showMessageDialog(null, "La función para la película " + peliculaSeleccionada.getNombrePelicula() + " ha sido agregada.");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una película con el ID proporcionado.");
        }

    }
}