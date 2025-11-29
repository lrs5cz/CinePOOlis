import java.io.IOException;
import javax.swing.*;

public class CinePOOlis {

    public static void menuPrincipal(GestorDeArchivos gestor) {
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
            case 0 -> registroClientes(gestor);
            case 1 -> accesoSistema(gestor);
            default -> {
                SalidaThread salida = new SalidaThread();
                Thread hiloSalida = new Thread(salida);
                hiloSalida.start();
            }
        } 
    }

    public static void registroClientes(GestorDeArchivos gestor) {
        String nombre, apellidoP, apellidoM, numeroCelular, nickname, correo = "", password = "", tarjetaBancaria = "";
        int edad = 0;
        try {
            nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre",
        "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
            if(nombre == null) return;
        
            apellidoP = JOptionPane.showInputDialog(null, "Ingrese su apellido paterno", 
            "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
            if(apellidoP == null) return;
            
            apellidoM = JOptionPane.showInputDialog(null, "Ingrese su apellido materno", 
            "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
            if(apellidoM == null) return;

            boolean edadValida = false;
            while (!edadValida) {
                try {
                    String inputEdad = JOptionPane.showInputDialog(null, "Ingrese su edad", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
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

            numeroCelular = JOptionPane.showInputDialog(null, "Ingrese su número telefónico", 
            "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
            if (numeroCelular == null) return;

            nickname = JOptionPane.showInputDialog(null, "Ingrese su nickname", 
            "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
            if (nickname == null) return;

            boolean correoValido = false;
            while(!correoValido) {
                try {
                    correo = JOptionPane.showInputDialog(null, "Ingrese su correo", 
            "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
                    if (correo == null) return;
                    JOptionPane.showMessageDialog(null, "Verificando correo electrónico...", "Verificación", JOptionPane.INFORMATION_MESSAGE);
                    
                    if (!(correo.endsWith("@gmail.com") || correo.endsWith("@hotmail.com") ||
                    correo.endsWith("@yahoo.com") || correo.endsWith("@outlook.com"))) {
                        throw new MailMismatchException("Error. El correo ingresado es inválido");
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
                    password = JOptionPane.showInputDialog(null, "Ingrese su contraseña", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
                    if (password == null) return;
                    
                    String confirmacion = JOptionPane.showInputDialog(null, "Confirme su contraseña", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
                    if (confirmacion == null) return;

                    validarPassword(password, confirmacion); 
                    passwordValida = true;
                } catch (PasswordException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Contraseña", JOptionPane.ERROR_MESSAGE);
                }
            }

            boolean tarjetaValida = false;
            while (!tarjetaValida) {
                try {
                    tarjetaBancaria = JOptionPane.showInputDialog(null, "Ingrese su tarjeta bancaria (16 dígitos)", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);

                    if (tarjetaBancaria == null) {
                        throw new NullPointerException("Operación de tarjeta cancelada por el usuario.");
                    }

                    if (tarjetaBancaria.length() != 16) {
                        throw new IndexOutOfBoundsException("Error. Ingrese una tarjeta válida (16 dígitos).");
                    }
                    // Opcional: Validar que sean solo dígitos.
                    // if (!tarjetaBancaria.matches("\\d{16}")) { throw new Exception("Solo se permiten dígitos."); }
                    
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
        } finally {
            JOptionPane.showMessageDialog(null, "Registro completado exitosamente!", "Registro Clientes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void validarPassword(String password, String confirmacion) throws PasswordException{
        if (!(password.contains("#") || password.contains("@")||
        password.contains("&") || password.contains("!")|| password.contains("%")))
        throw new PasswordException("Error. La contraseña no es válida");
        else if (password.length() < 10) throw new PasswordException("Error. La contraseña no es válida");
        else if (!password.equals(confirmacion)) throw new PasswordException("Error. La contraseña no es válida");
    }

    public static void accesoSistema(GestorDeArchivos gestor) {

    }

    public static void main(String[] args) {
        GestorDeArchivos gestor = new GestorDeArchivos();
        registroClientes(gestor);
    }
}

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
