import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoTxt implements Almacenamiento {
    // Implementación de los métodos para guardar y cargar datos en archivos de texto
    @Override
    public void guardarDatos(List<String> datos, String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            if (!(nombreArchivo.substring(nombreArchivo.length() - 4, nombreArchivo.length())).equals(".txt")) {
                throw new IllegalArgumentException("El archivo debe tener la extensión .txt");
            }
            for (String s : datos) {
                String linea = s;
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> cargarDatos(String nombreArchivo) throws IOException {
        // Lógica para cargar datos desde un archivo de texto
        List<String> datos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            if (!(nombreArchivo.substring(nombreArchivo.length() - 4, nombreArchivo.length())).equals(".txt")) {
                throw new IllegalArgumentException("El archivo debe tener la extensión .txt");
            }
            String linea;
            while ((linea = reader.readLine()) != null) {
                datos.add(linea);
            }
            return datos;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}