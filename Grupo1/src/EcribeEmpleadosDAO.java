
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 *
 * @author Pablo Jiménez Fuentes
 */
public class EcribeEmpleadosDAO {
    private static boolean ff;
    
    
    public EcribeEmpleadosDAO(){
        
    }
    public static Empleado leer(DataInputStream lee) {

        Empleado emple1 = null;
        try {
            EcribeEmpleadosDAO.ff = false;
            emple1 = null;

            String nomApe = lee.readUTF();
            char sexo = lee.readChar();
            float salario = lee.readFloat();
            short anio = lee.readShort();
            byte mes = lee.readByte();
            byte dia = lee.readByte();
            char tipoEmple = lee.readChar();
            byte provincia = lee.readByte();


            emple1 = new Empleado(nomApe, sexo, salario, new Fecha(anio,mes,dia), tipoEmple, provincia);
        } catch (EOFException eof) {
            EcribeEmpleadosDAO.ff = true;
            System.out.println("fin de fichero ");
        } catch (IOException e) {
            System.out.println("error entrada E/S");
        }
        return emple1;
    }
}
