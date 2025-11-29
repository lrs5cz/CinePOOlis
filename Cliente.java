import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
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
    public static void menuCliente () {

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
    public List<String> comprarBoletos(List<Pelicula> cartelera, List<Funcion> funciones, String[][] asientos) {
        // Creamos una lista para las claves de los boletos
        List<String> boletosClave = new ArrayList<String>();
        do {
            try {
                // Seleccionamos la función
                Funcion funcionSeleccionada = seleccionarFuncion(funciones);
                if (funcionSeleccionada == null) break;
                // Seleccionamos los asientos
                List<Boleto> boletos = seleccionarAsiento(asientos, funcionSeleccionada);
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
        // Retornamos la lista con la clave de los boletos
        return boletosClave;
    }

    // Método auxiliar para mostrar los boletos comprados
    private void mostrarBoletosComprados (List<Boleto> boletos, int precioTotal) {
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
    private Funcion seleccionarFuncion(List<Funcion> funciones) {

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
        int seleccion = 0;
        boolean seleccionValida = false;
        do {
            try {
                seleccion = Integer.parseInt(JOptionPane.showInputDialog(null, 
                "Ingrese el índice de la función que quieres asistir", "0"));
                // Ajustamos el índice
                seleccion = seleccion - 1;
                seleccionValida = true;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar la cartelera: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (!seleccionValida);
        return funciones.get(seleccion);
    }

    // Método auxiliar para seleccionar asiento
    public List<Boleto> seleccionarAsiento(String[][] asientos, Funcion funcion) {
        // Mostrar el mapa de asientos
        verAsientos(asientos, funcion);
        
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
    
    public void verAsientos(String[][] asientos, Funcion funcion) {
        String sala = funcion.getSala();
        StringBuilder asientosStr = new StringBuilder();

        switch (sala) {
            case "Sala A":
                asientosStr.append("Asientos Sala A.\n\n");
                break;
            case "Sala B":
                asientosStr.append("Asientos Sala B.\n\n");
                break;
            case "Sala VIP":
                asientosStr.append("Asientos Sala VIP.\n\n");
                break;
            default:
                asientosStr.append("Asientos de Sala Desconocida.\n\n");
                break;
        }

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
    public static void comprarDulceria (Orden orden, GestorDeArchivos gestor, Cliente cliente) {

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    // Método auxiliar que genera un ID del nombre del cliente
    public String generarIdNombre() {
        // Genera un ID con las iniciales de la película
        String[] palabras = {getNombre(), getApellidoP(), getApellidoM()};
        StringBuilder id = new StringBuilder();
        for (String palabra : palabras) {
            if (palabra != null && !palabra.trim().isEmpty()) { 
                id.append(palabra.trim().charAt(0));
            } 
        }
        return id.toString().toUpperCase();
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
}
