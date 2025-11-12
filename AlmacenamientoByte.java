public class AlmacenamientoByte implements Almacenamiento {
    // Implementación de los métodos para guardar y cargar datos en archivos binarios
    @Override
    public void guardarDatos(List<String> datos, String nombreArchivo) {
        // Lógica para guardar datos en un archivo binario
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            if (!(nombreArchivo.substring(nombreArchivo.length() - 5, nombreArchivo.length())).equals(".byte")) {
                throw new IllegalArgumentException("El archivo debe tener la extensión .byte");
            }
            for (String s : datos) {
                oos.writeObject(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> cargarDatos(String nombreArchivo) {
        // Lógica para cargar datos desde un archivo binario
        List<String> datos = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            if (!(nombreArchivo.substring(nombreArchivo.length() - 5, nombreArchivo.length())).equals(".byte")) {
                throw new IllegalArgumentException("El archivo debe tener la extensión .byte");
            }
            while (true) {
                try {
                    String s = (String) ois.readObject();
                    datos.add(s);
                } catch (EOFException e) {
                    break; // Fin del archivo alcanzado
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}