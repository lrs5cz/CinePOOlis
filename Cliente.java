import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.IOException;
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
        try {
            // Cargamos archivos
            List<Persona> usuarios = gestor.cargarUsuarios();
            List<Pelicula> cartelera = gestor.cargarPeliculas();
            List<Funcion> funciones = gestor.mostrarFunciones(cartelera);
            List<Boleto> boletos = gestor.cargarBoletosEnArchivo(cartelera);
            List<String> ordenes = gestor.cargarOrdenesDeUsuario(cliente);
            String[] opciones = {"Ver cartelera", "Comprar boletos", "Comprar en dulcería", "Notificaciones", "Cerrar Sesión"};
            Validaciones v = new Validaciones();
            int opcion = JOptionPane.showOptionDialog(null, "Menú del cliente.\n\n¿Qué acción desea realizar?", "CinePOOlis",
            JOptionPane.PLAIN_MESSAGE,  JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0 -> { 
                    verCartelera(cartelera);
                }
                case 1 -> {
                    comprarBoletos(cartelera, funciones, gestor);
                }
                case 2 -> {
                    List<Combo> comanda = new ArrayList<>();
                    Orden orden = new Orden(comanda);
                    comprarDulceria(orden, gestor, cliente);
                }
                case 3 -> {
                    revisarNotificaciones(gestor, cliente, funciones, boletos, ordenes, cartelera);
                }
                default -> CinePOOlis.menuPrincipal(gestor, v, usuarios);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    public static void comprarBoletos(List<Pelicula> cartelera, List<Funcion> funciones, GestorDeArchivos gestor) {
        try {
            // Creamos una lista para las claves de los boletos y otra para los boletos
            List<String> boletosClave = new ArrayList<>();
            List<Boleto> boletos = new ArrayList<>();

            do {
                try {
                    // Seleccionamos la función
                    Funcion funcionSeleccionada = seleccionarFuncion(funciones);
                    if (funcionSeleccionada == null) break;
                    // Seleccionamos los asientos
                    boletos = seleccionarAsiento(funcionSeleccionada);
                    // Añadimos las claves de los boletos
                    for (Boleto b : boletos) {
                        boletosClave.add(b.toString());
                    }
                    // Obtenemos el precio total
                    int precioTotal = 0;
                    for (Boleto b : boletos) {
                        precioTotal += b.getPrecio();
                    }
                    // Mostramos los boletos comprados
                    mostrarBoletosComprados(boletos, precioTotal);  
                    // Salimos del bucle
                    break;
                } catch (Exception e) {
                    System.err.println("Error." + e.getMessage());
                }
            } while (true);
            for(Boleto b : boletos) {
                gestor.guardarBoletosEnArchivo(b);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        StringBuilder funcionesStr = new StringBuilder("Cartelera de Películas:\n\n");
        for (int i = 0; i < funciones.size(); i++) {
            Funcion f = funciones.get(i);
            funcionesStr.append((i + 1)).append(". ").append(f.getNombrePelicula()).append("\n")
                .append("ID película: ").append(f.getIdPelicula()).append("\n")
                .append("Fecha: ").append(f.getFecha()).append("\n")
                .append("Hora: ").append(f.getHora()).append("\n")
                .append("Sala: ").append(f.getSala()).append("\n\n");
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
    public static List<Boleto> seleccionarAsiento(Funcion funcion) {
        String[][] asientos = funcion.cargarAsientos();
        // Mostrar el mapa de asientos
        verAsientos(funcion);
        
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
                return null; // El usuario canceló la selección
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

        // Bucle para seleccionar cada asiento individualmente
        String ultimoAsientoReservado = null; 
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
                    return null;
                }
                
                asientoSeleccionado = asientoSeleccionado.trim().toUpperCase();

                // Validación de formato básico (mínimo 2 caracteres)
                if (asientoSeleccionado.length() > 3 || asientoSeleccionado.length() < 2) {
                    JOptionPane.showMessageDialog(null, "Formato inválido. Ejemplo: A1 o B10.", "Error", JOptionPane.ERROR_MESSAGE); // Mover el mensaje aquí
                    continue; // Repite el bucle 'do-while'
                }

                try {
                    String letraStr, numeroStr;
                    // Parseo de la letra y el número
                    if (asientoSeleccionado.length() == 2) {
                        letraStr = asientoSeleccionado.substring(0, 1);
                        numeroStr = asientoSeleccionado.substring(1, 2);
                    } else { // longitud == 3
                        letraStr = asientoSeleccionado.substring(0, 1);
                        numeroStr = asientoSeleccionado.substring(1, 3);
                    }
                    

                    int indiceFila = funcion.obtenerNumeroFila(letraStr); // Convertir letra a índice
                    int indiceColumna = Integer.parseInt(numeroStr) - 1; // Columna base 1 a base 0

                    // Validar que los índices estén dentro de los límites de la matriz
                    if (indiceFila < 0 || indiceFila >= asientos.length ||
                        indiceColumna < 0 || indiceColumna >= asientos[0].length) {
                        
                        throw new IndexOutOfBoundsException("Asiento fuera de los límites de la sala.");
                    }
                    
                    String asientoActual = asientos[indiceFila][indiceColumna];

                    if (asientoActual.endsWith("[O]")) {
                        // Marcar asiento como reservado
                        asientos[indiceFila][indiceColumna] = asientoActual.replace("[O]", "[X]");
                        ultimoAsientoReservado = asientoSeleccionado; // Guardamos el último para devolver
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
                } catch (IndexOutOfBoundsException e) {
                    // Captura si el asiento está fuera del rango de la matriz (ej: Z99)
                    JOptionPane.showMessageDialog(
                        null, 
                        "Asiento " + asientoSeleccionado + " no existe en esta sala. Revisa el mapa.", 
                        "Error de Rango", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } while (!asientoReservado);
            // Creamos los hilos que muestran el proceso del pago
            ThreadBancario procesoPago = new ThreadBancario();
            Thread hiloProceso = new Thread(procesoPago, "Compra de boletos");

            hiloProceso.start();

            try {
                hiloProceso.join(); 
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "El proceso de pago fue interrumpido: " + e.getMessage(), "Error de Pago", JOptionPane.ERROR_MESSAGE);
                return null; // Cancelamos la compra de boletos.
            }

            // Creamos los boletos
            Boleto boleto = new Boleto(funcion.getFecha(), funcion.getHora(), funcion.getSala(), funcion.getPelicula(), ultimoAsientoReservado);
            boletosComprados.add(boleto);
        }
    return boletosComprados; // Devuelve una lista con los boletos comprados
}

    // Método auxiliar para ver asientos según la sala
    /*
     * Formato de asientos según sala:
     * Salas A:
     *  1 2 3 ... 15
     * A
     * B
     * c
     * ...
     * J
     * 
     * Sala B:
     *  1 2 3 ... 15
     * A
     * B
     * C
     * ...
     * J
     * En la sala B no hay asientos 1-4 y 12-15 en las filas A-D
     * 
     * Sala VIP:
     *  1 2 * * 3 4 * * 5 6
     * A
     * B
     * C
     * ...
     * H
     * Los asientos con * representan pasillos, no habrá asientos en esas posiciones
     */ 
    
    public static void verAsientos(Funcion funcion) {
        String[][] asientos = funcion.cargarAsientos();
        StringBuilder asientosStr = new StringBuilder();

        // Si la matriz de asientos está vacía o es nula, no imprimimos nada
        if (asientos == null || asientos.length == 0) {
            asientosStr.append("No hay datos de asientos disponibles.");
        } else {
            // Asignamos un valor a las columnas
            int numColumnas = asientos[0].length;
            asientosStr.append("   "); // Espacio para la letra de fila
            for (int j = 0; j < numColumnas; j++) {
                // Formato de impresión estandarizada para las columnas
                asientosStr.append(String.format(" %-3s", j + 1)); 
            }
            asientosStr.append("\n"); 

            for (int i = 0; i < asientos.length; i++) {
                // Añadir la letra de la fila 
                String letraFila = funcion.obtenerLetraFila(i); 
                asientosStr.append(letraFila).append(" ");
                
                for (String asiento : asientos[i]) {
                    // Formato de impresión estandarizada para el asiento
                    asientosStr.append(String.format(" %-3s", asiento));
                }
                asientosStr.append("\n"); 
            }
        }

        JOptionPane.showMessageDialog(
            null, 
            asientosStr.toString(), 
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
        List<Combo> comanda = orden.generarOrden(); 

        try {
            // Solicitamos al usuario que elija un combo o una orden personalizada
            seleccion = JOptionPane.showOptionDialog(null,"Buen día y bienvenido a la dulcería." + 
            "\n¿Te gustaría ordenar un combo o una orden personalizada?",
            "Dulcería", JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opcionesCombo,
            opcionesCombo[0]);

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
                            List<Persona> usuarios = gestor.cargarUsuarios();
                            
                            // Buscamos el objeto Vendedor 
                            nombreVendedor = usuarios.stream()
                                .filter(p -> p instanceof Vendedor)
                                .filter(v -> ((Vendedor)v).generarIdNombre().equals(idVendedorLog))
                                .map(Persona::getNombre)
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
