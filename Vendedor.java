public class Vendedor extends Empleado {
    // Atributos
    public String diaDescanso;

    // Constructor
    public Vendedor(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String turno, String diaDescanso) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta, turno);
        this.diaDescanso = diaDescanso;
    }

    // Getters y Setters

    public String getDiaDescanso() {
        return diaDescanso;
    }   

    public void setDiaDescanso(String diaDescanso) {
        this.diaDescanso = diaDescanso;
    }
}