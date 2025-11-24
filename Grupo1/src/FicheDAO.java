
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mejias Gonzalez Francisco
 */
public class FicheDAO {

    public static boolean ff;
    public File fiche;

    public FicheDAO(File fiche) {
        this.fiche = fiche;
    }

    public List<Empleado> leerFichero(DataInputStream data) throws FileNotFoundException, IOException {
        List<Empleado> lista = new LinkedList();
        Empleado emple;
        while (!FicheDAO.ff) {
            emple = leerRegistro(data);
            if (emple != null) {
                lista.add(emple);
            }
        }
        return lista;
    }

    public Empleado leerRegistro(DataInputStream data)
            throws FileNotFoundException, IOException {
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

            //Construir el empleado con los datos leidos
            emple = new Empleado(nombreApes, sexoFromChar,
                    salario, fechaIngreso, tipoEmpleFromChar, provincia);

        } catch (EOFException eofe) {
            ff = true;
            System.out.println("Fin de fichero");
        }
        return emple;
    }

    public void escribir(DataOutputStream data, Empleado reg) {


        try {

            data.writeUTF(reg.getNomApe());
            //Escribir sexo
            data.writeChar(reg.getSexo().getCodigo());
            //Escribir salario
            data.writeFloat(reg.getSalario());
            //Escribir anio ingreso
            data.writeShort(reg.getFechaIngreso().getAnio());
            //Escribir mes ingreso
            data.writeByte(reg.getFechaIngreso().getMes());
            //Escribir dia ingreso
            data.writeByte(reg.getFechaIngreso().getDia());
            //Escribir tipo empleado
            data.writeChar(reg.getTipo().getCodigo());
            //Escribir provincia empleado
            data.writeByte(reg.getProvincia().getCodigo());
        } catch (IOException ioe) {
            System.out.println("Error de E/S al escribir empleado en fichero");
        }


    }

    public int getNumeroRegistros() {
        int numRegistros = 0;
        FicheDAO.ff = false;

        try (DataInputStream data = new DataInputStream(
                new FileInputStream(fiche))) {

            while (!FicheDAO.ff) {

                try {
                    // Intentar leer un registro completo

                    Empleado emple = leerRegistro(data);
                    if (emple != null) {
                        numRegistros++;
                    }
                } catch (EOFException eofe) {
                    // Fin del fichero
                    FicheDAO.ff = true;
                }

            }
        } catch (IOException ioe) {
            System.out.println("Error al contar registros: " + ioe.getMessage());
        }
        System.out.println(numRegistros + " Empleados totales.");
        return numRegistros;
    }
}
