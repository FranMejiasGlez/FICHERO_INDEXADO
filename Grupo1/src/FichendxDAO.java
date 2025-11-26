
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class FichendxDAO extends Indexable {

    private static short tamanioRegistro = 91;
    private String modoApertura;
    private RandomAccessFile nFich;
    private boolean ff = false;
    private final byte tamDNI = 9;
    private final byte tamNombre = 30;

    public FichendxDAO(String modoApertura, RandomAccessFile nFich, short tamanioRegistro) {
        this.modoApertura = modoApertura;
        this.nFich = nFich;
        this.tamanioRegistro = tamanioRegistro;
    }

    private String cambiarACadenaFija(String dato, byte longitud) {
        StringBuilder cadenaFija = new StringBuilder(dato);
        cadenaFija.setLength(longitud);

        return new String(cadenaFija);
    }

    private String leerCaracteres(byte cantidad) {
        char caracterNomApe;
        String nombre = "";
        for (int i = 1; i <= cantidad; i++) {
            try {
                caracterNomApe = this.nFich.readChar();
                nombre = nombre + caracterNomApe;
            } catch (IOException ex) {
                System.out.println("Error de E/S leyendo caracteres de nomApe");
            }

        }
        return nombre;
    }

    @Override
    public Object leerRegistro() {
        try {
            Empleado empleado;
            Fecha fecha;
            // leemos el resto para construir objeto empleado
            String dni = leerCaracteres(tamDNI);
            String nombre = leerCaracteres(tamNombre);
            char sexo = nFich.readChar();
            float salario = nFich.readFloat();
            short year = nFich.readShort();
            byte month = nFich.readByte();
            byte day = nFich.readByte();
            char tipo = nFich.readChar();
            byte prov = nFich.readByte();

            //Construimos el empleado
            fecha = new Fecha(year, month, day);
            empleado = new Empleado(dni, nombre, Sexo.fromCodigo(sexo), salario, fecha, Tipo.fromCodigo(tipo), Provincia.fromCodigo(prov));
            return empleado;
        } catch (EOFException eofe) {
            ff = true;
            
        } catch (IOException ex) {
            System.err.println("Error de E/S");
            
        }finally{
            return null;
        }
    }
    public boolean isFf() {
        return ff;
    }

    public void setFf(boolean aFf) {
        ff = aFf;
    }
    @Override
    public int getTamanioRegistro() {
        return tamanioRegistro;
    }

    @Override
    public void escribirRegistro(Object registro) {
        if (registro instanceof Empleado) {
            try {
                posicionar(getSiguienteHueco(nFich), nFich);
                Empleado emple = (Empleado) registro;
                //DNI nombre sexo y salario
                cambiarACadenaFija(emple.getDNI(), tamDNI);
                cambiarACadenaFija(emple.getNomApe(), tamNombre);
                nFich.writeChar(emple.getSexo().getCodigo());
                nFich.writeFloat(emple.getSalario());
                //fecha
                nFich.writeShort(emple.getFechaIngreso().getAnio());
                nFich.writeByte(emple.getFechaIngreso().getMes());
                nFich.writeByte(emple.getFechaIngreso().getDia());
                //tipo y provincia
                nFich.writeChar(emple.getTipo().getCodigo());
                nFich.writeByte(emple.getProvincia().getCodigo());
            } catch (IOException ex) {
                System.err.println("Error de E/S");
            }
        } else {
            throw new IllegalArgumentException("El registro debe ser un empleado");
        }


    }
}
