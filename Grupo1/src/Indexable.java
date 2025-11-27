
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Grupo1 (co-op)
 */
public abstract class Indexable<T> {

    final File FICHE_INDICES = new File("Indices.dat");
    List<Long> listaHuecos;
    TreeMap<Object, Long> indices;

    public Indexable() {
        inicializarIndices();
    }

    public T leerRegistro(Object clave, RandomAccessFile raf) throws FileNotFoundException, IOException {
        if (!indices.containsKey(clave)) {
            return null;
        } else {
            posicionar(clave, raf);
            return leerRegistro();
        }
    }

    public boolean borrarRegistro(Object clave, RandomAccessFile raf) throws IOException {
        if (!indices.containsKey(clave)) {
            return false;
        } else {
            long pos;
            StringBuilder construirHueco = new StringBuilder();
            construirHueco.setLength(getTamanioRegistro());
            pos = posicionar(clave, raf);
            raf.writeBytes(construirHueco.toString());
            aniadirHueco(pos);
            indices.remove(clave);
            guardarIndices();
            return true;
        }
    }

    public boolean modificarRegistro(T registro, Object claveRegistroACambiar, RandomAccessFile raf) throws IOException {
        if (!indices.containsKey(claveRegistroACambiar)) {
            return false;
        } else {
            posicionar(claveRegistroACambiar, raf);
            
            escribirRegistro(registro);
            return true;
        }

    }

    public void guardarIndices() throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.FICHE_INDICES);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this.indices);
            oos.writeObject(this.listaHuecos);
        } catch (IOException ioe) {
        } finally {
            if (fos != null) {
                fos.close();
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
            //Logger.getLogger(Indexable.class.getName()).log(Level.SEVERE, null, ex);
            this.indices = new TreeMap();
            this.listaHuecos = new LinkedList();
        } catch (IOException ex) {
            Logger.getLogger(Indexable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Indexable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Indexable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public long posicionar(Object clave, RandomAccessFile raf) throws IOException {
        long posicion = (long) indices.get(clave);
        // Go to that position
        raf.seek(posicion);
        return posicion;
    }

    public void posicionar(long posicion, RandomAccessFile raf) throws IOException {
        raf.seek(posicion);
    }

    protected void aniadirIndice(Object clave, long pos) {
        this.indices.put(clave, pos);
    }

    public void aniadirHueco(long pos) {
        listaHuecos.add(pos);
    }

    public long getSiguienteHueco(RandomAccessFile raf) throws IOException {
        try {
            return listaHuecos.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return raf.length();
        }

    }

    public abstract T leerRegistro();

    public abstract int getTamanioRegistro();

    public abstract void escribirRegistro(T registro);
}
