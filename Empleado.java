public class Empleado extends Persona {
    // Atributos 
    private String turno;
   

    // Constructor
    public Empleado(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String turno,) {
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta);
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

