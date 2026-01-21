package Indexable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fran,Alvaro,Andy,Pablo
 * @Correcciones Fran --> Muevo al DAO leerCaracteres y CambiarACadenaFija Quito
 * abstract de getTamanioRegistro Quito atributo ff de DAO y lo muevo a Indexado
 * Cambio protected por private en metodo aniadirIndice
 * @Correcciones Álvaro --> cambio de nombres de metodos leer y escribir,
 * añadido close() y nueva gestión de huecos al cerrar el fichero. Cambiar ff al
 * DAO, cambio de algunos accesos.
 *
 */
public abstract class FicheroIndexado<T> {

    private final File FICHE_INDICES = new File("Indices.dat");
    private List<Long> listaHuecos;
    private TreeMap<Object, Long> indices;
    public RandomAccessFile nFich;
    private int tamanioRegistro;

    public FicheroIndexado(RandomAccessFile raf, int tamanioRegistro) {
        inicializarIndices();
        nFich = raf;
        this.tamanioRegistro = tamanioRegistro;
    }

    public T leer(Object clave) throws FileNotFoundException, IOException {
        if (!indices.containsKey(clave)) {
            return null;
        } else {
            posicionar(clave);
            return leer();
        }
    }

    public boolean borrar(Object clave) throws IOException {
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

    public boolean modificar(T registro, Object claveRegistroACambiar) throws IOException {
        if (!indices.containsKey(claveRegistroACambiar)) {
            return false;
        } else {
            posicionar(claveRegistroACambiar);

            escribir(registro);
            return true;
        }

    }

    public boolean escribir(T registro, Object claveRegistro) throws IOException {
        if (existe(claveRegistro)) {
            return false;
        } else {
            posicionar(getSiguienteHueco());
            aniadirIndice(claveRegistro, nFich.getFilePointer());
            escribir(registro);
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
            this.listaHuecos = new LinkedList();

            return true;
        } catch (FileNotFoundException ex) {
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

    public abstract T leer();

    public int getTamanioRegistro() {
        return this.tamanioRegistro;
    }

    public abstract void escribir(T registro);

    public void close() {
        try {
            //Ordenar lista de huecos
            if (listaHuecos != null) {
                Collections.sort(listaHuecos); 
            }
            boolean seguir = true;
            while (seguir) {

                // Seguimos si hay huecos y hay registros que no esten borrados
                seguir = (listaHuecos != null && !listaHuecos.isEmpty()
                        && indices != null && !indices.isEmpty());
                if (seguir) {

                    long posHueco = listaHuecos.get(0);
                    long posUltimoVivo = -1;
                    for (Long p : indices.values()) {
                        if (p != null && p.longValue() > posUltimoVivo) {
                            posUltimoVivo = p.longValue();
                        }
                    }
                    // Solo movemos si el último está detrás del hueco
                    seguir = (posUltimoVivo > posHueco);
                    if (seguir) {
                        // Mover byte a byte
                        for (int i = 0; i < tamanioRegistro; i++) {
                            nFich.seek(posUltimoVivo + i);
                            byte b = nFich.readByte();

                            nFich.seek(posHueco + i);
                            nFich.writeByte(b);
                        }

                        // Actualizar índice
                        Object claveMovida = null;
                        for (Object clave : indices.keySet()) {
                            Long pos = indices.get(clave);
                            if (claveMovida == null && pos != null && pos.longValue() == posUltimoVivo) {
                                claveMovida = clave;
                            }
                        }
                        if (claveMovida != null) {
                            indices.put(claveMovida, posHueco);
                        }

                        // El hueco ya está lleno
                        listaHuecos.remove(0);
                    }
                }
            }
            // Truncado final para que no haya huecos
            long maxVivo = -1;
            if (indices != null && !indices.isEmpty()) {
                for (Long p : indices.values()) {
                    if (p != null && p.longValue() > maxVivo) {
                        maxVivo = p.longValue();
                    }
                }
                nFich.setLength(maxVivo + tamanioRegistro);
            } else {
                nFich.setLength(0);
            }
            if (listaHuecos != null) {
                listaHuecos.clear();
            }
            guardarIndices();
            nFich.close();
        } catch (IOException e) {
            Logger.getLogger(FicheroIndexado.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
