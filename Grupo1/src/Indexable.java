
/**
 *
 * @author Mejias Gonzalez Francisco
 */
public abstract class Indexable {

    public abstract Object leerRegistro();

    public abstract Object leerRegistro(String clave);

    public abstract void posicionar(String clave);

    public abstract short getTamanioRegistro();

    public abstract String cambiaACadenaFija();

    public abstract String leerCaracteres();
}
