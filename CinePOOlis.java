import java.io.IOException;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import javax.swing.*;
import java.util.List;

public class CinePOOlis {

    public static void menuPrincipal(GestorDeArchivos gestor, Validaciones v, List<Cliente> clientes) {
        String[] opcionesUsuario = {"Nuevo registro de cliente", "Ingreso al sistema", "Salir"};
        int accion = JOptionPane.showOptionDialog(null, "Bienvenido a CinePOOlis",
            "CinePOOlis",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opcionesUsuario,
            opcionesUsuario[0]
        );
        switch (accion) {
            case 0 -> registroClientes(gestor, v, clientes);
            case 1 -> accesoSistema(gestor, v);

            default -> {
                JOptionPane.showMessageDialog(null, "Saliendo del programa...", "Salida", JOptionPane.PLAIN_MESSAGE);
                JOptionPane.showMessageDialog(null, "Hasta luego!", "Salida", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        } 
    }

    public static void registroClientes(GestorDeArchivos gestor, Validaciones v, List<Cliente> clientes) {
        String nombre, apellidoP, apellidoM, numeroCelular = "", nickname = "", correo = "", password = "", tarjetaBancaria = "";
        int edad = 0;
        try {
            nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre",
        "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
            if(nombre == null) return;
        
            apellidoP = JOptionPane.showInputDialog(null, "Ingrese su apellido paterno", 
            "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
            if(apellidoP == null) return;
            
            apellidoM = JOptionPane.showInputDialog(null, "Ingrese su apellido materno", 
            "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
            if(apellidoM == null) return;

            boolean edadValida = false;
            while (!edadValida) {
                try {
                    String inputEdad = JOptionPane.showInputDialog(null, "Ingrese su edad", "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                    if (inputEdad == null) return;
                    
                    edad = Integer.parseInt(inputEdad);

                    if (edad < 1 || edad > 99) {
                        throw new IllegalArgumentException("Error. Ingrese una edad válida (1 - 99).");
                    }
                    edadValida = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error. Ingrese solo números para la edad.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            boolean celularUnico = false;
            while (!celularUnico) {
                numeroCelular = JOptionPane.showInputDialog(null, "Ingrese su número telefónico", 
                "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);
                if (numeroCelular == null) return;

                if (v.existeNumeroCliente(clientes, numeroCelular)) {
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
                
                if (v.existeNicknameCliente(clientes, nickname)) {
                    JOptionPane.showMessageDialog(null, "Error. El nickname ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    nicknameUnico = true;
                }
            }

            nickname = nickname.toUpperCase();

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
                    } else if(v.existeCorreoCliente(clientes, correo)) {
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

            boolean tarjetaValida = false;
            while (!tarjetaValida) {
                try {
                    tarjetaBancaria = JOptionPane.showInputDialog(null, "Ingrese su tarjeta bancaria (16 dígitos)",
                    "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);

                    if (tarjetaBancaria == null) {
                        throw new NullPointerException("Operación de tarjeta cancelada por el usuario.");
                    }

                    if (v.existeTarjeta(clientes, tarjetaBancaria)) {
                        JOptionPane.showMessageDialog(null, "Error. La tarjeta bancaria ya está registrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue; 
                    }

                    if (tarjetaBancaria.length() != 16 || !tarjetaBancaria.matches("\\d{16}")) {
                        throw new IllegalArgumentException("Error. Ingrese una tarjeta válida (16 dígitos).");
                    }
                    
                    tarjetaValida = true;
                } catch (NullPointerException | IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            Cuenta cuenta = new Cuenta(nickname, password, correo);
            Cliente cliente = new Cliente(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, tarjetaBancaria);
            
            // Añadimos el nuevo cliente a la lista para asegurarnos de que no haya duplicaciones
            clientes.add(cliente);
            gestor.guardarClienteEnArchivo(cliente);
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente!", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void accesoSistema(GestorDeArchivos gestor, Validaciones v) {
        // Inicializamos variables y el tiempo
        Persona usuario = null;
        boolean inicioSesion = false;
        try {
            do {
                String nicknameIngresado = JOptionPane.showInputDialog(null, "Ingrese su nickname",
                "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                
                // Manejar cancelación
                if (nicknameIngresado == null) return; 

                nicknameIngresado = nicknameIngresado.toUpperCase();

                String passwordIngresado = JOptionPane.showInputDialog(null, "Ingrese su contraseña",
                "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                
                // Manejar cancelación
                if (passwordIngresado == null) return; 

                // Intentar autenticación
                usuario = getUsuario(gestor, nicknameIngresado, passwordIngresado);

                if (usuario == null) {
                    // Mostrar error y pedir reintento (o finalizar)
                    int opcion = JOptionPane.showConfirmDialog(
                        null, 
                        "Error: Nickname o contraseña incorrectos.\n¿Desea reintentar?", 
                        "Fallo de Autenticación", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.ERROR_MESSAGE
                    );

                    if (opcion == JOptionPane.NO_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                        return; // Si el usuario elige NO o cierra el diálogo, se termina el método.
                    }
                } else {
                    inicioSesion = true; 
                }

            } while (!inicioSesion);


            // Casteamos usuario según su instancia
            if (usuario instanceof Cliente) {
                Cliente cliente = (Cliente) usuario;
                // Agregaremos validación de horario para que el cliente no pueda acceder al cine antes de las 8 am y después de las 23:59pm
                LocalTime horaActual = LocalTime.now();

                // Hora de apretura y cierre
                LocalTime apertura = LocalTime.of(8, 0); // 8 am
                LocalTime cierre = LocalTime.of(23, 59); // 12 am
                
                // Validamos
                if(horaActual.isBefore(apertura) || horaActual.isAfter(cierre)) {
                    JOptionPane.showMessageDialog(null, "Bienvenido/a, CinePOOlis aún no está abierto.\n" +
                    "vuelve cuando el cine esté abierto.\n Horario: 08:00 am - 12:00 am", "Clientes", JOptionPane.ERROR_MESSAGE);
                } else {
                    Cliente.menuCliente(gestor, cliente);
                }
                
            } else if (usuario instanceof Administrador) {
                // Agregaremos una validación para que solo los administradores que trabajan entre semana puedan acceder de lunes a viernes y viceversa
                Administrador admin = (Administrador) usuario;
                boolean disponibilidad = validarDisponibilidadAdmin(admin);
                if (disponibilidad) Administrador.menuAdmin(gestor, v, admin); 
                else {
                    JOptionPane.showMessageDialog(null, "¿Qué haces aquí " + admin.getNombre() + "?\nA esta hora deberías de estar descansando del trabajo" + 
                    "\nVete a descansar, ya sabes que el jefe no paga horas extra.", "Administradores", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            } else if (usuario instanceof Vendedor) {
                // Agregaremos una validación para que los vendedores no puedan acceder a su trabajo fuera de su turno o en su día de descanso
                Vendedor vendedor = (Vendedor) usuario;
                boolean disponibilidad = validarDisponibilidadVendedor(vendedor);
                if (disponibilidad) Vendedor.menuVendedor(gestor, vendedor); 
                else {
                    JOptionPane.showMessageDialog(null, "¿Qué haces aquí " + vendedor.getNombre() + "?\nA esta hora deberías de estar descansando del trabajo" + 
                    "\nVete a descansar, ya sabes que el jefe no paga horas extra.", "Vendedores", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Persona getUsuario(GestorDeArchivos gestor, String nicknameIngresado, String passwordIngresado) {
        Cliente cliente = getCliente(gestor, nicknameIngresado, passwordIngresado);
        Administrador administrador = getAdmin(gestor, nicknameIngresado, passwordIngresado);
        Vendedor vendedor = getVendedor(gestor, nicknameIngresado, passwordIngresado);
        if (administrador == null && vendedor == null) {
            return cliente;
        } else if (cliente == null && vendedor == null) {
            return administrador;
        } else if (cliente == null && administrador == null) {
            return vendedor;
        }
        return null;
    }

    public static Cliente getCliente(GestorDeArchivos gestor, String nicknameIngresado, String passwordIngresado) {
        try {
            List<Cliente> clientes = gestor.cargarClientes();
            for (Cliente c : clientes) {
                if(nicknameIngresado.equals(c.getNicknameCuenta()) && passwordIngresado.equals(c.getPasswordCuenta())) {
                    return c;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static Administrador getAdmin(GestorDeArchivos gestor, String nicknameIngresado, String passwordIngresado) {
        try {
            List<Administrador> administradores = gestor.cargarAdmin();
            for (Administrador a : administradores) {
                if(nicknameIngresado.equals(a.getNicknameCuenta()) && passwordIngresado.equals(a.getPasswordCuenta())) {
                    return a;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static Vendedor getVendedor(GestorDeArchivos gestor, String nicknameIngresado, String passwordIngresado) {
        try {
            List<Vendedor> vendedores = gestor.cargarVendedores();
            for (Vendedor v : vendedores) {
                if(nicknameIngresado.equals(v.getNicknameCuenta()) && passwordIngresado.equals(v.getPasswordCuenta())) {
                    return v;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static boolean validarDisponibilidadAdmin(Administrador admin) {
        boolean isTrabajandoDia = false;

        // Validamos sus días de trabajo
        ZonedDateTime hoy = ZonedDateTime.now();
        int diaSemana = hoy.getDayOfWeek().getValue();
        // Validamos si el día es entre semana o fin de semana
        boolean entreSemana = (diaSemana >= 1 && diaSemana <= 5);
        boolean finDeSemana = (diaSemana == 6 || diaSemana == 7);

        if (admin.getDiasTrabajo().equals("Entre Semana")) isTrabajandoDia = entreSemana;
        else if (admin.getDiasTrabajo().equals("Fin de semana")) isTrabajandoDia = finDeSemana;
        
        if(!isTrabajandoDia) return false;

        // Validamos el turno
        boolean isTrabajandoAhora = false;
        int hora = hoy.getHour();

        if (admin.getTurno().equals("Matutino") && (hora >= 7 && hora < 13)) isTrabajandoAhora = true;
        else if (admin.getTurno().equals("Vespertino") && (hora >= 13 && hora < 18)) isTrabajandoAhora = true;
        else if (admin.getTurno().equals("Nocturno") && (hora >= 18 || hora < 2)) isTrabajandoAhora = true;

        return isTrabajandoAhora; 
    }

    public static boolean validarDisponibilidadVendedor(Vendedor vendedor) {

        // Validamos su día de descanso
        ZonedDateTime hoy = ZonedDateTime.now();
        int diaSemana = hoy.getDayOfWeek().getValue();
        String dia = switch (diaSemana) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6 -> "Sábado";
            case 7 -> "Domingo";
            default -> "";
        };
        if (dia.toUpperCase().equals(vendedor.getDiaDescanso().toUpperCase())) return false;
        
        // Validamos el turno
        boolean isTrabajandoAhora = false;
        int hora = hoy.getHour();

        if (vendedor.getTurno().equals("Matutino") && (hora >= 7 && hora < 13)) isTrabajandoAhora = true;
        else if (vendedor.getTurno().equals("Vespertino") && (hora >= 13 && hora < 18)) isTrabajandoAhora = true;
        else if (vendedor.getTurno().equals("Nocturno") && (hora >= 18 || hora < 1)) isTrabajandoAhora = true;

        return isTrabajandoAhora; 
    }
    public static void main(String[] args) {
        try {
            GestorDeArchivos gestor = new GestorDeArchivos();
            Validaciones v = new Validaciones();
            List<Cliente> clientes = gestor.cargarClientes();
            while (true) {
                menuPrincipal(gestor, v, clientes);
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}

// Excepciones auxiliares

class PasswordException extends Exception {
    public PasswordException(String mensaje) {
        super(mensaje);
    }
}

class MailMismatchException extends Exception {
    public MailMismatchException(String mensaje) {
        super(mensaje);
    }

}

class UsuarioExistenteException extends Exception {
    public UsuarioExistenteException(String mensaje) {
        super(mensaje);
    }
}