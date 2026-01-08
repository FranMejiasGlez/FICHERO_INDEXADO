package FichendxDAO;


import FichendxDAO.Registro.*;
import FichendxDAO.Registro.Empleado;
import Indexable.FicheroIndexado;
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
 * @author Grupo 1 (co-op)
 */
public class FichendxDAO extends FicheroIndexado<Empleado> {

    //private static final short TAMANIO_REGISTRO = 91;
//    private boolean ff = false;
//    private static final byte TAM_DNI = 9;
//    private static final byte TAM_NOMBRE = 30;
//    private static int tamanioRegistro;
    
    public FichendxDAO(RandomAccessFile nFich,int tamanioRegistro) {
        super(nFich,tamanioRegistro);
        this.ff=false;
    }

    /*private String cambiarACadenaFija(String dato, byte longitud) {
        StringBuilder cadenaFija = new StringBuilder(dato);
        cadenaFija.setLength(longitud);

        return new String(cadenaFija);
    }*/

   /* protected String leerCaracteres(byte cantidad) {
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
    }*/

    @Override
    public Empleado leerRegistro() {
        try {
            boolean esRegistroValido = false; // control del bucle
            Empleado empleado = null; // registro a devolver

            while (!esRegistroValido) {

                // Leer los campos del registro
                String dni = leerCaracteres(Empleado.TAM_DNI);
                String nombre = leerCaracteres(Empleado.TAM_NOMBRE);
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

    /*public void setFf(boolean aFf) {
        ff = aFf;
    }*/

    @Override
    public int getTamanioRegistro() {
        return this.tamanioRegistro;
    }

    @Override
    public void escribirRegistro(Empleado registro) {

        try {

            // DNI nombre sexo y salario
            nFich.writeChars(cambiarACadenaFija(registro.getDni(), Empleado.TAM_DNI));
            nFich.writeChars(cambiarACadenaFija(registro.getNomApe(), Empleado.TAM_NOMBRE));
            nFich.writeChar(registro.getSexo().getCodigo());
            nFich.writeFloat(registro.getSalario());
            // fecha
            nFich.writeShort(registro.getFechaIngreso().getAnio());
            nFich.writeByte(registro.getFechaIngreso().getMes());
            nFich.writeByte(registro.getFechaIngreso().getDia());
            // tipo y provincia
            nFich.writeChar(registro.getTipo().getCodigo());
            nFich.writeByte(registro.getProvincia().getCodigo());


        } catch (IOException ex) {
            System.err.println("Error de E/S");
        }


    }
}
