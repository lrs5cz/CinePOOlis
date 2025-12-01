import java.io.IOException;
import javax.swing.*;
import java.util.List;

public class CinePOOlis {

    public static void menuPrincipal(GestorDeArchivos gestor, Validaciones v, List<Persona> usuarios) {
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
            case 0 -> registroClientes(gestor, v, usuarios);
            case 1 -> accesoSistema(gestor, usuarios);

            default -> {
                JOptionPane.showMessageDialog(null, "Saliendo del programa...", "Salida", JOptionPane.PLAIN_MESSAGE);
                JOptionPane.showMessageDialog(null, "Hasta luego!", "Salida", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        } 
    }

    public static void registroClientes(GestorDeArchivos gestor, Validaciones v, List<Persona> usuariosEnSistema) {
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
                        throw new IndexOutOfBoundsException("Error. Ingrese una edad válida (1 - 99).");
                    }
                    edadValida = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error. Ingrese solo números para la edad.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

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

            boolean tarjetaValida = false;
            while (!tarjetaValida) {
                try {
                    tarjetaBancaria = JOptionPane.showInputDialog(null, "Ingrese su tarjeta bancaria (16 dígitos)",
                    "Registro de clientes", JOptionPane.INFORMATION_MESSAGE);

                    if (tarjetaBancaria == null) {
                        throw new NullPointerException("Operación de tarjeta cancelada por el usuario.");
                    }

                    if (v.existeTarjeta(usuariosEnSistema, tarjetaBancaria)) {
                        JOptionPane.showMessageDialog(null, "Error. La tarjeta bancaria ya está registrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue; 
                    }

                    if (tarjetaBancaria.length() != 16 || !tarjetaBancaria.matches("\\d{16}")) {
                        throw new IndexOutOfBoundsException("Error. Ingrese una tarjeta válida (16 dígitos).");
                    }
                    
                    tarjetaValida = true;
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            Cuenta cuenta = new Cuenta(nickname, password, correo);
            Cliente cliente = new Cliente(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, tarjetaBancaria);
            gestor.guardarUsuariosEnArchivo(cliente);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void accesoSistema(GestorDeArchivos gestor, List<Persona> usuarios) {
        Persona usuario = null;
        boolean inicioSesion = false;
        do {
            String nicknameIngresado = JOptionPane.showInputDialog(null, "Ingrese su nickname",
            "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
            
            // Manejar cancelación
            if (nicknameIngresado == null) return; 

            String passwordIngresado = JOptionPane.showInputDialog(null, "Ingrese su contraseña",
            "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
            
            // Manejar cancelación
            if (passwordIngresado == null) return; 

            // Intentar autenticación
            usuario = getUsuario(usuarios, nicknameIngresado, passwordIngresado);

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
            Cliente.menuCliente(gestor, cliente);
        } else if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;
            // Administrador.menuAdmin(parámetros); 
        } else if (usuario instanceof Vendedor) {
            Vendedor vendedor = (Vendedor) usuario;
            // Vendedor.menuVendedor(parámetros); 
        }
    }

    public static Persona getUsuario(List<Persona> usuarios, String nicknameIngresado, String passwordIngresado) {
        // Buscamos al usuario con el que coincidan ese nickname y esa contraseña
        for (Persona p : usuarios) {
            if (nicknameIngresado.equals(p.getNicknameCuenta()) && passwordIngresado.equals(p.getPasswordCuenta())) {
                if (p instanceof Cliente) {
                    return (Cliente) p;
                } else if (p instanceof Administrador) {
                    return (Administrador) p;
                } else if (p instanceof Vendedor) {
                    return (Vendedor) p;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            GestorDeArchivos gestor = new GestorDeArchivos();
            Validaciones v = new Validaciones();
            List<Persona> usuarios = gestor.cargarUsuarios();
            while (true) {
                menuPrincipal(gestor, v, usuarios);
            }
        } catch (IOException e) {

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
