import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente extends Persona {
    // Constantes para los métodos del cliente
    private static final int[] PRECIOS_PALOMITAS = {60, 70, 80, 90};
    private static final int[] PRECIOS_NACHOS = {70, 80, 90, 100};
    private static final int[] PRECIOS_REFRESCO = {45, 50, 60, 70};

    // Atributos
    private String tarjetaBancaria;

    // Constructor
    public Cliente(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String tarjetaBancaria) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta);
        this.tarjetaBancaria = tarjetaBancaria;
    }

    // Getters y Setters
    public String getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(String tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    // Métodos

    // Método para mostrar el menú del cliente
    public static void menuCliente (GestorDeArchivos gestor, Cliente cliente) {
        // Definimos las variables fuera del bucle do-while
        List<Cliente> clientes = new ArrayList<>(); 
        List<Pelicula> cartelera;
        List<Funcion> funciones;
        List<Boleto> boletos;
        List<String> ordenes;
        Validaciones v = new Validaciones();
        int opcion;
        final int CERRAR_SESION = 4; // Índice de la opción "Cerrar Sesión"
        final String[] opciones = {"Ver cartelera", "Comprar boletos", "Comprar en dulcería", "Notificaciones", "Cerrar Sesión"};
        
        // Bucle para mantener al usuario en el menú hasta que elija "Cerrar Sesión"
        do {
            try {
                // Recargamos archivos en cada iteración por si han sido modificados
                clientes = gestor.cargarClientes(); 
                cartelera = gestor.cargarPeliculas();
                // Asegúrate de que gestor.mostrarFunciones maneje listas vacías.
                funciones = gestor.mostrarFunciones(cartelera); 
                boletos = gestor.cargarBoletosEnArchivo(cartelera);
                ordenes = gestor.cargarOrdenesDeUsuario(cliente);
                
                // Mostrar diálogo
                opcion = JOptionPane.showOptionDialog(
                    null, 
                    "Bienvenido/a, " + cliente.getNombre() + "!\n\n¿Qué acción desea realizar?", 
                    "Clientes de CinePOOlis",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones, 
                    opciones[0]
                );

                // Manejo de la opción
                switch (opcion) {
                    case 0 -> { 
                        verCartelera(cartelera);
                    }
                    case 1 -> {
                        comprarBoletos(cartelera, funciones, gestor, cliente);
                    }
                    case 2 -> {
                        List<Combo> comanda = new ArrayList<>();
                        Orden orden = new Orden(comanda); 
                        
                        // Asegurarse de que el método comprarDulceria se ejecute, pero sin un loop explícito aquí.
                        comprarDulceria(orden, gestor, cliente);
                    }
                    case 3 -> {
                        revisarNotificaciones(gestor, cliente, funciones, boletos, ordenes, cartelera);
                    }
                    case CERRAR_SESION -> {
                        // El bucle terminará y el control volverá al método llamador.
                        JOptionPane.showMessageDialog(null, "Cerrando sesión...", "Cerrar Sesión", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case JOptionPane.CLOSED_OPTION -> { // Manejo explícito del -1
                        // El usuario cerró la ventana, también cerramos la sesión.
                        opcion = CERRAR_SESION;
                    }
                }
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar datos o procesar la orden: " + e.getMessage(), "Error de IO", JOptionPane.ERROR_MESSAGE);
                opcion = CERRAR_SESION; // Forzar salida del menú ante un error grave de IO
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error desconocido: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                opcion = CERRAR_SESION; // Forzar salida del menú ante un error desconocido
            }
            
        } while (opcion != CERRAR_SESION);

        // Volver al menú principal después de cerrar sesión
        CinePOOlis.menuPrincipal(gestor, v, clientes); 
    }

    // Método para mostrar la cartelera de películas
    public static void verCartelera (List<Pelicula> cartelera) { // La lista de películas son solo las que están en cartelera
        StringBuilder carteleraStr = new StringBuilder("Cartelera de Películas:\n\n");
        try {
            if (cartelera.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay películas en la cartelera en este momento.", "Cartelera Vacía", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                for (Pelicula pelicula : cartelera) {
                carteleraStr.append("Nombre: ").append(pelicula.getNombrePelicula()).append("\n")
                            .append("Género: ").append(pelicula.getGenero()).append("\n")
                            .append("Sinopsis: ").append(pelicula.getSinopsis()).append("\n")
                            .append("Duración: ").append(pelicula.getDuracion()).append("\n\n");
                }

                // Creamos un JScrollPane para mostrar la cartelera en un área de texto desplazable
                JScrollPane scrollPane = new JScrollPane(new JTextArea(carteleraStr.toString())); // Área de texto dentro del JScrollPane
                scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

                JOptionPane.showMessageDialog(null, scrollPane, "Cartelera", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la cartelera: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para comprar boletos (Retorna la clave de los boletos)
    public static void comprarBoletos(List<Pelicula> cartelera, List<Funcion> funciones, GestorDeArchivos gestor, Cliente cliente) {
        try {
            List<Boleto> boletos = new ArrayList<>();
            do {
                try {
                    // Seleccionamos la función
                    Funcion funcionSeleccionada = seleccionarFuncion(funciones);
                    if (funcionSeleccionada == null) break;
                    
                    // Seleccionamos los asientos
                    boletos = seleccionarAsiento(funcionSeleccionada, gestor, cliente);
                    
                    if (boletos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se compraron boletos.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                    
                    // Obtenemos el precio total
                    int precioTotal = 0;
                    for (Boleto b : boletos) {
                        precioTotal += b.getPrecio();
                    }
                    
                    // Mostramos los boletos comprados
                    mostrarBoletosComprados(boletos, precioTotal);  
                    
                    // Guardar boletos
                    for(Boleto b : boletos) {
                        gestor.guardarBoletosEnArchivo(b);
                    }
                    
                    break;
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al comprar boletos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            } while (true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para mostrar los boletos comprados
    private static void mostrarBoletosComprados (List<Boleto> boletos, int precioTotal) {
        try {
            StringBuilder boletosStr = new StringBuilder();
            if (boletos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay películas en la cartelera en este momento.", "Cartelera Vacía", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    for (Boleto b : boletos) {
                    boletosStr.append("Película: ").append(b.getNombrePelicula()).append("\n") // 1. Se quitó nombrePelicula
                                .append("Horario: ").append(b.getFecha()).append(" - ").append(b.getHora()).append("\n")
                                .append("Asiento: ").append(b.getAsiento()).append("\n")
                                .append("Clave: ").append(b.toString());
                }
                boletosStr.append("Precio total: $").append(precioTotal).append("\n");

                // Creamos un JScrollPane para mostrar los boletos comprados en un área de texto desplazable
                JScrollPane scrollPane = new JScrollPane(new JTextArea(boletosStr.toString())); // Área de texto dentro del JScrollPane
                scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

                JOptionPane.showMessageDialog(null, scrollPane, "Boletos Comprados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los boletos comprados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para seleccionar función
    private static Funcion seleccionarFuncion(List<Funcion> funciones) {

        if (funciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay funciones programadas en este momento.", "Cartelera Vacía", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        int i = 1;

        StringBuilder funcionesStr = new StringBuilder("Cartelera de Películas:\n\n");
        for (Funcion f : funciones) {
            funcionesStr.append((i)).append(". ").append(f.getNombrePelicula()).append("\n")
                .append("ID película: ").append(f.getIdPelicula()).append("\n")
                .append("Fecha: ").append(f.getFecha()).append("\n")
                .append("Hora: ").append(f.getHora()).append("\n")
                .append("Sala: ").append(f.getSala()).append("\n\n");
                i++;
        }

        // Creamos un JScrollPane para mostrar las funciones seleccionables
        JTextArea textArea = new JTextArea(funcionesStr.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea); // Área de texto dentro del JScrollPane
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Funciones Disponibles", JOptionPane.INFORMATION_MESSAGE);
        int seleccion = -1;
        boolean seleccionValida = false;
        do {
            try {
                String input = JOptionPane.showInputDialog(null, 
                    "Ingrese el índice de la función que quieres asistir", "1");
                
                if (input == null) return null; 

                seleccion = Integer.parseInt(input);

                if (seleccion > 0 && seleccion <= funciones.size()) {
                    seleccion = seleccion - 1; // Ajustamos a índice base 0
                    seleccionValida = true;
                } else {
                    JOptionPane.showMessageDialog(null, 
                    "Número de función fuera del rango. Ingrese un número entre 1 y " + funciones.size(), 
                    "Error de Rango", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar la cartelera: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (!seleccionValida);
        return funciones.get(seleccion);
    }

    // Método auxiliar para seleccionar asiento
    public static List<Boleto> seleccionarAsiento(Funcion funcion, GestorDeArchivos gestor, Cliente cliente) {
        String[][] asientos = gestor.cargarAsientos(funcion);

        if (asientos == null) {
            JOptionPane.showMessageDialog(null, "Error grave: No se pudo cargar el mapa de asientos de la sala.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }

        // Mostrar el mapa de asientos
        verAsientos(asientos, gestor, funcion);
        
        int asientosASeleccionar = 0;
        boolean cantidadValida = false;
        
        // Bucle para validar la cantidad de asientos a reservar (máx 10)
        do {
            String inputCantidad = JOptionPane.showInputDialog(
                null, 
                "Ingresa el número de asientos que deseas reservar (máximo 10):",
                "Seleccionar Asientos", 
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Manejar cancelación
            if (inputCantidad == null) {
                return new ArrayList<>(); // El usuario canceló la selección
            }

            try {
                asientosASeleccionar = Integer.parseInt(inputCantidad.trim());
                
                // Validar el rango
                if (asientosASeleccionar <= 0 || asientosASeleccionar > 10) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Selección fuera de rango. Elige entre 1 y 10 asientos.",
                        "Error de Rango", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    cantidadValida = true; // La cantidad es válida
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Entrada inválida. Por favor, ingresa un número entero.", 
                    "Error de Formato", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } while (!cantidadValida);

        // Creamos dos ArrayList para almacenar los índices y las etiquetas
        List<String> asientosTemporales = new ArrayList<>(); 
        List<int[]> indicesSeleccionados = new ArrayList<>();
        // Otro más para almacenar los boletos
        List<Boleto> boletosComprados = new ArrayList<>();

        for (int i = 0; i < asientosASeleccionar; i++) {
            boolean asientoReservado = false;
            
            // Bucle interno para forzar la selección correcta de CADA asiento
            do {
                String asientoSeleccionado = JOptionPane.showInputDialog(
                    null, 
                    "Asiento " + (i + 1) + " de " + asientosASeleccionar + 
                    "\nIngresa el asiento a reservar (ejemplo: A1, B10):",
                    "Seleccionar Asiento",
                    JOptionPane.QUESTION_MESSAGE
                );

                if (asientoSeleccionado == null) {
                    // Si cancela a mitad de selección, detenemos el proceso
                    JOptionPane.showMessageDialog(null, "Proceso de reserva cancelado.", "Cancelado", JOptionPane.WARNING_MESSAGE);
                    return new ArrayList<>();
                }
                
                asientoSeleccionado = asientoSeleccionado.trim().toUpperCase();

                if (!asientoSeleccionado.matches("^[A-Z][1-9][0-9]?$")) {
                    JOptionPane.showMessageDialog(null, "Formato inválido. Ejemplo: A1 o B10. La letra debe ser mayúscula.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    // Parseo mejorado de la letra y el número
                    String letraStr = asientoSeleccionado.substring(0, 1);
                    String numeroStr = asientoSeleccionado.substring(1);
                    
                    int indiceFila = gestor.obtenerNumeroFila(letraStr);
                    int indiceColumna = Integer.parseInt(numeroStr) - 1;

                    // Validar que los índices estén dentro de los límites de la matriz
                    if (asientos == null || indiceFila < 0 || indiceFila >= asientos.length) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "Fila " + letraStr + " no existe en esta sala.", 
                            "Error de Rango", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }
                    
                    // Validar que los índices estén dentro de los límites de la matriz
                    if (indiceColumna < 0 || indiceColumna >= asientos[0].length) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "Columna " + (indiceColumna + 1) + " no existe en esta sala.", 
                            "Error de Rango", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }
                    
                    String asientoActual = asientos[indiceFila][indiceColumna];

                    if (asientoActual == null) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "El asiento " + asientoSeleccionado + " es inválido (posible pasillo).",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    } else if (asientoActual != null && asientoActual.endsWith("[O]")) {
                        // Marcar asiento como reservado
                        asientos[indiceFila][indiceColumna] = asientoActual.replace("[O]", "[X]");

                        asientosTemporales.add(asientoSeleccionado);
                        indicesSeleccionados.add(new int[]{indiceFila, indiceColumna});
                        asientoReservado = true; // Salimos del bucle do-while
                        JOptionPane.showMessageDialog(
                            null, 
                            "Asiento " + asientoSeleccionado + " reservado con éxito.",
                            "Asiento Reservado", 
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else if (asientoActual.endsWith("[X]")) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "El asiento " + asientoSeleccionado + " ya está reservado. Elige otro.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        // Captura asientos que son pasillos ("   ") o inválidos
                        JOptionPane.showMessageDialog(
                            null, 
                            "El asiento " + asientoSeleccionado + " es inválido (posible pasillo).",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Error: El número de columna es inválido.", 
                        "Error de Formato", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } 
            } while (!asientoReservado);
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, 
            "¿Confirmas la compra de " + asientosASeleccionar + " boletos?", 
            "Confirmar Compra", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Proceso de Pago
                ThreadBancario procesoPago = new ThreadBancario();
                Thread hiloProceso = new Thread(procesoPago, "Compra de boletos");
                hiloProceso.start();
                hiloProceso.join(); 

                // Crear los boletos y agregarlos
                for (String asiento : asientosTemporales) {
                    Boleto boleto = new Boleto(funcion.getFecha(), funcion.getHora(), funcion.getSala(), funcion.getPelicula(), asiento, cliente.getNicknameCuenta());
                    boletosComprados.add(boleto);
                }
                gestor.guardarAsientos(funcion, asientos);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "El proceso de pago fue interrumpido. Revertiendo reservas.", "Error de Pago", JOptionPane.ERROR_MESSAGE);
            
                for (int[] indices : indicesSeleccionados) {
                    String asientoX = asientos[indices[0]][indices[1]];
                    asientos[indices[0]][indices[1]] = asientoX.replace("[X]", "[O]");
                }
                
                try {
                    gestor.guardarAsientos(funcion, asientos);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Error al guardar la reversión: " + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return new ArrayList<>(); // Devolver lista vacía
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar los boletos comprados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                // Podrías decidir si revertir aquí si el error de guardar implica que la reserva no es segura.
            }
        } else {
            // Cancelación del Usuario: Revertir y Guardar
            if (!indicesSeleccionados.isEmpty()) { 
                JOptionPane.showMessageDialog(null, "Compra cancelada por el usuario. Revertiendo asientos seleccionados.", "Cancelado", JOptionPane.WARNING_MESSAGE);
                
                for (int[] indices : indicesSeleccionados) {
                    // Revertir el asiento a disponible
                    String asientoX = asientos[indices[0]][indices[1]];
                    asientos[indices[0]][indices[1]] = asientoX.replace("[X]", "[O]");
                }
                
                try {
                    // Es vital guardar el estado revertido para que esté disponible para otros usuarios
                    gestor.guardarAsientos(funcion, asientos);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar la reversión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }  
        return boletosComprados;
    }

    // Método auxiliar para ver asientos según la sala
    
    public static void verAsientos(String[][] asientos, GestorDeArchivos gestor, Funcion funcion) {

        StringBuilder asientosStr = new StringBuilder();

        // Si la matriz de asientos está vacía o es nula, no imprimimos nada
        if (asientos == null || asientos.length == 0) {
            asientosStr.append("No hay datos de asientos disponibles.");
        } else {
            // Hacemos un switch para imprimir los índices correctos de la sala
            switch (funcion.getSala().toUpperCase()) {
                case "A", "SALA A", "B", "SALA B" -> {
                    // Asignamos un valor a las columnas
                    int numColumnas = asientos[0].length;
                    asientosStr.append("      "); // Espacio para la letra de fila
                    for (int j = 0; j < numColumnas; j++) {
                        // Formato de impresión estandarizada para las columnas
                        asientosStr.append(String.format(" %-4s", j + 1)); 
                    }
                    asientosStr.append("\n"); 

                    for (int i = 0; i < asientos.length; i++) {
                        // Añadir la letra de la fila 
                        String letraFila = gestor.obtenerLetraFila(i); 
                        asientosStr.append(letraFila).append("      ");
                        
                        for (String asiento : asientos[i]) {
                            // Formato de impresión estandarizada para el asiento
                            asientosStr.append(String.format(" %-4s", asiento));
                        }
                        asientosStr.append("\n"); 
                    }
                }
                case "VIP", "SALA VIP" -> {
                    asientosStr.append("      "); // Espacio para la letra de fila
                    int numAsiento = 0;
                    
                    for (int j = 0; j < asientos[0].length; j++) {
                        // Pasillos verticales
                        if (j == 2 || j == 5) {
                            asientosStr.append("      "); 
                        } else {
                            numAsiento++;
                            // Imprimimos el número de asiento
                            asientosStr.append(String.format(" %-4s", numAsiento)); 
                        }
                    }
                    asientosStr.append("\n"); 

                    // Imprimir el cuerpo de la matriz (mismo código que A y B)
                    for (int i = 0; i < asientos.length; i++) {
                        // Añadir la letra de la fila 
                        String letraFila = gestor.obtenerLetraFila(i); 
                        asientosStr.append(letraFila).append("\t");
                        
                        for (String asiento : asientos[i]) {
                            // Formato de impresión estandarizada para el asiento
                            asientosStr.append(String.format(" %-4s", asiento));
                        }
                        asientosStr.append("\n"); 
                    }
                } 
            }
        }

        // Mostramos los asientos en un JScrollPane
        JTextArea textArea = new JTextArea(asientosStr.toString());
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14)); 
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

        JOptionPane.showMessageDialog(
        null, 
        scrollPane, // <-- Pasamos el JScrollPane con el JTextArea formateado
        "Asientos Disponibles", 
        JOptionPane.PLAIN_MESSAGE
        );
    }

    // Método para comprar en la dulcería
    public static Orden comprarDulceria (Orden orden, GestorDeArchivos gestor, Cliente cliente) {

        // Arreglo para almacenar las opciones disponibles
        String[] opcionesCombo = {"Combo amix", "Combo nachos", "Combo buen trío", "Combo ¿Qué me ves?", "Orden personalizada"};
        String[] opcionesAlimentos = {"Palomitas", "Nachos", "Refresco"};
        String[] tamaniosAlimentos = {"Mediano", "Grande", "Jumbo", "Mega"};
        int tamanio = -1, seleccion = -1, precio = 0;
        String sabor = "";
        Combo combo = new Combo("", 0); // Inicialización de la variable combo
        Alimento alimento = new Alimento("", "", 0); // Inicialización de la variable alimento
        List<Combo> comanda = orden.getOrden();

        try {
            // Solicitamos al usuario que elija un combo o una orden personalizada
            seleccion = JOptionPane.showOptionDialog(null,"Buen día y bienvenido a la dulcería." + 
            "\n¿Te gustaría ordenar un combo o una orden personalizada?",
            "Dulcería", JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opcionesCombo,
            opcionesCombo[0]);

            if (seleccion == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Compra de dulcería cancelada.", "Cancelación", JOptionPane.INFORMATION_MESSAGE);
                return null; // Devolver NULL para indicar la cancelación a menuCliente
            }

            switch(seleccion) {
                case 0:
                    comanda.add(combo.crearComboAmix());
                    break;
                case 1:
                    comanda.add(combo.crearComboNachos());
                    break;
                case 2:
                    comanda.add(combo.crearComboBuenTrio());  
                    break;
                case 3:
                    comanda.add(combo.crearComboQueMeVes());
                    break;
                case 4:
                    boolean agregarMas = true;
                    Combo ordenPersonalizada = new Combo("Orden personalizada", 0); // Inicialización de la orden personalizada
                    while (agregarMas) {
                        int seleccionAlimento = JOptionPane.showOptionDialog(null,"¿Qué deseas ordenar?",
                        "Orden personalizada", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        opcionesAlimentos,
                        opcionesAlimentos[0]);

                        tamanio = seleccionarTamanio(opcionesAlimentos[seleccionAlimento], tamaniosAlimentos);
        
                        if (tamanio == JOptionPane.CLOSED_OPTION) continue; // Salta al inicio del while si se cancela el tamaño

                        switch(seleccionAlimento) {
                            case 0:
                                precio = PRECIOS_PALOMITAS[tamanio];
                                sabor = obtenerSaborPalomitas();
                                alimento = new Palomitas("Palomitas", tamaniosAlimentos[tamanio], precio, sabor);
                                break;
                            case 1:
                                precio = PRECIOS_NACHOS[tamanio];
                                alimento = new Alimento("Nachos", tamaniosAlimentos[tamanio], precio);
                                break;
                            case 2:
                                precio = PRECIOS_REFRESCO[tamanio];
                                sabor = obtenerSaborRefresco();
                                alimento = new Refresco("Refresco", tamaniosAlimentos[tamanio], precio, sabor);
                                break;
                        }
                        ordenPersonalizada.agregarAlimento(alimento);

                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Quieres agregar otro alimento?", "Agregar más", JOptionPane.YES_NO_OPTION);
                        if (respuesta == JOptionPane.NO_OPTION) {
                            agregarMas = false;
                        }
                    }
                    comanda.add(ordenPersonalizada);
                    break;
            }

            orden.setOrden(comanda);
            // Creamos los hilos que muestran el proceso del pago
            ThreadBancario procesoPago = new ThreadBancario();
            Thread hiloProceso = new Thread(procesoPago, "Compra en dulcería");

            hiloProceso.start();
            
            try {
                hiloProceso.join(); 
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "El proceso de pago fue interrumpido: " + e.getMessage(), "Error de Pago", JOptionPane.ERROR_MESSAGE);
                // Cancelamos la compra de boletos.
            }
            
            // Algoritmo para obtener el ID de la orden
            String idCliente = cliente.generarIdNombre();
            String idOrden = generarClaveDulceria(idCliente);
            gestor.guardarOrdenesDeDulceria(idOrden);

            // Hilo de integración
            ThreadIntegrador integrar = new ThreadIntegrador(idOrden, orden, gestor);
            Thread hiloIntegrador = new Thread(integrar, "Integración de Orden Dulcería");

            hiloIntegrador.start();

            JOptionPane.showMessageDialog(null, "Revisa la sección de notificaciones para saber \ncuando tu orden de dulcería esté lista 😉",
            "Finalizar orden", JOptionPane.INFORMATION_MESSAGE);
            return orden;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método auxiliar para seleccionar el tamaño del alimento
    private static int seleccionarTamanio(String tipoAlimento, String[] tamaniosAlimentos) {
        return JOptionPane.showOptionDialog(null, "Selecciona el tamaño de " + tipoAlimento, "Tamaño de " + tipoAlimento,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                tamaniosAlimentos,
                tamaniosAlimentos[1]);
    }

    // Método auxiliar para obtener el sabor de las palomitas
    private static String obtenerSaborPalomitas() {
        String[] sabores = {"Mantequilla", "Queso", "Jalapeño", "Caramelo"};
        int saborSeleccionado = JOptionPane.showOptionDialog(null, "Selecciona el sabor de las palomitas", "Sabor de palomitas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                sabores,
                sabores[0]);
        return sabores[saborSeleccionado];
    }

    // Método auxiliar para obtener el sabor del refresco
    private static String obtenerSaborRefresco() {
        String[] sabores = {"Cola", "Cola Light", "Naranja", "Manzana", "Toronja"};
        int saborSeleccionado = JOptionPane.showOptionDialog(null, "Selecciona el sabor del refresco", "Sabor de refresco",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                sabores,
                sabores[0]);
        return sabores[saborSeleccionado];
    }

    // Método para generar clave de dulcería
    public static String generarClaveDulceria (String id) {
        // Creamos instancias que manejan el tiempo
        ZonedDateTime tiempoCompleto = ZonedDateTime.now();

        // Patrón de la fecha
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fechaFormateada = tiempoCompleto.format(formatoFecha);

        // Patrón de la hora
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HHmm");
        String horaFormateada = tiempoCompleto.format(formatoHora);

        // Generamos la clave
        return id + "|" + fechaFormateada + "|" + horaFormateada;
    }

    // Método para revisar notificaciones
    public static void revisarNotificaciones(GestorDeArchivos gestor, Cliente cliente, List<Funcion> funcionesCargadas, List<Boleto> boletosCargados, List<String> ordenes, List<Pelicula> cartelera) {
        String [] opciones = {"Revisar órdenes de compra para una función", "Revisar notificaciones de dulcería", "Regresar al menú"};
        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione el tipo de notificación que desee revisar",
        "Revisar notificaciones", JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

        switch (seleccion) {
            case 0 -> { 
                try {
                    List<Funcion> funcionesConBoleto = verificarFuncion(funcionesCargadas, boletosCargados);
                    if(funcionesConBoleto.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No tienes boletos comprados para funciones activas.", "Notificaciones de funciones", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }

                    StringBuilder funcionesStr = new StringBuilder("\nFunciones:");
                    int i = 1;
                    for (Funcion f : funcionesConBoleto) {
                        funcionesStr.append(i).append(". ").append(f.getNombrePelicula())
                        .append(f.getFecha()).append(f.getHora());
                        i++;
                    }

                    String inputSeleccion = JOptionPane.showInputDialog(null,
                    "Ingrese el número de la función que quiere ver a detalle: " + funcionesStr, "1");
                            
                    if (inputSeleccion == null) { 
                        revisarNotificaciones(gestor, cliente, funcionesCargadas, boletosCargados, ordenes, cartelera); 
                        return;
                    }

                    int seleccionUno = Integer.parseInt(inputSeleccion);

                    if (seleccionUno <= 0 || seleccionUno > funcionesConBoleto.size()) {
                        JOptionPane.showMessageDialog(null, "No existe la función " + seleccionUno + ".", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    Funcion funcionSelec = funcionesConBoleto.get(seleccionUno - 1);

                    List<Boleto> boletosFuncion = gestor.cargarBoletosPorFuncion(boletosCargados, funcionSelec);

                    StringBuilder boletosClave = new StringBuilder("\nBoletos comprados:\n");
                    for (Boleto b : boletosFuncion) boletosClave.append(b.toString()).append("\n");
                    
                    String funcionStr = "\nFunción #" + seleccion + "\n" + funcionSelec.getNombrePelicula() + 
                    "\nHorario: " + funcionSelec.getFecha() + ", " + funcionSelec.getHora() + boletosClave;

                    JOptionPane.showMessageDialog(null, funcionStr, "Notificaciones de funciones", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            case 1 -> { // Revisar notificaciones de dulcería
                boolean reintentar = false;
                do {
                    try {
                        String idCliente = cliente.generarIdNombre();
                        
                        // Filtrar las órdenes compradas por este cliente.
                        List<String> ordenesDelCliente = ordenes.stream()
                                                        .filter(o -> o.startsWith(idCliente + "|"))
                                                        .toList();

                        // Cargar las órdenes que ya fueron terminadas por el ThreadIntegrador
                        List<String> ordenesTerminadas = gestor.cargarHistorialDeDulceria(); 
                        
                        if (ordenesDelCliente.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No tienes órdenes de dulcería compradas.", "Notificaciones de Dulcería", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                        
                        // Mostrar la lista numerada al cliente
                        StringBuilder ordenesStr = new StringBuilder("\nÓrdenes de " + cliente.getNombre() + "\n");
                        for (int i = 0; i < ordenesDelCliente.size(); i++) {
                            // Muestra las claves para que el cliente seleccione.
                            ordenesStr.append(i + 1).append(". ").append(ordenesDelCliente.get(i)).append("\n");
                        }

                        // Pedir al cliente que seleccione una orden.
                        String inputSeleccion = JOptionPane.showInputDialog(null,
                            "Órdenes de Dulcería: \n" + ordenesStr.toString() + 
                            "\nIngrese el número de la orden que desea verificar:", "1");
                        
                        if (inputSeleccion == null) break; // Si cancela

                        int indiceSeleccionado = Integer.parseInt(inputSeleccion) - 1;
                        
                        if (indiceSeleccionado < 0 || indiceSeleccionado > ordenesDelCliente.size()) {
                            JOptionPane.showMessageDialog(null, "Selección inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                            reintentar = true;
                            continue;
                        }
                        
                        String claveSeleccionada = ordenesDelCliente.get(indiceSeleccionado);
                        
                        if (ordenesTerminadas.contains(claveSeleccionada)) {
                        String idVendedorLog = cliente.generarIdNombre(); 

                        String logDetallado = gestor.buscarOrdenEnHistorialVendedor(claveSeleccionada, idVendedorLog);
                        
                        String nombreVendedor;
                        
                        if (logDetallado != null) {
                            // Cargamos todos los usuarios para encontrar el nombre del vendedor
                            List<Vendedor> vendedores = gestor.cargarVendedores();
                            
                            // Buscamos el objeto Vendedor 
                            nombreVendedor = vendedores.stream()
                                .filter(v -> (v.generarIdNombre().equals(idVendedorLog)))
                                .map(Vendedor::getNombre)
                                .findFirst()
                                .orElse("Vendedor Desconocido"); // Usar fallback

                        } else {
                            // Fallback si no se puede leer el log del vendedor (problema de archivo)
                            nombreVendedor = "Vendedor Temporal"; 
                        }

                        // Formato de la clave: ID|AAAAMMDD|hhmm
                        String[] partesClave = claveSeleccionada.split("\\|");
                        String fechaHoraTerminacionStr = partesClave[1] + ":" + partesClave[2]; // AAAAMMDD:hhmm

                        String mensajeListo = String.format(
                            "Hola, soy %s. Ya está lista tu orden de dulcería. Puedes pasar a recogerla en la fila de dulcería para ventas de la app. %s",
                            nombreVendedor, fechaHoraTerminacionStr
                        );

                        gestor.guardarMensajeNotificacion(mensajeListo);
                            
                        } else {
                            // Orden en progreso (La clave existe en 'ordenesDelCliente' pero no en 'ordenesTerminadas').
                            String mensajeProgreso = "Estamos trabajando arduamente para que tus alimentos sean deliciosos. Por favor, espera un poco más =D";
                            gestor.guardarMensajeNotificacion(mensajeProgreso);
                        }
                        
                        // 5. Mostrar el mensaje al cliente leyendo el archivo
                        String mensajeFinal = gestor.leerMensajeNotificacion();
                        JOptionPane.showMessageDialog(null, mensajeFinal, "Estado de la Orden: " + claveSeleccionada, JOptionPane.INFORMATION_MESSAGE);
                        reintentar = false;
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        reintentar = true;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al revisar notificaciones: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                        reintentar = false;
                    }
                } while (reintentar);
            }
            default -> menuCliente(gestor, cliente);
        }
    }

    // Método auxiliar para comprobar si hay boletos comprados en una función
    public static List<Funcion> verificarFuncion(List<Funcion> funcionesCargadas, List<Boleto> boletosCargados) {
        // Lista para almacenar las funciones que tengan boletos comprados por el usuario
        List<Funcion> funcionesCompradas = new ArrayList<>();

        // Comprobamos si hay boletos comprados por el usuario en esa película
        for (Funcion f : funcionesCargadas) {
            boolean hasBoletos = false;
            for (Boleto b : boletosCargados) {
                boolean esMismoId = f.getIdPelicula().equals(b.getIdPelicula());
                boolean esMismoNombre = f.getNombrePelicula().equals(b.getNombrePelicula());
                boolean esMismaFecha = f.getFecha().equals(b.getFecha());
                boolean esMismaHora = f.getHora().equals(b.getHora());
                boolean esMismaSala = f.getSala().equals(b.getSala());

                if (esMismoId && esMismoNombre && esMismaFecha && esMismaHora && esMismaSala) {
                    hasBoletos = true; // Se encontró un boleto para esta función
                    break;             // No necesitamos revisar más boletos para esta función
                }
            }

            if (hasBoletos) {
                funcionesCompradas.add(f);
            }
        }
        return funcionesCompradas;
    }
    
    // Método para obtener el precio total
    public static int obtenerPrecioTotal(List<Boleto> boletos) {
        int precioTotal = 0;
        for (Boleto b : boletos) {
            precioTotal += b.getPrecio();
        }
        return precioTotal;
    }
}