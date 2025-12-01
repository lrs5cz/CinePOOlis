import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class GestorDeArchivos {

    // Se crea el archivo de peliculas
    private final File CLIENTES_REGISTRADOS = new File("clientesRegistrados.dat"); 
    private final File ADMIN_REGISTRADOS = new File("adminRegistrados.dat");
    private final File VENDEDORES_REGISTRADOS = new File("vendedoresRegistrados.dat");
    private final File ARCHIVO_PELICULAS = new File("peliculasAgregadas.txt");
    private final File ARCHIVO_FUNCIONES = new File("funcionesAgregadas.txt");
    private final File BOLETOS_COMPRADOS = new File("boletosComprados.txt");
    private final String RUTA_BASE_ASIENTOS = "asientosFuncion_";
    private final File HISTORIAL_DULCERIA = new File("historialDeDulceria.txt"); // Claves de órdenes 
    private final String HISTORIAL_VENDEDOR_PREFIX = "historialVendedor_"; // Prefijo de la ruta para archivos como "historialVendedor_{ID Vendedor}.txt"
    private final File ESTADO_ORDEN = new File ("EstadoDeLaOrden.txt"); // Archivo de texto para leer si una orden ya está lista o aún no

    public void guardarUsuarios (Persona persona) throws IOException {
        if (persona instanceof Cliente) {
            Cliente cliente = (Cliente) persona;
            guardarClienteEnArchivo(cliente);
        } else if (persona instanceof Administrador) {
            Administrador admin = (Administrador) persona;
            guardarAdminEnArchivo(admin);
        } else if (persona instanceof Vendedor) {
            Vendedor vendedor = (Vendedor) persona;
            guardarVendedorEnArchivo(vendedor);
        }
    }

    public void guardarClienteEnArchivo(Cliente cliente) throws IOException {
        try (
            FileOutputStream fos = new FileOutputStream(CLIENTES_REGISTRADOS, true);
            DataOutputStream dos = new DataOutputStream(fos)
        ){
            dos.writeUTF(cliente.getNombre());
            dos.writeUTF(cliente.getApellidoP());
            dos.writeUTF(cliente.getApellidoM());
            dos.writeInt(cliente.getEdad());
            dos.writeUTF(cliente.getNumeroCelular());
            dos.writeUTF(cliente.getNicknameCuenta());
            dos.writeUTF(cliente.getCorreoCuenta());
            dos.writeUTF(cliente.getPasswordCuenta());
            dos.writeUTF(cliente.getTarjetaBancaria());
        }
    }

    public List<Cliente> cargarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try (
        FileInputStream fis = new FileInputStream(CLIENTES_REGISTRADOS);
        DataInputStream dis = new DataInputStream(fis)
        ) {
            while (true) {
                try {
                    // Leemos los datos
                    String nombre = dis.readUTF();
                    String apellidoP = dis.readUTF();
                    String apellidoM = dis.readUTF();
                    int edad = dis.readInt();
                    String celular = dis.readUTF();
                    String nickname = dis.readUTF();
                    String correo = dis.readUTF();
                    String password = dis.readUTF();
                    Cuenta cuenta = new Cuenta(nickname, password, correo);
                    String tarjetaBancaria = dis.readUTF();
                    Cliente cliente = new Cliente(nombre, apellidoP, apellidoM, edad, celular, cuenta, tarjetaBancaria);
                    clientes.add(cliente);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return clientes;
    } 

    public void guardarAdminEnArchivo(Administrador admin) throws IOException {
        try (
            FileOutputStream fos = new FileOutputStream(ADMIN_REGISTRADOS, true);
            DataOutputStream dos = new DataOutputStream(fos)
        ){
            dos.writeUTF(admin.getNombre());
            dos.writeUTF(admin.getApellidoP());
            dos.writeUTF(admin.getApellidoM());
            dos.writeInt(admin.getEdad());
            dos.writeUTF(admin.getNumeroCelular());
            dos.writeUTF(admin.getNicknameCuenta());
            dos.writeUTF(admin.getCorreoCuenta());
            dos.writeUTF(admin.getPasswordCuenta());
            dos.writeUTF(admin.getTurno());
            dos.writeUTF(admin.getDiasTrabajo());
        }
    }

    public List<Administrador> cargarAdmin() throws IOException {
        List<Administrador> admins = new ArrayList<>();
        try (
        FileInputStream fis = new FileInputStream(ADMIN_REGISTRADOS);
        DataInputStream dis = new DataInputStream(fis)
        ) {
            while (true) {
                try {
                    // Leemos los datos
                    String nombre = dis.readUTF();
                    String apellidoP = dis.readUTF();
                    String apellidoM = dis.readUTF();
                    int edad = dis.readInt();
                    String celular = dis.readUTF();
                    String nickname = dis.readUTF();
                    String correo = dis.readUTF();
                    String password = dis.readUTF();
                    Cuenta cuenta = new Cuenta(nickname, password, correo);
                    String turno = dis.readUTF();
                    String diasTrabajo = dis.readUTF();
                    Administrador admin = new Administrador(nombre, apellidoP, apellidoM, edad, celular, cuenta, turno, diasTrabajo);
                    admins.add(admin);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return admins;
    }

    public void guardarVendedorEnArchivo(Vendedor vendedor) throws IOException {
        try (
            FileOutputStream fos = new FileOutputStream(VENDEDORES_REGISTRADOS, true);
            DataOutputStream dos = new DataOutputStream(fos)
        ){
            dos.writeUTF(vendedor.getNombre());
            dos.writeUTF(vendedor.getApellidoP());
            dos.writeUTF(vendedor.getApellidoM());
            dos.writeInt(vendedor.getEdad());
            dos.writeUTF(vendedor.getNumeroCelular());
            dos.writeUTF(vendedor.getNicknameCuenta());
            dos.writeUTF(vendedor.getCorreoCuenta());
            dos.writeUTF(vendedor.getPasswordCuenta());
            dos.writeUTF(vendedor.getTurno());
            dos.writeUTF(vendedor.getDiaDescanso());
        }
    }

    public List<Vendedor> cargarVendedores() throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        try (
        FileInputStream fis = new FileInputStream(VENDEDORES_REGISTRADOS);
        DataInputStream dis = new DataInputStream(fis)
        ) {
            while (true) {
                try {
                    // Leemos los datos
                    String nombre = dis.readUTF();
                    String apellidoP = dis.readUTF();
                    String apellidoM = dis.readUTF();
                    int edad = dis.readInt();
                    String celular = dis.readUTF();
                    String nickname = dis.readUTF();
                    String correo = dis.readUTF();
                    String password = dis.readUTF();
                    Cuenta cuenta = new Cuenta(nickname, password, correo);
                    String turno = dis.readUTF();
                    String diaDescanso = dis.readUTF();
                    Vendedor vendedor = new Vendedor(nombre, apellidoP, apellidoM, edad, celular, cuenta, turno, diaDescanso);
                    vendedores.add(vendedor);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return vendedores;
    } 

    // Método para guardar peliculas en el archivo "peliculasAgregadas.txt"
    public void guardarPeliculaEnArchivo(Pelicula unaPelicula) throws IOException{ //La excepcion se maneja en la interfaz grafica 
        FileWriter objetoFileWriter = new FileWriter(ARCHIVO_PELICULAS,true);
        objetoFileWriter.write(unaPelicula.toString() + "\n");
        objetoFileWriter.close();
    }

    // Método para cargar las peliculas del archivo "peliculasAgregadas.txt"
    public List<Pelicula> cargarPeliculas() throws IOException{
        List<Pelicula> listaDePeliculasDelArchivo = new ArrayList<>();
        if(!ARCHIVO_PELICULAS.exists()){
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return listaDePeliculasDelArchivo;
        }
        BufferedReader objetoReader = new BufferedReader(new FileReader(ARCHIVO_PELICULAS));
        String linea;
        while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            if(partes.length == 4){
                Pelicula peliculaLeida = new Pelicula(partes[0],partes[1],partes[2],partes[3]);
                listaDePeliculasDelArchivo.add(peliculaLeida);
            }
        }
        objetoReader.close();
        return listaDePeliculasDelArchivo;
    }

    // Método para guardar funciones en el archivo "funcionesAgregadas.txt"
    public void guardarFuncionesEnArchivo(Funcion unaFuncion) throws IOException{
        FileWriter objetoFileWriter = new FileWriter(ARCHIVO_FUNCIONES,true);
        objetoFileWriter.write(unaFuncion.toString() + "\n");
        objetoFileWriter.close();
    }  

    // Método para cargar las funciones del archivo "funcionesAgregadas.txt"
    public List<Funcion> mostrarFunciones(List<Pelicula> cartelera) throws IOException{
        List<Funcion> listaDeFuncionesDelArchivo = new ArrayList<>();

        if(!ARCHIVO_FUNCIONES.exists()){
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo de funciones","ARCHIVO NO CREADO",0);
            return listaDeFuncionesDelArchivo;
        }
        BufferedReader objetoReader = new BufferedReader(new FileReader(ARCHIVO_FUNCIONES));
        String linea;
        while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            String idPelicula = partes[0];
            String fecha = partes[1];
            String hora = partes[2];
            String sala = partes[3];
            Pelicula pelicula = buscarPeliculaPorId(cartelera, idPelicula);
            if(partes.length == 4) {
                Funcion funcionLeida = new Funcion(fecha, hora, sala, pelicula);
                listaDeFuncionesDelArchivo.add(funcionLeida);
            }
        }
        objetoReader.close();
        return listaDeFuncionesDelArchivo;
    }

    private Pelicula buscarPeliculaPorId(List<Pelicula> cartelera, String id) {
        for (Pelicula p : cartelera) {
            if (p.getIdPelicula().equals(id)) { 
                return p;
            }
        }
        return null; // Película no encontrada
    }

    // Validación de media hora entre funciones
    public boolean validarIntervaloEntreFunciones(String salaNueva , String nuevaFecha, String nuevaHora, Pelicula pelicula) throws IOException {
        String nuevaFechaNormalizada = nuevaFecha.replace("/", "");
        if(!ARCHIVO_FUNCIONES.exists()){
            JOptionPane.showMessageDialog(null,"Puedes agregar a la hora que quieras","ARCHIVO NO CREADO",1);
            return true;
        }
        try (BufferedReader objetReader = new BufferedReader(new FileReader(ARCHIVO_FUNCIONES))){
            String linea;
            while((linea = objetReader.readLine()) != null){
                String[] datos = linea.split("|");
                String fecha = datos[1];
                String hora = datos[2];
                String sala = datos[3];

                if(!fecha.equals(nuevaFechaNormalizada)) continue;
                if(!sala.equals(salaNueva)) continue;
                int horaExistente = Integer.parseInt(hora.substring(0,2)) * 60 + Integer.parseInt(hora.substring(2,4));
                int horaNueva = Integer.parseInt(nuevaHora.substring(0,2)) * 60 + Integer.parseInt(nuevaHora.substring(2,4));
                if(Math.abs(horaExistente - horaNueva) < 30){
                    Funcion unaFuncion = new Funcion(fecha, hora, sala, pelicula);
                    JOptionPane.showMessageDialog(null,"Coincide con: "+ unaFuncion.toString(),"Hay una funcion programada:",0);
                    return false;
                }
            }
        }
        return true;
    }       
    
    // Método para guardar peliculas en el archivo "boletosComprados.txt"
    public void guardarBoletosEnArchivo(Boleto boleto) throws IOException{ 
        FileWriter objetoFileWriter = new FileWriter(BOLETOS_COMPRADOS,true);
        objetoFileWriter.write(boleto.toString() + "\n");
        objetoFileWriter.close();
    }

    // Método para cargar los boletos comprados del archivo "boletosComprados.txt"
    public List<Boleto> cargarBoletosEnArchivo (List<Pelicula> cartelera) throws IOException {
        List<Boleto> listaDeBoletosDelArchivo = new ArrayList<>();

        if(!BOLETOS_COMPRADOS.exists()) {
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return listaDeBoletosDelArchivo;
        }

        BufferedReader objetoReader = new BufferedReader(new FileReader(BOLETOS_COMPRADOS));
        String linea;

        while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            String idPelicula = partes[0];
            String fecha = partes[1];
            String hora = partes[2];
            String sala = partes[3];
            String asiento = partes[4];
            Pelicula pelicula = buscarPeliculaPorId(cartelera, idPelicula);
            if(partes.length == 5) {
                Boleto boletoLeido = new Boleto(fecha, hora, sala, pelicula, asiento);
                listaDeBoletosDelArchivo.add(boletoLeido);
            }
        }
        objetoReader.close();
        return listaDeBoletosDelArchivo;
    }

    public List<Boleto> cargarBoletosPorFuncion (List<Boleto> boletos, Funcion funcion) throws IOException {
        List<Boleto> listaDeBoletosDeLaFuncion = new ArrayList<>();
        for (Boleto b : boletos) {
            if(boletoInFuncion(funcion, b)) listaDeBoletosDeLaFuncion.add(b);
        }
        return listaDeBoletosDeLaFuncion;
    }

    // Método que comprueba si un boleto se encuentra en una función
    private boolean boletoInFuncion(Funcion f, Boleto b) {
        boolean funcionHasBoleto = false;
        boolean esMismoId = f.getIdPelicula().equals(b.getIdPelicula());
        boolean esMismoNombre = f.getNombrePelicula().equals(b.getNombrePelicula());
        boolean esMismaFecha = f.getFecha().equals(b.getFecha());
        boolean esMismaHora = f.getHora().equals(b.getHora());
        boolean esMismaSala = f.getSala().equals(b.getSala());
        if (esMismoId && esMismoNombre && esMismaFecha && esMismaHora && esMismaSala) funcionHasBoleto = true;
        return funcionHasBoleto;
    }

    // Método para guardar órdenes en el archivo "historialDeDulceria.txt"
    public void guardarOrdenesDeDulceria(String clave) throws IOException { //La excepcion se maneja en la interfaz grafica 
        FileWriter objetoFileWriter = new FileWriter(HISTORIAL_DULCERIA,true);
        objetoFileWriter.write(clave + "\n");
        objetoFileWriter.close();
    }

    // Método para cargar sólo las órdenes del usuario
    public List<String> cargarOrdenesDeUsuario (Cliente cliente) throws IOException {
        List<String> ordenesDeUsuario = new ArrayList<>();
        String idCliente = cliente.generarIdNombre() + "|";

        if (!HISTORIAL_DULCERIA.exists()) {
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return ordenesDeUsuario;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL_DULCERIA))) {
            String linea;
            // Leemos el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                // Verificamos si la línea comienza con el Id del cliente
                if (linea.startsWith(idCliente)) {
                    ordenesDeUsuario.add(linea);
                }
            }
        }

        return ordenesDeUsuario;
    }

    // Método para cargar las películas del archivo "historialDeDulceria.txt"
    public List<String> cargarHistorialDeDulceria () throws IOException {
        List<String> ordenesDeUsuario = new ArrayList<>();

        if (!HISTORIAL_DULCERIA.exists()) {
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return ordenesDeUsuario;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL_DULCERIA))) {
            String linea;
            // Leemos el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                    ordenesDeUsuario.add(linea);
                }
            }

        return ordenesDeUsuario;
    }

    public void guardarNotificacionCliente(String clave) throws IOException {
        // Usamos HISTORIAL_DULCERIA para este propósito, como se define en el código original.
        FileWriter objetoFileWriter = new FileWriter(HISTORIAL_DULCERIA, true);
        objetoFileWriter.write(clave + "\n");
        objetoFileWriter.close();
    }

    public void guardarHistorialVendedor(String idVendedor, String logEntry) throws IOException {
        String nombreArchivo = HISTORIAL_VENDEDOR_PREFIX + idVendedor + ".txt";
        
        FileWriter objetoFileWriter = new FileWriter(nombreArchivo, true); // true para adjuntar
        objetoFileWriter.write(logEntry + "\n");
        objetoFileWriter.close();
    }  
    
    public String buscarOrdenEnHistorialVendedor(String claveOrden, String idVendedor) throws IOException {
        String nombreArchivo = HISTORIAL_VENDEDOR_PREFIX + idVendedor + ".txt";
        File archivoVendedor = new File(nombreArchivo);

        if (!archivoVendedor.exists()) {
            return null; 
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoVendedor))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Clave: " + claveOrden + " |")) {
                    return linea;
                }
            }
        }
        return null; // Clave no encontrada en este historial
    }

    public void guardarMensajeNotificacion(String mensaje) throws IOException {
        // Usamos 'false' para sobrescribir el archivo en cada llamada
        FileWriter objetoFileWriter = new FileWriter(ESTADO_ORDEN, false); 
        objetoFileWriter.write(mensaje);
        objetoFileWriter.close();
    }

    public String leerMensajeNotificacion() throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ESTADO_ORDEN))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, retornamos un mensaje por defecto o vacío.
            return "El estado de la orden no está disponible.";
        }
        return contenido.toString().trim();
    }

    // Generar el nombre del archivo de asientos de una función
    public String generarNombreArchivo(Funcion funcion) {
        // Tomamos los atributos clave y normalizamos:
        String pelicula = funcion.getNombrePelicula().replaceAll("\\s+", "_").toUpperCase();
        String sala = funcion.getSala().replaceAll("\\s+", "_").toUpperCase();
        String fecha = funcion.getFecha().replaceAll("[/-]", ""); 
        String hora = funcion.getHora().replaceAll("[:]", "");

        // Formato del identificador
        String nombre = pelicula + "_" + sala + "_" + fecha + "_" + hora + ".txt";
        
        // Devolvemos la ruta completa
        return RUTA_BASE_ASIENTOS + nombre;
    }

    public void guardarAsientos(Funcion funcion, String[][] asientos) throws IOException {
        String rutaArchivo = generarNombreArchivo(funcion);

        if (asientos == null) {
            throw new IllegalArgumentException("El mapa de asientos de la función no puede ser nulo al guardar.");
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < asientos.length; i++) {
                StringBuilder linea = new StringBuilder();
                for (int j = 0; j < asientos[i].length; j++) {
                    linea.append(asientos[i][j]);
                    if (j < asientos[i].length - 1) {
                        linea.append("\t");
                    }
                }
                bw.write(linea.toString());
                // Agregamos un salto de línea para separar las filas
                bw.newLine(); 
            }
        } catch (IOException e) {
            throw new IOException("Fallo al guardar el mapa de asientos en " + rutaArchivo, e); 
        }
    }

    public String[][] cargarAsientos(Funcion funcion) {
        String rutaArchivo = generarNombreArchivo(funcion);
        File archivo = new File(rutaArchivo);
        
        if (!archivo.exists()) {
            
            // Inicializar la matriz de asientos
            String[][] asientosIniciales = null;
            switch (funcion.getSala().toUpperCase()) {
                case "A", "SALA A" -> asientosIniciales = obtenerAsientosA();
                case "B", "SALA B" -> asientosIniciales = obtenerAsientosB();
                case "VIP", "SALA VIP" -> asientosIniciales = obtenerAsientosVIP();
            }
            
            try {
                // Guardamos la matriz inicial en el nuevo archivo
                guardarAsientos(funcion, asientosIniciales); 
                System.out.println("Archivo de asientos inicial creado con éxito en: " + rutaArchivo);
                return asientosIniciales; // Devolvemos el mapa inicial creado
            } catch (IOException e) {
                return null; // Error grave de escritura
            }
        }

        List<String[]> filasAsientos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Se usa un tabulador para separar los asientos
                String[] asientosEnFila = linea.split("\t"); 
                filasAsientos.add(asientosEnFila);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de asientos: " + e.getMessage());
            return null; // En caso de error de lectura
        }

        if (filasAsientos.isEmpty()) {
            // Podría ser un archivo vacío, lo tratamos como un error o volvemos a inicializar
            return null; 
        }
        
        // Convertimos la lista dinámica a String[][]
        int numFilas = filasAsientos.size();
        int numColumnas = filasAsientos.get(0).length;
        String[][] asientos = new String[numFilas][numColumnas];
        
        for (int i = 0; i < numFilas; i++) {
            // Copiamos el array de Strings directamente
            asientos[i] = filasAsientos.get(i); 
        }
        return asientos;
    }

    // Funciones para obtener los asientos

    // Función para convertir un valor numérico de fila a letra
    public String obtenerLetraFila(int fila) {
        char letra = (char) ('A' + fila); // Convierte el número de fila a letra (0 -> A, 1 -> B, etc.)
        return String.valueOf(letra);
    }

    public int obtenerNumeroFila(String letra) {
        return letra.charAt(0) - 'A'; // Convierte la letra de fila a número (A -> 0, B -> 1, etc.)
    }

    
    // Función para obtener los asientos de la sala, así como su disponibilidad
    public String[][] obtenerAsientosA() {
        String[][] asientosA = new String[10][15];
        for (int i = 0; i < asientosA.length; i++) {
            String fila = obtenerLetraFila(i);
            for (int j = 0; j < asientosA[i].length; j++) {
                int columna = j + 1;
                asientosA[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
            }
        }
        return asientosA;
    }

    public String[][] obtenerAsientosB() {
        String[][] asientosB = new String[10][15];
        for (int i = 0; i < asientosB.length; i++) {
            String fila = obtenerLetraFila(i);

            for (int j = 0; j < asientosB[i].length; j++) {

                boolean esFilaPasillo = (i >= 0 && i <= 3); 
                
                boolean esPasilloIzquierdo = (j >= 0 && j <= 3); 
                
                boolean esPasilloDerecho = (j >= 11 && j <= 14); 

                if (esFilaPasillo && (esPasilloIzquierdo || esPasilloDerecho)) {
                    asientosB[i][j] = "    "; 
                } else {
                    int columna = j + 1;
                    asientosB[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
                }
            }
        }
        return asientosB;
    }

    public String[][] obtenerAsientosVIP() {
        String[][] asientosVIP = new String[8][10];
        for (int i = 0; i < asientosVIP.length; i++) {
            String fila = obtenerLetraFila(i);
            for (int j = 0; j < asientosVIP[i].length; j++) {
                boolean esFilaIzqPasillo = (i >= 2 && i <= 3); 
                boolean esFilaDerPasillo = (i >= 6 && i <= 7); 
                if (esFilaIzqPasillo || esFilaDerPasillo) {
                asientosVIP[i][j] = "    ";
                } else {
                    int columna = j + 1;
                    asientosVIP[i][j] = fila + columna + "[O]"; // O representa un asiento disponible
                }
            }
        }
        return asientosVIP;
    }
}
