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
//funcion qye registra a los empleados
    public void registroEmpleado(GestorDeArchivos gestor, Validaciones v, List<Persona> usuariosEnSistema) {
        String nombre, apellidoP, apellidoM, numeroCelular, nickname = "";
        String correo = "", password = "", turno = "";
        int edad = 0;

        try {

            nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del empleado",
                    "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE);
            if (nombre == null) return;

            apellidoP = JOptionPane.showInputDialog(null, "Ingrese su apellido paterno",
                    "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE);
            if (apellidoP == null) return;

            apellidoM = JOptionPane.showInputDialog(null, "Ingrese su apellido materno",
                    "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE);
            if (apellidoM == null) return;
             // Validar edad 
            boolean edadValida = false;
            while (!edadValida) {
                try {
                    String inputEdad = JOptionPane.showInputDialog(null, "Ingrese su edad",
                            "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE);
                    if (inputEdad == null) return;

                    edad = Integer.parseInt(inputEdad);

                    if (edad < 18 || edad > 99) {
                        throw new IndexOutOfBoundsException("El empleado debe ser mayor a 18 años.");
                    }
                    edadValida = true;

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese solo números.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            numeroCelular = JOptionPane.showInputDialog(null, "Ingrese su número telefónico", 
                "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                if (numeroCelular == null) return;
                // Validar celular único
                boolean celularUnico = false;
            while (!celularUnico) {
                numeroCelular = JOptionPane.showInputDialog(null, "Ingrese su número telefónico", 
                "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                if (numeroCelular == null) return;

                if (v.existeNumeroCelular(usuariosEnSistema, numeroCelular)) {
                    JOptionPane.showMessageDialog(null, "Error. El número de celular ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (numeroCelular.length() != 10) {
                    JOptionPane.showMessageDialog(null, "Error. Número celular inválido", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    celularUnico = true;
                }
            }

            boolean nicknameUnico = false;
            while (!nicknameUnico) {
                nickname = JOptionPane.showInputDialog(null, "Ingrese su nickname", 
            "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                if (nickname == null) return;
                
                if (v.existeNickname(usuariosEnSistema, nickname)) {
                    JOptionPane.showMessageDialog(null, "Error. El nickname ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    celularUnico = true;
                }
            }

            boolean correoValido = false;
            while(!correoValido) {
                try {
                    correo = JOptionPane.showInputDialog(null, "Ingrese su correo", 
            "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                    if (correo == null) return;
                    JOptionPane.showMessageDialog(null, "Verificando correo electrónico...", "Verificación", JOptionPane.INFORMATION_MESSAGE);
                    
                    if (!(correo.endsWith("@gmail.com") || correo.endsWith("@hotmail.com") ||
                    correo.endsWith("@yahoo.com") || correo.endsWith("@outlook.com"))) {
                        throw new MailMismatchException("Error. El correo ingresado es inválido");
                    } else if(v.existeCorreo(usuariosEnSistema, correo)) {
                        JOptionPane.showMessageDialog(null, "Error, el correo ya ha sido registrado, intenta nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Correo validado", "Verificación", JOptionPane.INFORMATION_MESSAGE);
                        correoValido = true;
                    }
                } catch (MailMismatchException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            boolean passwordValida = false;
            while (!passwordValida) {
                try {
                    password = JOptionPane.showInputDialog(null, "Ingrese su contraseña\n(Ingresa al menos 10 caracteres, una mayúscula, una minúscula, un número\ny un caracter especial[@, #, %, $,])",
                    "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                    if (password == null) return;
                    
                    String confirmacion = JOptionPane.showInputDialog(null, "Confirme su contraseña", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
                    if (confirmacion == null) return;
                    passwordValida = v.validarPassword(password, confirmacion);
                } catch (PasswordException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Contraseña", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Aquí se pide el turno
            turno = JOptionPane.showInputDialog(null,
                    "Ingrese turno del empleado (Matutino / Vespertino / Nocturno):",
                    "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE);

            if (turno == null) return;

            // Crear cuenta y objeto Empleado
            Cuenta cuenta = new Cuenta(nickname, password, correo);
            //Empleado empleado = new Empleado(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno);
            // Aquí se elige el tipo de empleado
        String[] opciones = {"Administrador", "Vendedor"};
        int tipo = JOptionPane.showOptionDialog( null,"Seleccione el tipo de empleado a registrar","Tipo de Empleado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        Empleado empleado = null;

        switch (tipo) {
            case 0 -> {
                String diasTrabajo = JOptionPane.showInputDialog(null, "Ingrese los días de trabajo del Administrador:");
                if (diasTrabajo == null) return;

                empleado = new Administrador(
                        nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno, diasTrabajo
                );
            }

            case 1 -> {
                String area = JOptionPane.showInputDialog(null, "Ingrese el área del Vendedor:");
                if (area == null) return;

                empleado = new Vendedor(
                        nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno, area
                );
            }

            default -> {
                JOptionPane.showMessageDialog(null, "Registro cancelado.");
                return;
            }
        }

        // Guardar en sistemas
        usuariosEnSistema.add(personaRegistrada);
        gestor.guardarUsuariosEnArchivo(personaRegistrada);

        JOptionPane.showMessageDialog(null, "Empleado registrado correctamente.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

            // Guardar en archivo
            gestor.guardarUsuariosEnArchivo(empleado);

            JOptionPane.showMessageDialog(null, "Empleado registrado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

