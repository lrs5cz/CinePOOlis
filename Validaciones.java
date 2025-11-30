import java.util.List;

public class Validaciones {
    // Funciones de validación de datos

    // Contraseña
    public boolean validarPassword(String password, String confirmacion) throws PasswordException {
        boolean contraseñaValidada = false;
        if (!(password.contains("#") || password.contains("@")|| password.contains("$") ||
        password.contains("&") || password.contains("!")|| password.contains("%")))
        throw new PasswordException("Error. Ingresa un caracter especial.");
        else if (password.length() < 10) throw new PasswordException("Error. Tienes que ingresar 10 caracteres.");
        else if (!password.equals(confirmacion)) throw new PasswordException("Error. Las contraseñas son diferentes, vuelve a intentarlo.");
        else if(!comprobarCaracteres(password)) throw new PasswordException("Error. La contraseña debe de contar con al menus una mayúscula, una minúscula y un número.");
        else contraseñaValidada = true;
        return contraseñaValidada;
    }

    public boolean comprobarCaracteres(String password) {
        boolean caracteresValidos = false;
        boolean mayusculas = false;
        boolean minusculas = false;
        boolean numeros = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) mayusculas = true;
            else if (Character.isLowerCase(c)) minusculas = true;
            else if (Character.isDigit(c)) numeros = true;
        }
        if (mayusculas && numeros && minusculas) caracteresValidos = true;
        return caracteresValidos;
    }

    // Número de celular
    public boolean existeNumeroCelular(List<Persona> usuarios, String numeroCelular) {
        for (Persona p : usuarios) {
            if (p.getNumeroCelular().equals(numeroCelular)) {
                return true;
            }
        }
        return false;
    }

    // Nickname
    public boolean existeNickname(List<Persona> usuarios, String nickname) {
        for (Persona p : usuarios) {
            if (p.getNicknameCuenta().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    // Correo Electrónico
    public boolean existeCorreo(List<Persona> usuarios, String correo) {
        for (Persona p : usuarios) {
            if (p.getCorreoCuenta().equals(correo)) {
                return true;
            }
        }
        return false;
    }

    // Tarjeta Bancaria
    public boolean existeTarjeta(List<Persona> usuarios, String tarjeta) {
        for (Persona p : usuarios) {
            Cliente c = (Cliente) p;
            if (c.getTarjetaBancaria().equals(tarjeta)) {
                return true;
            }
        }
        return false;
    }
}
