import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    //Nota: Se tiene que validar que la película no exista ya en el archivo con el nombre (o el ID) 
    public static void agregarPeliculaACartelera(GestorDeArchivos gestor) {
            boolean banderaRepetir = false;
            do {
                try {
                    String nombrePelicula = JOptionPane.showInputDialog("Ingrese el nombre de la película", "Ej. Titanic");
                    if (nombrePelicula == null) return;
                    
                    String generoPelicula = JOptionPane.showInputDialog("Ingrese el género de la película", "Ej. Terror");
                    if (generoPelicula == null) return;
                    
                    String sinopsis = JOptionPane.showInputDialog("Ingrese la sinopsis de la película");
                    if (sinopsis == null) return;
                    
                    String duracion = JOptionPane.showInputDialog("Ingrese la duración de la película (formato hh:mm)", "Ej. 02:24");
                    if (duracion == null) return;
                    
                    Pelicula nuevaPelicula = new Pelicula(nombrePelicula, generoPelicula, sinopsis, duracion);
                    gestor.guardarPeliculaEnArchivo(nuevaPelicula); // Llama al metodo que guarda la pelicula en el archivo
                    List<Pelicula> cartelera = gestor.cargarPeliculas(); // Lista de peliculas cargadas desde el archivo
                    cartelera.add(nuevaPelicula);// Agregar a la lista de cartelera
                    JOptionPane.showMessageDialog(null, "La película " + nombrePelicula + " ha sido agregada a la cartelera.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al agregar la película. Por favor, intente de nuevo.");
                    banderaRepetir = true;
                }
            } while (banderaRepetir);
    }

    public static void agregarFuncion(GestorDeArchivos gestor) throws IOException {
        try {
            List<Pelicula> peliculas = gestor.cargarPeliculas();
            if (peliculas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay películas registradas. Agrega una primero.");
                return;
            }

            // Selección de Película
            StringBuilder unStringBuilder = new StringBuilder("Películas disponibles:\n");
            for (int i = 0; i < peliculas.size(); i++) {
                unStringBuilder.append((i + 1)).append(". ").append(peliculas.get(i).getNombrePelicula()).append("\n");
            }
            String seleccion = JOptionPane.showInputDialog("Ingrese el número de la película que desea seleccionar\n" + unStringBuilder, "Seleccionar Película");

            if (seleccion == null) return;

            int idSelec = -1;
            try {
                idSelec = Integer.parseInt(seleccion) - 1;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error al procesar: Número no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (idSelec < 0 || idSelec >= peliculas.size()) {
                JOptionPane.showMessageDialog(null, "Error. Elige un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pelicula unaPeli = peliculas.get(idSelec);

            // Selección de Sala 
            String[] opcionesSalas = {"Sala A", "Sala B", "Sala VIP"};
            int salaIdx = JOptionPane.showOptionDialog(
                null, 
                "Seleccione una sala para la función:",
                "Agregar Función", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opcionesSalas, 
                opcionesSalas[0]
            );
            // Si el usuario cierra la ventana (cancela)
            if (salaIdx == JOptionPane.CLOSED_OPTION) return; 
            String salaSeleccionada = opcionesSalas[salaIdx];
            
            // Mostrar Programación Actual
            List<Pelicula> cartelera = gestor.cargarPeliculas();
            List<Funcion> funcionesExistentes = gestor.mostrarFunciones(cartelera);
            StringBuilder programacion = new StringBuilder("Programación en Cartelera:\n");
            if(funcionesExistentes.isEmpty()) {
                programacion.append(" (No hay funciones programadas)\n");
            } else {
                for (Funcion f : funcionesExistentes){
                    programacion.append("Pelicula: ").append(f.getNombrePelicula()) 
                                .append("\nFecha: ").append(f.getFecha())
                                .append("\nHora: ").append(f.getHora()) 
                                .append("\nSala: ").append(f.getSala()).append("\n");
                }
            }

            // Creamos un ScrollPane para ver las funciones
            JTextArea textArea = new JTextArea(programacion.toString());
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(null, scrollPane, "Funciones Programadas", JOptionPane.INFORMATION_MESSAGE);

            // Bucle de Entrada y Validación de Fecha/Hora
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            
            while (true) {
                int accion = JOptionPane.showConfirmDialog(null, "¿Desea dar de alta una función de la película " +
                    unaPeli.getNombrePelicula() + "?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (accion == JOptionPane.NO_OPTION || accion == -1) return;

                // Entrada de Fecha
                String fechaInput = JOptionPane.showInputDialog("Ingrese la fecha de la función (AAAA/MM/DD)", "Agregar Función");
                if (fechaInput == null) continue; // Si cancela, vuelve a preguntar

                LocalDate fechaFuncion;
                try {
                    fechaFuncion = LocalDate.parse(fechaInput.trim(), dateFormatter);
                } catch (DateTimeParseException dtpe) {
                    JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use AAAA/MM/DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Entrada y Validación de Hora/Minutos 
                String horaStr = JOptionPane.showInputDialog("Ingrese la hora (00 - 23)");
                if (horaStr == null) continue;
                String minStr = JOptionPane.showInputDialog("Ingrese los minutos (00 - 59)");
                if (minStr == null) continue;
                
                int h, m;
                LocalTime horaFuncion;
                try {
                    h = Integer.parseInt(horaStr);
                    m = Integer.parseInt(minStr);
                    if (h < 0 || h > 23 || m < 0 || m > 59) {
                        throw new NumberFormatException();
                    }
                    horaFuncion = LocalTime.of(h, m);
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Hora o minutos no válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Validación de Tiempo Pasado 
                LocalDate hoy = LocalDate.now();
                LocalTime horaActual = LocalTime.now();

                // Valida si la fecha es pasada O si es HOY y la hora ya pasó.
                if (fechaFuncion.isBefore(hoy) || (fechaFuncion.isEqual(hoy) && horaFuncion.isBefore(horaActual))) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "La función no puede ser en el pasado. La hora debe ser futura si la fecha es hoy.", 
                        "Error de Tiempo Pasado", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                // Validamos el intervalo
                String fechaFinal = fechaFuncion.toString().replace('-', '/');
                String horaFinal = String.format("%02d%02d", h, m);

                if (!gestor.validarIntervaloEntreFunciones(salaSeleccionada, fechaFinal, horaFinal, unaPeli)) {
                    JOptionPane.showMessageDialog(null, "Existe otra función programada a menos de 30 minutos para esta sala. Cambia la hora o la sala.");
                    continue;
                }

                Funcion nuevaFuncion = new Funcion(fechaFinal, horaFinal, salaSeleccionada, unaPeli); 
                
                // Generamos un ID descriptivo
                String idFuncion = nuevaFuncion.getNombrePelicula().replace(" ", "") + "|" + fechaFinal.replace("/", "") + "|" + horaFinal + "|" + salaSeleccionada.replace(" ", "");
                
                gestor.guardarFuncionesEnArchivo(nuevaFuncion);
                JOptionPane.showMessageDialog(null, "Función registrada con el siguiente ID:\n" + idFuncion);
                break; 
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Función para mostrar el menu del administrador
    public static void menuAdmin(GestorDeArchivos gestor, Validaciones v, Administrador admin) throws IOException {
        List<Administrador> administradores = gestor.cargarAdmin();
        do {
            String[] opciones = {"Agregar Película a cartelera", "Agregar Función", "Registrar Empleado", "Ver boletos comprados de un cliente", "Cerrar sesión"};
            int opcion = JOptionPane.showOptionDialog(null, "Bienvenido/a, " + admin.getNombre() + 
            "!\n\n¿Qué acción desea realizar?", "Administradores de CinePOOlis",
            JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        
            switch (opcion) {
                case 0 -> agregarPeliculaACartelera(gestor);
                case 1 -> agregarFuncion(gestor);
                case 2 -> registroEmpleado(gestor, v, administradores);
                // case 3 -> 
                default -> {
                    JOptionPane.showMessageDialog(null, "Cerrando sesión...");
                    return;
                }
            }
            // agregarFuncion(funciones, cartelera, opcion)
        } while (true);
    }
    // Funcion que registra a los empleados
    public static void registroEmpleado(GestorDeArchivos gestor, Validaciones v, List<Administrador> administradores) {
        String nombre, apellidoP, apellidoM, numeroCelular = "", nickname = "";
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

            boolean celularUnico = false;
            while (!celularUnico) {
                numeroCelular = JOptionPane.showInputDialog(null, "Ingrese su número telefónico", 
                "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                if (numeroCelular == null) return;

                if (v.existeNumeroAdmin(administradores, numeroCelular)) {
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
                
                if (v.existeNicknameAdmin(administradores, nickname)) {
                    JOptionPane.showMessageDialog(null, "Error. El nickname ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    nicknameUnico = true;
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
                    } else if(v.existeCorreoAdmin(administradores, correo)) {
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
            String[] opcionesTurno = {"Matutino", "Vespertino", "Nocturno"};
            int turnoInt = JOptionPane.showOptionDialog(null,
                    "Ingrese turno del empleado", "Registro de Empleado", JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE,
                null, opcionesTurno, opcionesTurno[0]);

            turno = opcionesTurno[turnoInt];

            // Crear cuenta y objeto Empleado
            Cuenta cuenta = new Cuenta(nickname, password, correo);

            // Aquí se elige el tipo de empleado
        String[] opcionesEmpleado = {"Administrador", "Vendedor"};
        int tipo = JOptionPane.showOptionDialog( null,"Seleccione el tipo de empleado a registrar","Tipo de Empleado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesEmpleado,
                opcionesEmpleado[0]
        );

        Empleado empleado = null;

        switch (tipo) {
            case 0 -> {
                String[] opcionesDia = {"Entre Semana", "Fin de Semana"};
                int diasTrabajoInt = JOptionPane.showOptionDialog(null, "Ingrese los días de trabajo del Administrador", "Registro Administrador",
                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, opcionesDia, opcionesDia[0]);

                String diasTrabajo = opcionesDia[diasTrabajoInt];

                empleado = new Administrador(
                        nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno, diasTrabajo
                );
            }

            case 1 -> {
                String[] opcionesDia = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                int diaDescansoInt = JOptionPane.showOptionDialog(null, "Ingrese el dia de descanso del vendedor", "Registrar vendedor",
                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, opcionesDia, opcionesDia[0]);

                String diaDescanso = opcionesDia[diaDescansoInt];

                empleado = new Vendedor(
                        nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno, diaDescanso
                );
            }

            default -> {
                JOptionPane.showMessageDialog(null, "Registro cancelado.");
                return;
            }
        }
              // Guardar en archivo
            gestor.guardarUsuarios(empleado);
            //usuariosEnSistema.add(personaRegistrada);

            JOptionPane.showMessageDialog(null, "Empleado registrado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
