import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

public class ThreadIntegrador implements Runnable {

    private static final Set<String> VENDEDOR_PREPARANDO = ConcurrentHashMap.newKeySet();

    private String claveOrden; 
    private Combo combo; // Objeto Orden completo
    private Vendedor vendedorAsignado; 
    private GestorDeArchivos gestor; // Para manejar la persistencia de datos

    // Constante para el formato de fecha y hora
    private static final DateTimeFormatter FORMATO_LOG = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    
    // Constructor modificado para recibir el combo
    public ThreadIntegrador(String claveOrden, Combo combo, GestorDeArchivos gestor, List<Vendedor> vendedores) {
        this.claveOrden = claveOrden;
        this.combo = combo;
        this.gestor = gestor;
        this.vendedorAsignado = obtenerVendedor(vendedores);

        if(this.vendedorAsignado != null) {
            VENDEDOR_PREPARANDO.add(this.vendedorAsignado.generarIdNombre()); // Añade al set el vendedor para que no lo usen si está preparando algo
        }
    }


    // Método auxiliar para simular la pausa del hilo
    private void simularPausa(int minSegundos, int maxSegundos) throws InterruptedException {
        Random rand = new Random();
        // Genera una pausa entre minSegundos y maxSegundos (inclusive)
        long pausaMillis = (rand.nextInt(maxSegundos - minSegundos + 1) + minSegundos) * 1000L;
        Thread.sleep(pausaMillis);
    }

    public String prepararOrden (Vendedor vendedor, Combo combo) {
        String tipo = null;
        switch (combo.getNombreCombo().toUpperCase()) {
            case "COMBO AMIX" -> tipo = vendedor.prepararComboAmix(combo);
            case "COMBO NACHOS" -> tipo = vendedor.prepararComboNachos(combo);
            case "COMBO BUEN TRÍO" -> tipo =  vendedor.prepararComboBuenTrio(combo);
            case "COMBO QUÉ ME VES" -> tipo = vendedor.prepararComboQueMeVes(combo);
            case "ORDEN PERSONALIZADA" -> tipo = vendedor.prepararOrdenPersonalizada(combo);
            default -> tipo = null;
        }
        return tipo;
    }

    private Vendedor obtenerVendedor (List<Vendedor> vendedores) {
        ZonedDateTime tiempo = ZonedDateTime.now();
        int hora = tiempo.getHour();
        int diaInt = tiempo.getDayOfWeek().getValue();
        String dia = switch (diaInt) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6 -> "Sábado";
            case 7 -> "Domingo";
            default -> "";
        };

        for (Vendedor v : vendedores) {

            // Validamos que el vendedor no esté ocupado
            String idVendedor = v.generarIdNombre();

            if (VENDEDOR_PREPARANDO.contains(idVendedor)) {
                continue; // Este vendedor está ocupado, buscar el siguiente.
            }

            // Validamos que sea su día laboral
            if (!(dia.equals(v.getDiaDescanso()))) {
                boolean esMatutino = v.getTurno().equals("Matutino") && (hora >= 7 && hora < 13);
                boolean esVespertino = v.getTurno().equals("Vespertino") && (hora >= 13 && hora < 18);
                // Si la hora es 18:00 o más, o antes de las 7:00
                boolean esNocturno = v.getTurno().equals("Nocturno") && (hora >= 18 || hora < 7);
                
                if ((esMatutino || esVespertino || esNocturno) && !(v.getDiaDescanso().toUpperCase().equals(dia.toUpperCase()))) {
                    // Devolvemos el primer vendedor disponible.
                    return v; 
                }
            }
        }
        return null; // No se encontró un vendedor disponible
    }

    // Método run 
    public void run() {
        ZonedDateTime horaGeneracion = ZonedDateTime.now();
        String logOrden;
        String tipoOrden = "";

        try {
            // Pausa antes de asignar la orden, 20 a 40 segundos antes de asignar la orden
            simularPausa(20, 40); 
            
            // Cargamos los vendedores
            List<Vendedor> vendedores = gestor.cargarVendedores();
            vendedorAsignado = obtenerVendedor(vendedores);
            
            if (vendedorAsignado == null) {
                JOptionPane.showMessageDialog(null, "No hay vendedores disponibles para atender la orden en este momento.", "Error de Asignación", JOptionPane.ERROR_MESSAGE);
                // La orden no se procesa si no hay vendedor.
                return; 
            }
            
            ZonedDateTime horaAsignacion = ZonedDateTime.now();
            
            // Pausa de 20 a 30 segundos antes de empezar a preparar
            simularPausa(20, 30);
            
            ZonedDateTime horaInicioPrep = ZonedDateTime.now();
            
            // Preparar la orden (Pausa de 10 a 15 segundos)
            prepararOrden(vendedorAsignado, combo);
            simularPausa(10, 15);

            ZonedDateTime horaFinPrep = ZonedDateTime.now();
            
            // Actualizar el historial del empleado
            logOrden = String.format(
                "Clave: %s\nTipo: %s\nFecha y hora de Generación de la orden: %s\nAsignada: %s\nInicio de Preparación: %s\nFin de Preparación: %s\n",
                claveOrden,
                tipoOrden,
                horaGeneracion.format(FORMATO_LOG),
                horaAsignacion.format(FORMATO_LOG),
                horaInicioPrep.format(FORMATO_LOG),
                horaFinPrep.format(FORMATO_LOG)
            );
            
            // El historial del vendedor lleva el ID de usuario del vendedor
            String idVendedor = vendedorAsignado.generarIdNombre();
            gestor.guardarHistorialVendedor(idVendedor, logOrden);
            
            // Actualizar el archivo de notificaciones del cliente
            // La clave de dulcería que se guarda aquí indica que la orden está lista
            gestor.guardarNotificacionCliente(claveOrden);

            // Imprimimos la hora y día de finalización
            int anio = horaFinPrep.getYear();
            int mes = horaFinPrep.getMonthValue();
            int dia = horaFinPrep.getDayOfMonth();
            int hora = horaFinPrep.getHour();
            int minutos = horaFinPrep.getMinute();

            // Sobreescribimos el mensaje que se va a devolver al usuario al verificar las notificaciones
            gestor.guardarMensajeNotificacion("Hola, soy " + vendedorAsignado.getNombre() +
            ".\nYa está lista tu orden de dulcería. Puedes pasar a recogerla en la fila de dulcería para ventas de la app." + anio +
            mes + dia + ":" + hora + minutos + ".");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de archivo en la integración de la orden: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            // El hilo fue interrumpido
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error desconocido en la integración de la orden: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }
}