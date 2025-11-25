
import java.io.EOFException;

import java.io.IOException;

import java.io.RandomAccessFile;

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
        if (modoApertura.matches("r|rw")) {
            this.modoApertura = modoApertura;
        } else {
            System.out.println("Modo de apertura no valido");
        }
    }

    public static boolean isFf() {
        return ff;
    }

    public static void setFf(boolean aFf) {
        ff = aFf;
    }

    //Buscar por clave --> DNI, cambiar registro por blanco/vacio/loquesea
    public void borrarEmple(String dni) {
        
    }
    //Buscar por clave --> DNI, mostrar el registro, pedir cambio y 
    //reescribir todo el registro mostrando el nuevo registro con el cambio

    public Empleado modificarEmple(String dni) {
        Empleado emple = null;
        

        return emple;
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

            emple = new Empleado(dni, nomApe, sexo, salario, fecha,
                    tipoEmpleado, provincia);
        } catch (EOFException eof) {
            System.out.println("Fin de fichero");
            setFf(true);
        }
        return emple;
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

    private String cambiaACadenaFija(String dato, byte longitud) {
        StringBuilder cadenaFija = new StringBuilder(dato);
        cadenaFija.setLength(longitud);

        return new String(cadenaFija);
    }

    private String leerCaracteres(byte cantidad) {
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

    public void escribirEmpleado(Empleado empleado) throws IOException {
        this.nFich.writeUTF(empleado.getDni());
        //Tratar el nombre para escribirlo fijo(30)caracteres
        this.nFich.writeUTF(cambiaACadenaFija(empleado.getNomApe(), (byte) 30));
        this.nFich.writeChar(empleado.getSexo().getCodigo());
        this.nFich.writeFloat(empleado.getSalario());
        this.nFich.writeShort(empleado.getFechaIngreso().getAnio());
        this.nFich.writeByte(empleado.getFechaIngreso().getMes());
        this.nFich.writeByte(empleado.getFechaIngreso().getDia());
        this.nFich.writeByte(empleado.getProvincia().getCodigo());
        this.nFich.writeChar(empleado.getTipo().getCodigo());
    }
}
