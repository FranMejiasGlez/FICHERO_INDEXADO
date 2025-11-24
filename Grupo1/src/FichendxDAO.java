
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
    private short tamanioRegistro = 82;

    public FichendxDAO(RandomAccessFile nFich, String modoApertura) {
        this.nFich = nFich;

        //TODO: Validar modo
        this.modoApertura = modoApertura;
    }

    public static boolean isFf() {
        return ff;
    }

    public static void setFf(boolean aFf) {
        ff = aFf;
    }

    @Override
    public Empleado leerRegistro() throws IOException {

        setFf(false);

        String dni, nomApe;
        Sexo sexo;
        float salario;
        Fecha fecha;
        short anio;
        byte mes, dia;
        Provincia provincia;
        Tipo tipoEmpleado;
        Empleado emple = null;

        try {
            dni = this.nFich.readUTF();
            nomApe = leerCaracteres((byte) 30);
            sexo = Sexo.fromCodigo(this.nFich.readChar());
            salario = this.nFich.readFloat();
            anio = this.nFich.readShort();
            mes = this.nFich.readByte();
            dia = this.nFich.readByte();
            fecha = new Fecha(anio, mes, dia);//Validar fe
            provincia = Provincia.fromCodigo(this.nFich.readByte());
            tipoEmpleado = Tipo.fromCodigo(this.nFich.readChar());

            emple = new Empleado(dni, nomApe, sexo, salario, fecha,
                    tipoEmpleado, provincia);

        } catch (EOFException eof) {
            System.out.println("Fin de fichero");
            setFf(true);
        }

        return emple;
    }

    @Override
    public Empleado leerRegistro(long valor) throws IOException {

        String dni, nomApe;
        Sexo sexo;
        float salario;
        Fecha fecha;
        short anio;
        byte mes, dia;
        Provincia provincia;
        Tipo tipoEmpleado;


        posicionar(valor);

        dni = this.nFich.readUTF();
        nomApe = leerCaracteres((byte) 30);
        sexo = Sexo.fromCodigo(this.nFich.readChar());
        salario = this.nFich.readFloat();
        anio = this.nFich.readShort();
        mes = this.nFich.readByte();
        dia = this.nFich.readByte();
        fecha = new Fecha(anio, mes, dia);//Validar fecha
        provincia = Provincia.fromCodigo(this.nFich.readByte());
        tipoEmpleado = Tipo.fromCodigo(this.nFich.readChar());

        return new Empleado(dni, nomApe, sexo, salario, fecha,
                tipoEmpleado, provincia);

    }

//TODO: No esta completo creo xddd.sd.s,d.
    @Override
    public void posicionar(long valor) {
        try {
            this.nFich.seek(valor);
        } catch (IOException ex) {
            System.out.println("Error de E/S posicionando cursor");
        }
    }

    @Override
    public short getTamanioRegistro() {
        return this.tamanioRegistro;
    }

    @Override
    public String cambiaACadenaFija(String dato, byte longitud) {
        StringBuilder cadenaFija = new StringBuilder(dato);
        cadenaFija.setLength(longitud);

        return new String(cadenaFija);
    }

    @Override
    public String leerCaracteres(byte cantidad) {
        char caracterNomApe;
        String nombre = "";
        for (int i = 1; i <= 30; i++) {
            try {
                caracterNomApe = this.nFich.readChar();
                nombre = nombre + caracterNomApe;
            } catch (IOException ex) {
                System.out.println("Error de E/S leyendo caracteres de nomApe");
            }

        }
        return nombre;
    }

    //TODO: Faltan cosas-... xdddd
    public void escribirEmpleado(Empleado empleado) throws IOException {
        this.nFich.writeUTF(empleado.getDni());
        this.nFich.writeUTF(empleado.getNomApe());
        this.nFich.writeChar(empleado.getSexo().getCodigo());
        this.nFich.writeFloat(empleado.getSalario());
        this.nFich.writeShort(empleado.getFechaIngreso().getAnio());
        this.nFich.writeShort(empleado.getFechaIngreso().getMes());
        this.nFich.writeShort(empleado.getFechaIngreso().getDia());
        this.nFich.writeByte(empleado.getProvincia().getCodigo());
        this.nFich.writeChar(empleado.getTipo().getCodigo());
    }
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
