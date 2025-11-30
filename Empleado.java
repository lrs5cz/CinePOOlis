public class Empleado extends Persona {
    // Atributos 
    private String turno;
    private String rol;//nat

    // Constructor
    public Empleado(String nombre, String apellidoP, String apellidoM, int edad, String numeroCelular, Cuenta cuenta, String turno,String rol) {//nat
        super(nombre, apellidoP, apellidoM, edad, numeroCelular, cuenta);
        this.turno = turno;
        this.rol = rol;//nat
    }

    // Getters y Setters
    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }   

    //nat
    public String getRol(){
        return rol;
    }
    public void setRol(String rol){
        this.rol=rol;
    }

}
