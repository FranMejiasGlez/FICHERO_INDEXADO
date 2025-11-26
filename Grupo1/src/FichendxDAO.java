
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class FichendxDAO extends Indexable {

    private static final short TAMANIO_REGISTRO = 91;
    private RandomAccessFile nFich;
    private boolean ff = false;
    private static final byte TAM_DNI = 9;
    private static final byte TAM_NOMBRE = 30;

    public FichendxDAO(RandomAccessFile nFich) {
        this.nFich = nFich;
    }

    private String cambiarACadenaFija(String dato, byte longitud) {
        StringBuilder cadenaFija = new StringBuilder(dato);
        cadenaFija.setLength(longitud);

        return new String(cadenaFija);
    }

    protected String leerCaracteres(byte cantidad) {
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
            boolean esRegistroValido = false; // control del bucle
            Empleado empleado = null; // registro a devolver

            while (!esRegistroValido) {

                // Leer los campos del registro
                String dni = leerCaracteres(TAM_DNI);
                String nombre = leerCaracteres(TAM_NOMBRE);
                char sexo = nFich.readChar();
                float salario = nFich.readFloat();
                short year = nFich.readShort();
                byte month = nFich.readByte();
                byte day = nFich.readByte();
                char tipo = nFich.readChar();
                byte prov = nFich.readByte();

                // Detectar si es hueco (DNI lleno de '\0')
                boolean esHueco = true;
                for (int i = 0; i < dni.length(); i++) {
                    if (dni.charAt(i) != '\0') {
                        esHueco = false;
                    }
                }

                // Si NO es hueco, preparar el empleado y marcar como válido
                if (!esHueco) {
                    Fecha fecha = new Fecha(year, month, day);
                    empleado = new Empleado(
                            dni,
                            nombre,
                            Sexo.fromCodigo(sexo),
                            salario,
                            fecha,
                            Tipo.fromCodigo(tipo),
                            Provincia.fromCodigo(prov));
                    esRegistroValido = true; // ahora sí vamos a devolverlo
                }

                // Si es hueco → simplemente dejar que se repita el while
            }

            return empleado; // solo sale del while cuando es válido

        } catch (EOFException eofe) {
            ff = true;
            return null;
        } catch (IOException ex) {
            System.err.println("Error de E/S leyendo registro");
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
        return TAMANIO_REGISTRO;
    }

    @Override
    public void escribirRegistro(Object registro) {
        if (registro instanceof Empleado) {
            try {
                long pos;
                Empleado emple = (Empleado) registro;
                if (!indices.containsKey(emple.getDni())) {
                    pos = getSiguienteHueco(nFich);
                } else {
                    pos = indices.get(emple.getDni());
                }
                posicionar(pos, nFich);
                // DNI nombre sexo y salario
                nFich.writeChars(cambiarACadenaFija(emple.getDni(), TAM_DNI));
                nFich.writeChars(cambiarACadenaFija(emple.getNomApe(), TAM_NOMBRE));
                nFich.writeChar(emple.getSexo().getCodigo());
                nFich.writeFloat(emple.getSalario());
                // fecha
                nFich.writeShort(emple.getFechaIngreso().getAnio());
                nFich.writeByte(emple.getFechaIngreso().getMes());
                nFich.writeByte(emple.getFechaIngreso().getDia());
                // tipo y provincia
                nFich.writeChar(emple.getTipo().getCodigo());
                nFich.writeByte(emple.getProvincia().getCodigo());
                aniadirIndice(emple.getDni(), pos);
                guardarIndices();

            } catch (IOException ex) {
                System.err.println("Error de E/S");
            }
        } else {
            throw new IllegalArgumentException("El registro debe ser un empleado");
        }

    }
}
