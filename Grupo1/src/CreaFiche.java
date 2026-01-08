
import FichendxDAO.Registro.Provincia;
import FichendxDAO.Registro.Sexo;
import FichendxDAO.Registro.Fecha;
import FichendxDAO.Registro.Empleado;
import FichendxDAO.Registro.Tipo;
import FichendxDAO.FichendxDAO;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mejias Gonzalez Francisco, Andy Jan
 */
public class CreaFiche {

    public static boolean ff;
    public static File fiche;

    private static Empleado leerRegistro(DataInputStream data)
            throws FileNotFoundException, IOException {
        String dni;
        String nombreApes;
        char sexo, tipoEmple;
        Provincia provincia;
        float salario;
        byte mes, dia;
        short anio;
        Sexo sexoFromChar;
        Tipo tipoEmpleFromChar;
        Fecha fechaIngreso;
        Empleado emple = null;

        try {
            ff = false;
            //Leer nombreApes
            nombreApes = data.readUTF().trim();
            //Leer sexo
            sexo = data.readChar();
            sexoFromChar = Sexo.fromCodigo(sexo);
            //Leer salario
            salario = data.readFloat();
            //Leer anio ingreso
            anio = data.readShort();
            //Leer mes ingreso
            mes = data.readByte();
            //Leer dia ingreso
            dia = data.readByte();
            //Construir fechaIngreso
            fechaIngreso = new Fecha(anio, mes, dia);

            //Leer tipo emple
            tipoEmple = data.readChar();
            tipoEmpleFromChar = Tipo.fromCodigo(tipoEmple);
            //Leer provincia emple
            provincia = Provincia.fromCodigo(data.readByte());

            dni = pedirDNI(nombreApes);

            //Construir el empleado con los datos leidos
            emple = new Empleado(dni, nombreApes, sexoFromChar,
                    salario, fechaIngreso, tipoEmpleFromChar, provincia);

        } catch (EOFException eofe) {
            ff = true;
            System.out.println("Fin de fichero");
        }
        return emple;
    }

    private static String pedirDNI(String nombre) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String dni = "";
        do {
            try {
                System.out.print("Introduce el DNI de " + nombre + ": ");
                dni = br.readLine();
            } catch (IOException e) {
                System.out.println("Error leyendo el DNI.");
            }
        } while (!dni.matches("^([XYZ]\\d{7}|\\d{8})[A-Z]$"));
        return dni;
    }

    public static void main(String[] args) throws IOException {
        try {
            fiche = new File("fiche.dat");
            RandomAccessFile raf;
            raf = new RandomAccessFile("fichendx.dat", "rw");
            raf.setLength(0);
            final byte tamanioRegistro = 91;
            FichendxDAO fid = new FichendxDAO(raf, tamanioRegistro);
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(fiche));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreaFiche.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (dis != null) {
                while (!ff) {
                    try {
                        Empleado emple = leerRegistro(dis);
                        if (emple != null) {
                            fid.aniadirRegistro(emple, emple.getDni());
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CreaFiche.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CreaFiche.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreaFiche.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
