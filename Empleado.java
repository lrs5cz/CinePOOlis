public class Empleado extends Persona {
    // Atributos 
    private String turno;

    // Constructor
    public Empleado(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, String turno) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular);
        this.turno = turno;
    }

    // Getters y Setters
    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }   
}