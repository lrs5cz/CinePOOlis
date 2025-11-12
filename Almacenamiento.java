import java.io.IOException;
import java.util.List;

public interface Almacenamiento {
    // Métodos para guardar y cargar datos
    void guardarDatos(List<String> datos);
    List<String> cargarDatos() throws IOException;
}
