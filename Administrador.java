import java.io.IOException;
import java.util.List;
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
    boolean banderaRepetir = false;
        public void agregarPeliculaACartelera() {
        do {
            try {
                banderaRepetir =false;

                String nombrePelicula = JOptionPane.showInputDialog("Ingrese el nombre de la película:", "Ej. Titanic");
                String generoPelicula = JOptionPane.showInputDialog("Ingrese el género de la película:", "Ej. Terror");
                String sinopsis = JOptionPane.showInputDialog("Ingrese la sinopsis de la película:");
                String duracion = JOptionPane.showInputDialog("Ingrese la duración de la película (formato hh:mm):", "Ej. 02:24");
                
                Pelicula nuevaPelicula = new Pelicula(nombrePelicula, generoPelicula, sinopsis, duracion);
                GestorDeArchivos unGestorDearchivosAdministrador = new GestorDeArchivos();// Crea un objeto de la clase GestorDearchivosAdministrador
                unGestorDearchivosAdministrador.guardarPeliculaEnArchivo(nuevaPelicula); // Llama al metodo que guarda la pelicula en el archivo
                List<Pelicula> cartelera = unGestorDearchivosAdministrador.cargarPeliculas(); // Lista de peliculas cargadas desde el archivo
                cartelera.add(nuevaPelicula);// Agregar a la lista de cartelera
                JOptionPane.showMessageDialog(null, "La película " + nombrePelicula + " ha sido agregada a la cartelera.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al agregar la película. Por favor, intente de nuevo.");
                banderaRepetir = true;
            }
        } while (banderaRepetir);
    }


    public void agregarFuncion() throws IOException{
        GestorDeArchivos gestor = new GestorDeArchivos();
        try {
            List<Pelicula> peliculas = gestor.cargarPeliculas();
            if(peliculas.isEmpty()){
                JOptionPane.showMessageDialog(null, "No hay peliculas registradas");
                return;
            }
            //Mostrar peliculas

            StringBuilder unStringBuilder = new StringBuilder("Pelicula disponibles:\n");
            for(int i = 0; i < peliculas.size(); i++) {
                unStringBuilder.append((i + 1)).append(". ").append(peliculas.get(i).getNombrePelicula()).append("\n");
            }

            JOptionPane.showMessageDialog(null, unStringBuilder.toString());
            JOptionPane.showConfirmDialog(null, "Ingrese el numero de la pelicula");
            String seleccion = JOptionPane.showInputDialog("Ingrese el numero de la pelicula que desea seleccionar");
            
            /*
             * Nota: Podemos castear directamente y validar con un try-catch y NumberFormatException
             */

            if(seleccion == null) return;

                int idSelec;
            try {
                idSelec = Integer.parseInt(seleccion)-1;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error al procesar, Numero no valido"+ e.getMessage());
                return;
            }

            if (idSelec < 0 || idSelec >= peliculas.size()) {
                JOptionPane.showMessageDialog(null, "Ese numero esta fuera de rango");
                return;
            }

            Pelicula unaPeli = peliculas.get(idSelec);

            String fecha = JOptionPane.showInputDialog("Ingrese la fecha de la función (AAAA/MM/DD)", "Agregar Función");
            if(fecha == null)return;
            
            //Para la seleccion de sala
            String[] opcionesSalas = {"Sala A", "Sala B", "Sala VIP"}; 
            int salaIdx = JOptionPane.showOptionDialog(null, "Seleccione una sala","Agregar Función",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opcionesSalas,opcionesSalas[0]);
            String salaSeleccionada = (salaIdx == 0)? "A":(salaIdx == 1)? "B" : "VIP";
            if (salaIdx < 0 || salaIdx > 3) return;
            
            //Mostramos las funciones existentes
            List<Pelicula> cartelera = gestor.cargarPeliculas();
            List<Funcion> funcionesExistentes = gestor.mostrarFunciones(cartelera);
            StringBuilder programacion = new StringBuilder("Programacion para "+ fecha + " en Salas \n");
            if(funcionesExistentes.isEmpty()) {
                programacion.append("(No hay funciones)\n");
            } else {
                for(Funcion f : funcionesExistentes){
                    programacion.append(f.getIdPelicula()).append(" : ").append(f.getHora().substring(0,2)).append(" : ").append(f.getHora().substring(2,4)).append("\n");

                }
                JOptionPane.showMessageDialog(null, programacion.toString());
            }
            while(true) {
                String accion = JOptionPane.showInputDialog("Escriba 'Cancelar' o 'Alta'");
                if (accion == null || accion.equalsIgnoreCase("Cancelar")) return;
                if (!accion.equalsIgnoreCase("Alta")) {
                    JOptionPane.showMessageDialog(null, "Opcion invalida");
                    continue;
                }

                String horaS =JOptionPane.showInputDialog("Ingrese la hora(00-23)");
                String minS =JOptionPane.showInputDialog("Ingrese los minutos (00-59)");
                if(minS == null)return;
                int h , m;
                try {
                    h = Integer.parseInt(horaS);
                    m = Integer.parseInt(minS);
                    if(h < 0 || h > 23 || m < 0 || m > 59) throw new NumberFormatException();
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Hora o minutos no validos");
                    continue;
                }
                String horaFinal = String.format("%02d%02d", h, m);
                //Validar intervalo entre funciones
                //gestor.validarIntervaloEntreFunciones(salaSeleccionada, Fecha, horaFinal);
                if(!gestor.validarIntervaloEntreFunciones(salaSeleccionada, fecha, horaFinal, unaPeli)){
                    JOptionPane.showMessageDialog(null, "Existe otra funcion programada a menos de 30min para esta sala");
                    continue;
                }

                // Generar ID y guardar la función
                Funcion nuevaFuncion = new Funcion(fecha, horaFinal, salaSeleccionada, unaPeli);
                String idFuncion = nuevaFuncion.getIdPelicula() + ":" + fecha.replace("/", "")+ ":" + horaFinal + ":" + salaSeleccionada;
                
                gestor.guardarFuncionesEnArchivo(nuevaFuncion);
                JOptionPane.showMessageDialog(null, "Funcion registrada con id:\n"+idFuncion);
                break;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar "+ e.getMessage());
        }
    }
    
    //Función para mostrar el menu del administrador
    public void mostrarMenuAdministrador() throws IOException{
        int opcionint = -1;
        do { 
            JOptionPane.showMessageDialog(null, "--- MENU ADMINISTRADOR ---\n1. Agregar Pelicula a Cartelera\n2. Agregar Funcion\n3. Salir","**MENU**",JOptionPane.PLAIN_MESSAGE);
            String opcion = JOptionPane.showInputDialog("Ingrese la opcion deseada");
            if(opcion == null){
                JOptionPane.showMessageDialog(null, "Saliendo del menu...");
                break;
            }
            //Validamos que sea un valor numerico
            try {
                opcionint = Integer.parseInt(opcion);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un numero valido");
                continue;
            }
            switch (opcionint) {
                case 1 -> agregarPeliculaACartelera();
                case 2 -> agregarFuncion();
                case 3 -> JOptionPane.showMessageDialog(null, "Saliendo del menu...");
                default -> JOptionPane.showMessageDialog(null, "Opcion no valida, intente de nuevo.");
            }
            //agregarFuncion(funciones, cartelera, opcion)
        } while (opcionint != 0);
    }
}
