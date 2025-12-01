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
    public boolean existeNumeroCliente(List<Cliente> cliente, String numeroCelular) {
        for (Cliente c : cliente) {
            if (c.getNumeroCelular().equals(numeroCelular)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeNumeroAdmin(List<Administrador> admininstradores, String numeroCelular) {
        for (Administrador a : admininstradores) {
            if (a.getNumeroCelular().equals(numeroCelular)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeNumeroVendedor(List<Vendedor> vendedores, String numeroCelular) {
        for (Vendedor v : vendedores) {
            if (v.getNumeroCelular().equals(numeroCelular)) {
                return true;
            }
        }
        return false;
    }

    // Nickname
    public boolean existeNicknameCliente(List<Cliente> clientes, String nickname) {
        for (Cliente c : clientes) {
            if (c.getNicknameCuenta().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeNicknameAdmin(List<Administrador> administradores, String nickname) {
        for (Administrador a : administradores) {
            if (a.getNicknameCuenta().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeNicknameVendedor(List<Vendedor> vendedores, String nickname) {
        for (Vendedor v : vendedores) {
            if (v.getNicknameCuenta().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    // Correo Electrónico

    public boolean existeCorreoCliente(List<Cliente> clientes, String correo) {
        for (Cliente c : clientes) {
            if (c.getCorreoCuenta().equals(correo)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeCorreoAdmin(List<Administrador> administradores, String correo) {
        for (Administrador a : administradores) {
            if (a.getCorreoCuenta().equals(correo)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeCorreoVendedor(List<Vendedor> vendedores, String correo) {
        for (Vendedor v : vendedores) {
            if (v.getCorreoCuenta().equals(correo)) {
                return true;
            }
        }
        return false;
    }

    // Tarjeta Bancaria
    public boolean existeTarjeta(List<Cliente> clientes, String tarjeta) {
        for (Cliente c : clientes) {
            if (c.getTarjetaBancaria().equals(tarjeta)) {
                return true;
            }
        }
        return false;
    }
}
