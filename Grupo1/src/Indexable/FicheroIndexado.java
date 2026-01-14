package Indexable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fran,Alvaro,Andy,Pablo
 * @Correcciones Fran --> Muevo al DAO leerCaracteres y CambiarACadenaFija Quito
 * abstract de getTamanioRegistro 
 * Quito atributo ff de DAO y lo muevo a Indexado
 * Cambio protected por private en metodo aniadirIndice
 */
public abstract class FicheroIndexado<T> {

    final File FICHE_INDICES = new File("Indices.dat");
    List<Long> listaHuecos;
    TreeMap<Object, Long> indices;
    public RandomAccessFile nFich;
    public int tamanioRegistro;
    public boolean ff;

    public FicheroIndexado(RandomAccessFile raf, int tamanioRegistro) {
        inicializarIndices();
        nFich = raf;
        this.tamanioRegistro = tamanioRegistro;
    }

    public boolean isFf() {
        return ff;
    }

    public T leerRegistro(Object clave) throws FileNotFoundException, IOException {
        if (!indices.containsKey(clave)) {
            return null;
        } else {
            posicionar(clave);
            return leerRegistro();
        }
    }

    public boolean borrarRegistro(Object clave) throws IOException {
        if (!indices.containsKey(clave)) {
            return false;
        } else {
            long pos;
            StringBuilder construirHueco = new StringBuilder();
            construirHueco.setLength(getTamanioRegistro());
            pos = posicionar(clave);
            nFich.writeBytes(construirHueco.toString());
            aniadirHueco(pos);
            indices.remove(clave);
            guardarIndices();
            return true;
        }
    }

    public TreeMap<Object, Long> getIndices() {
        return new TreeMap<>(indices);
    }

    public boolean modificarRegistro(T registro, Object claveRegistroACambiar) throws IOException {
        if (!indices.containsKey(claveRegistroACambiar)) {
            return false;
        } else {
            posicionar(claveRegistroACambiar);

            escribirRegistro(registro);
            return true;
        }

    }

    public boolean aniadirRegistro(T registro, Object claveRegistro) throws IOException {
        if (existe(claveRegistro)) {
            return false;
        } else {
            posicionar(getSiguienteHueco());
            aniadirIndice(claveRegistro, nFich.getFilePointer());
            escribirRegistro(registro);
            guardarIndices();
            return true;
        }
    }

    private void guardarIndices() throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(this.FICHE_INDICES);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(this.indices);
            oos.writeObject(this.listaHuecos);
        } catch (IOException ioe) {
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    private boolean inicializarIndices() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.FICHE_INDICES);
            ObjectInputStream ois = new ObjectInputStream(fis);

            this.indices = (TreeMap) ois.readObject();
            this.listaHuecos = (List) ois.readObject();
            return true;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(FicheroIndexado.class.getName()).log(Level.SEVERE, null, ex);
            this.indices = new TreeMap();
            this.listaHuecos = new LinkedList();
        } catch (IOException ex) {
            Logger.getLogger(FicheroIndexado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FicheroIndexado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(FicheroIndexado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public long posicionar(Object clave) throws IOException {
        long posicion = (long) indices.get(clave);
        // Go to that position
        nFich.seek(posicion);
        return posicion;
    }

    public void posicionar(long posicion) throws IOException {
        nFich.seek(posicion);
    }

    private void aniadirIndice(Object clave, long pos) {
        this.indices.put(clave, pos);
    }

    private void aniadirHueco(long pos) {
        listaHuecos.add(pos);
    }

    private long getSiguienteHueco() throws IOException {
        try {
            return listaHuecos.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return nFich.length();
        }

    }

    public boolean existe(Object clave) {
        return indices.containsKey(clave);
    }

    public abstract T leerRegistro();

    public int getTamanioRegistro() {
        return this.tamanioRegistro;
    }

    public abstract void escribirRegistro(T registro);
}
