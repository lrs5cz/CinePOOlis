import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
public class GestorDearchivosAdministrador {
    //Se crea el archivo de peliculas 
    private File archivoDePeliculas = new File("peliculasAgregadas.txt");
    private File archivoDeFunciones = new File("funcionesAgregadas.txt");
    //Metodo para guardar peliculas en el archivo "peliculasAgregadas.txt"
    public void guardarPeliculaEnArchivo(Pelicula unaPelicula) throws IOException{//La excepcion se maneja en la interfaz grafica 
        FileWriter objetoFileWriter = new FileWriter(archivoDePeliculas,true);
        objetoFileWriter.write(unaPelicula.toString() + "\n");
        objetoFileWriter.close();
    }
    //Metodo para cargar las peliculas del archivo "peliculasAgregadas.txt"
    public List<Pelicula> cargarPeliculas() throws IOException{
        List<Pelicula> listaDePeliculasDelArchivo = new ArrayList<>();
        if(!archivoDePeliculas.exists()){
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo","ARCHIVO NO CREADO",0);
            return listaDePeliculasDelArchivo;
        }
        BufferedReader objetoReader = new BufferedReader(new FileReader(archivoDePeliculas));
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
    public void guardarFuncionesEnArchivo(Funcion unaFuncion) throws IOException{
        FileWriter objetoFileWriter = new FileWriter(archivoDeFunciones,true);
        objetoFileWriter.write(unaFuncion.toString() + "\n");
        objetoFileWriter.close();
    }  
    public List<Funcion> mostrarFunciones(String fecha, String sala) throws IOException{
        List<Funcion> listaDeFuncionesDelArchivo = new ArrayList<>();
        File archivoDeFunciones = new File("funcionesAgregadas.txt");
        if(!archivoDeFunciones.exists()){
            JOptionPane.showMessageDialog(null,"Aun no esta creado el archivo de funciones","ARCHIVO NO CREADO",0);
            return listaDeFuncionesDelArchivo;
        }
        BufferedReader objetoReader = new BufferedReader(new FileReader(archivoDeFunciones));
        String linea;
         while((linea = objetoReader.readLine()) != null){
            String[] partes = linea.split("\\|");
            if(partes.length == 4){
                Funcion funcionLeida = new Funcion(partes[0],partes[1],partes[2],partes[3]);
                listaDeFuncionesDelArchivo.add(funcionLeida);
            }
        }
        objetoReader.close();
        return listaDeFuncionesDelArchivo;

    }
    //Validación de media hora entre funciones
    public boolean validarIntervaloEntreFunciones(String salaNueva , String nuevaFecha, String nuevaHora) throws IOException {
        if(!archivoDeFunciones.exists()){
            JOptionPane.showMessageDialog(null,"Puedes agregar a la hora que quieras","ARCHIVO NO CREADO",1);
            return true;
        }
        try(BufferedReader objetReader = new BufferedReader(new FileReader(archivoDeFunciones))){
            String linea;
            while((linea = objetReader.readLine()) != null){
                String[] datos = linea.split("\\|");
                String id = datos[0];
                String fecha = datos[1];
                String hora = datos[2];
                String sala = datos[3];

                if(!fecha.equals(nuevaFecha)) continue;
                if(!sala.equals(salaNueva)) continue;
                int horaExistente = Integer.parseInt(hora.substring(0,2)) * 60 + Integer.parseInt(hora.substring(2,4));
                int horaNueva = Integer.parseInt(nuevaHora.substring(0,2)) * 60 + Integer.parseInt(nuevaHora.substring(2,4));
                if(Math.abs(horaExistente - horaNueva) < 30){
                    Funcion unaFuncion = new Funcion(id, fecha, hora, sala);
                    JOptionPane.showMessageDialog(null,"Coincide con: "+ unaFuncion.toString(),"Hay una funcion programada:",0);
                    return false;
                }
            }
        }
        return true;
    }       
    
}
