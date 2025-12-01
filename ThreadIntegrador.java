import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class ThreadIntegrador implements Runnable {

    private String claveOrden; 
    private Orden orden; // Objeto Orden completo
    private Vendedor vendedorAsignado; 
    private GestorDeArchivos gestor; // Para manejar la persistencia de datos

    // Constante para el formato de fecha y hora
    private static final DateTimeFormatter FORMATO_LOG = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Constructor modificado para recibir la Orden completa
    public ThreadIntegrador(String claveOrden, Orden orden, GestorDeArchivos gestor) {
        this.claveOrden = claveOrden;
        this.orden = orden;
        this.gestor = gestor;
    }

    // Método auxiliar para simular la pausa del hilo
    private void simularPausa(int minSegundos, int maxSegundos) throws InterruptedException {
        Random rand = new Random();
        // Genera una pausa entre minSegundos y maxSegundos (inclusive)
        long pausaMillis = (rand.nextInt(maxSegundos - minSegundos + 1) + minSegundos) * 1000L;
        Thread.sleep(pausaMillis);
    }

    public String prepararOrden (Vendedor vendedor) {
        String tipoOrden = "";

        if (orden.getOrden().isEmpty()) {
            return null;
        }
        
        // Simular la "preparación" 
        try {
            // Pausa de 10 a 15 segundos antes de terminar
            simularPausa(10, 15); 
        } catch (InterruptedException e) {
            // Re-lanzar la interrupción para que el hilo lo maneje
            Thread.currentThread().interrupt();
        }
        
        return tipoOrden;
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
            if (!(dia.equals(v.getDiaDescanso()))) {
                boolean esMatutino = v.getTurno().equals("Matutino") && (hora >= 7 && hora < 13);
                boolean esVespertino = v.getTurno().equals("Vespertino") && (hora >= 13 && hora < 18);
                // Si la hora es 18:00 o más, o antes de las 7:00
                boolean esNocturno = v.getTurno().equals("Nocturno") && (hora >= 18 || hora < 7);
                
                if (esMatutino || esVespertino || esNocturno) {
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
            // Pausa inicial: 20 a 40 segundos antes de asignar la orden
            JOptionPane.showMessageDialog(null, "Esperando asignación de orden", "Preparación de orden", JOptionPane.PLAIN_MESSAGE);
            simularPausa(20, 40); 
            
            // Cargar vendedores
            List<Vendedor> vendedores = gestor.cargarVendedores();
            vendedorAsignado = obtenerVendedor(vendedores);
            
            if (vendedorAsignado == null) {
                JOptionPane.showMessageDialog(null, "No hay vendedores disponibles para atender la orden en este momento.", "Error de Asignación", JOptionPane.ERROR_MESSAGE);
                // La orden no se procesa si no hay vendedor.
                return; 
            }
            
            ZonedDateTime horaAsignacion = ZonedDateTime.now();
            
            // Pausa intermedia: 20 a 30 segundos antes de empezar a preparar
            String mensaje = "Orden asignada al vendedor " + vendedorAsignado.getNombre() + ".\nEsperando para iniciar preparación...";
            JOptionPane.showMessageDialog(null, mensaje, "Preparación de orden", JOptionPane.PLAIN_MESSAGE);
            simularPausa(20, 30);
            
            ZonedDateTime horaInicioPrep = ZonedDateTime.now();
            
            // Preparar la orden (contiene la pausa de 10-15 segundos)
            mensaje = "Iniciando la preparación...";
            JOptionPane.showMessageDialog(null, mensaje, "Preparación de orden", JOptionPane.PLAIN_MESSAGE);
            tipoOrden = prepararOrden(vendedorAsignado);

            if (!(tipoOrden == null)) {
                ZonedDateTime horaFinPrep = ZonedDateTime.now();
                
                // Actualizar el historial del empleado
                logOrden = String.format(
                    "Clave: %s, Tipo: %s | Generada: %s | Asignada: %s | Inicio Prep: %s | Fin Prep: %s",
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

                mensaje = "Orden " + claveOrden + " ha sido preparada. :)";
                JOptionPane.showMessageDialog(null, mensaje, "Preparación de orden", JOptionPane.PLAIN_MESSAGE);
            } else {
                throw new NullPointerException("Error. No hay ninguna orden para preparar");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de archivo en la integración de la orden: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            // El hilo fue interrumpido
            JOptionPane.showMessageDialog(null, "El proceso de preparación de la orden fue interrumpido.", "Cancelado", JOptionPane.WARNING_MESSAGE);
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error desconocido en la integración de la orden: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }
}