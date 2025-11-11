import javax.swing.*;

public class Cliente extends Persona {
    // Constantes para los métodos del cliente
    private static final int[] PRECIOS_PALOMITAS = {60, 70, 80, 90};
    private static final int[] PRECIOS_NACHOS = {70, 80, 90, 100};
    private static final int[] PRECIOS_REFRESCO = {45, 50, 60, 70};

    // Atributos
    private Cuenta cuenta;

    // Constructor
    public Cliente(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta);
        this.cuenta = cuenta;
    }

    // Getter y Setter para la cuenta de la persona
    public String getNicknameCuenta() {
        return cuenta.getNickname();
    }

    public void setNicknameCuenta(String nickname) {
        cuenta.setNickname(nickname);
    }

    public String getPasswordCuenta() {
        return cuenta.getPassword();
    }

    public void setPasswordCuenta(String password) {
        cuenta.setPassword(password);
    }

    public String getCorreoCuenta() {
        return cuenta.getCorreo();
    }

    public void setCorreoCuenta(String correo) {
        cuenta.setCorreo(correo);
    }

    // Métodos

    // Método para mostrar el menú del cliente
    public void menuCliente () {

    }

    // Método para comprar en la dulcería
    public static void comprarDulceria (Orden orden) {

        // Arreglo para almacenar las opciones disponibles
        String[] opcionesCombo = {"Combo amix", "Combo nachos", "Combo palomitas", "Combo buen trío", "Orden personalizada"};
        String[] opcionesAlimentos = {"Palomitas", "Nachos", "Refresco"};
        String[] tamaniosAlimentos = {"Mediano", "Grande", "Jumbo", "Mega"};
        int tamanio = -1, seleccion = -1, precio = 0;
        String sabor = "";
        Combo combo = new Combo("", 0); // Inicialización de la variable combo
        Alimento alimento = new Alimento("", "", 0); // Inicialización de la variable alimento

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
                    orden.generarOrden().add(combo.crearComboAmix());
                    break;
                case 1:
                    orden.generarOrden().add(combo.crearComboNachos());
                    break;
                case 2:
                    orden.generarOrden().add(combo.crearComboPalomitas());   
                    break;
                case 3:
                    orden.generarOrden().add(combo.crearComboBuenTrio());
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
                    orden.generarOrden().add(ordenPersonalizada);              
            }
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
}