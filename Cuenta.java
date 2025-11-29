public class Cuenta {
    // Atributos
    private String nickname, password, correo;

    // Constructor
    public Cuenta(String nickname, String password, String correo) {
        this.nickname = nickname;
        this.password = password;
        this.correo = correo;
    }

    // Getters y Setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}