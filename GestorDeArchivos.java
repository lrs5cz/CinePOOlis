import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class GestorDeArchivos {

    // Se crea el archivo de peliculas
    private final File USUARIOS_REGISTRADOS = new File("usuariosRegistrados.dat"); 
    private final File ARCHIVO_PELICULAS = new File("peliculasAgregadas.txt");
    private final File ARCHIVO_FUNCIONES = new File("funcionesAgregadas.txt");
    private final File BOLETOS_COMPRADOS = new File("boletosComprados.txt");
    private final File HISTORIAL_DULCERIA = new File("historialDeDulceria.txt");

    // Método para registrar usuarios en el archivo "usuariosRegistrados.dat"
    public void guardarUsuariosEnArchivo(Persona persona) throws IOException {
        try (
            FileOutputStream fos = new FileOutputStream(USUARIOS_REGISTRADOS, true);
            DataOutputStream dos = new DataOutputStream(fos)
        ){
            dos.writeUTF(persona.getNombre());
            dos.writeUTF(persona.getApellidoP());
            dos.writeUTF(persona.getApellidoM());
            dos.writeInt(persona.getEdad());
            dos.writeUTF(persona.getNumeroCelular());
            dos.writeUTF(persona.getNicknameCuenta());
            dos.writeUTF(persona.getCorreoCuenta());
            dos.writeUTF(persona.getPasswordCuenta());
            if (persona instanceof Cliente) {
                Cliente cliente = (Cliente) persona;
                dos.writeUTF("CLIENTES");
                dos.writeUTF(cliente.getTarjetaBancaria());
            } else if (persona instanceof Administrador) {
                Administrador admin = (Administrador) persona;
                dos.writeUTF("ADMINISTRADORES");
                dos.writeUTF(admin.getTurno());
                dos.writeUTF(admin.getDiasTrabajo());
            } else if (persona instanceof Vendedor) {
                Vendedor vendedor = (Vendedor) persona;
                dos.writeUTF("VENDEDORES");
                dos.writeUTF(vendedor.getTurno());
                dos.writeUTF(vendedor.getDiaDescanso());
            }
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente!", "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // Método para acceder al registro de los usuarios
    public List<Persona> cargarUsuarios() throws IOException {
        List<Persona> usuarios = new ArrayList<>();
        try (
        FileInputStream fis = new FileInputStream(USUARIOS_REGISTRADOS);
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
                    String tipoUsuario = dis.readUTF().toUpperCase();
                    // Creamos las perosnas según su categoría
                    switch(tipoUsuario) { 
                        case "CLIENTES" -> {
                            String tarjetaBancaria = dis.readUTF();
                            Cliente cliente = new Cliente(nombre, apellidoP, apellidoM, edad, celular, cuenta, tarjetaBancaria);
                            usuarios.add(cliente);
                        }
                        case "ADMINISTRADORES" -> {
                            String turno = dis.readUTF();
                            String diasTrabajo = dis.readUTF();
                            Administrador admin = new Administrador(nombre, apellidoP, apellidoM, edad, celular, cuenta, turno ,diasTrabajo);
                            usuarios.add(admin);
                        }
                        case "VENDEDOR" -> {
                            String turno = dis.readUTF();
                            String diaDescanso = dis.readUTF();
                            Vendedor vendedor = new Vendedor(nombre, apellidoP, apellidoM, edad, celular, cuenta, turno, diaDescanso);
                            usuarios.add(vendedor);
                        }
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return usuarios;
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
    public List<Funcion> mostrarFunciones(String fecha, String sala, Pelicula pelicula) throws IOException{
        List<Funcion> listaDeFuncionesDelArchivo = new ArrayList<>();

        if(!ARCHIVO_FUNCIONES.exists()){
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo de funciones","ARCHIVO NO CREADO",0);
            return listaDeFuncionesDelArchivo;
        }
        BufferedReader objetoReader = new BufferedReader(new FileReader(ARCHIVO_FUNCIONES));
        String linea;
        while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            if(partes.length == 5) {
                Funcion funcionLeida = new Funcion(partes[0], partes[1], partes[2], pelicula);
                listaDeFuncionesDelArchivo.add(funcionLeida);
            }
        }
        objetoReader.close();
        return listaDeFuncionesDelArchivo;
    }

    // Validación de media hora entre funciones
    public boolean validarIntervaloEntreFunciones(String salaNueva , String nuevaFecha, String nuevaHora, Pelicula pelicula) throws IOException {
        if(!ARCHIVO_FUNCIONES.exists()){
            JOptionPane.showMessageDialog(null,"Puedes agregar a la hora que quieras","ARCHIVO NO CREADO",1);
            return true;
        }
        try (BufferedReader objetReader = new BufferedReader(new FileReader(ARCHIVO_FUNCIONES))){
            String linea;
            while((linea = objetReader.readLine()) != null){
                String[] datos = linea.split("\\|");
                String fecha = datos[1];
                String hora = datos[2];
                String sala = datos[3];

                if(!fecha.equals(nuevaFecha)) continue;
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
    public List<Boleto> cargarBoletosEnArchivo (Pelicula pelicula) throws IOException {
        List<Boleto> listaDeBoletosDelArchivo = new ArrayList<>();

        if(!BOLETOS_COMPRADOS.exists()) {
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return listaDeBoletosDelArchivo;
        }

        BufferedReader objetoReader = new BufferedReader(new FileReader(BOLETOS_COMPRADOS));
        String linea;

        while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            if(partes.length == 5) {
                Boleto boletoLeido = new Boleto(partes[0], partes[1], partes[2], pelicula, partes[4]);
                listaDeBoletosDelArchivo.add(boletoLeido);
            }
        }
        objetoReader.close();
        return listaDeBoletosDelArchivo;
    }

    // Método para guardar órdenes en el archivo "historialDeDulceria.txt"
    public void guardarOrdenesDeDulceria(Cliente cliente) throws IOException { //La excepcion se maneja en la interfaz grafica 
        FileWriter objetoFileWriter = new FileWriter(HISTORIAL_DULCERIA,true);
        objetoFileWriter.write(cliente.generarClaveDulceria() + "\n");
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
}

    
}


