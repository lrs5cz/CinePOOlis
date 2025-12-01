import java.io.*;

public abstract class Persona implements Serializable {
    // Atributos
    private String nombre, apellidoP, apellidoM, numeroCelular;
    private int edad;
    private Cuenta cuenta;

    // Constructor
    public Persona(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.numeroCelular = numeroCelular;
        this.cuenta = cuenta;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
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
}