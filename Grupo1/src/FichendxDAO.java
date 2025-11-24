
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mejias Gonzalez Francisco
 */
public class FichendxDAO extends Indexable {

    private static boolean ff;
    private RandomAccessFile nFich;
    private String modoApertura;

    public FichendxDAO(RandomAccessFile nFich, String modoApertura) {
        this.nFich = nFich;
        this.modoApertura = modoApertura;
    }
    /*  public Empleado leerRegistro(DataInputStream data)
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
     dni =;//Leer 18 bytes
     //Leer nombreApes
     nombreApes = data.readUTF().trim();//Leer 60 bytes
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

     public void escribirEmpleado(Empleado reg) {

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
     FichendxDAO.ff = false;

     try (DataInputStream data = new DataInputStream(
     new FileInputStream(fiche))) {

     while (!FichendxDAO.ff) {

     try {
     // Intentar leer un registro completo

     Empleado emple = leerRegistro(data);
     if (emple != null) {
     numRegistros++;
     }
     } catch (EOFException eofe) {
     // Fin del fichero
     FichendxDAO.ff = true;
     }

     }
     } catch (IOException ioe) {
     System.out.println("Error al contar registros: " + ioe.getMessage());
     }
     System.out.println(numRegistros + " Empleados totales.");
     return numRegistros;
     }*/

    @Override
    public Empleado leerRegistro() {
        String dni, nomApe;
        Sexo sexo;
        float salario;
        Fecha fecha;
        Provincia provincia;
        Tipo tipoEmpleado;
        Empleado emple = null;
        try {
            dni = this.nFich.readUTF();
            nomApe = this.nFich.readUTF();
            sexo = Sexo.fromCodigo(this.nFich.readChar());
            salario = this.nFich.readFloat();
            fecha= this.nFich.read
        } catch (IOException ex) {
            System.out.println("Error de E/S");
        }

        return emple;
    }

    @Override
    public Object leerRegistro(String clave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void posicionar(String clave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public short getTamanioRegistro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String cambiaACadenaFija() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String leerCaracteres() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
