
import java.io.IOException;

/**
 *
 * @author Mejias Gonzalez Francisco
 */
public abstract class Indexable {

    public abstract Object leerRegistro() throws IOException;

    public abstract Object leerRegistro(long valor) throws IOException;

    public abstract void posicionar(long valor);

    public abstract short getTamanioRegistro();
}
