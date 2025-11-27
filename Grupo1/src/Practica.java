
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mejias Gonzalez Francisco\
 * @author Pablo
 * @author Andy Jan
 */
public class Practica {

    public static Empleado pedirDatos() {
        BufferedReader teclado;
        String entrada, nombre, dni;
        boolean valido;
        float salario = 0;
        char sexo, tipoEmple;
        Fecha fecha;
        String[] campos;
        short anio;
        byte provincia = 0, mes, dia;
        Empleado emple = null;

        try {

            teclado = new BufferedReader(new InputStreamReader(System.in));
            do {
                System.out.print("Introduce un DNI valido: ");
                dni = teclado.readLine();
            } while (!Practica.esDNIValido(dni));
            do {
                System.out.print("Introduce un nombre y apellidos: ");
                entrada = teclado.readLine();
            } while (!Practica.esValido(entrada));
            nombre = entrada;
            do {
                System.out.print("Sexo(M/H): ");
                entrada = teclado.readLine();
            } while (!entrada.matches("[MH]"));
            sexo = entrada.charAt(0);
            do {
                valido = true;
                System.out.print("Introduce un salario >0: ");
                entrada = teclado.readLine();
                try {
                    salario = Float.parseFloat(entrada);
                } catch (NumberFormatException nfe) {
                    System.out.println("Tiene que ser un numérico real");
                    valido = false;
                }
            } while (!valido || salario < 0);
            do {
                do {
                    System.out.print("Introduce una fecha de ingreso valida año/mes/dia(xxxx/xx/xx): ");
                    entrada = teclado.readLine();
                } while (!entrada.matches("\\d{4}\\/\\d{2}\\/\\d{2}"));
                campos = entrada.split("[/]");
                anio = Short.parseShort(campos[0]);
                mes = Byte.parseByte(campos[1]);
                dia = Byte.parseByte(campos[2]);
            } while (!Fecha.esValida(anio, mes, dia));



            do {
                System.out.print("Introduce una letra del tipo del empleado (D,C,F): ");
                entrada = teclado.readLine();
            } while (!entrada.matches("[CFD]"));
            tipoEmple = entrada.charAt(0);
            do {
                valido = true;
                System.out.print("Introduce el número de una provincia(1-8): ");
                try {
                    entrada = teclado.readLine();
                    provincia = Byte.parseByte(entrada);
                } catch (NumberFormatException nfe) {
                    System.out.println("Debe ser un número entre 1 y 8");
                    valido = false;
                }
            } while (!valido || !entrada.matches("[1-8]"));
            fecha = new Fecha(anio, mes, dia);
            emple = new Empleado(dni, nombre, Sexo.fromCodigo(sexo), salario,
                    fecha, Tipo.fromCodigo(tipoEmple), Provincia.fromCodigo(provincia));
        } catch (IOException ioe) {
            System.out.println("Error de E/S pidiendo datos");
        }

        return emple;
    }

    public static boolean esValido(String cadena) {

        if (cadena == null) {
            return false;
        }
        if (cadena.trim().isEmpty()) {
            return false;
        }

        for (char c : cadena.toCharArray()) {

            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean esDNIValido(String dni) {
        return dni.matches("^([XYZ]\\d{7}|\\d{8})[A-Z]$");
    }

    public static void menu() {

        System.out.println("");
        System.out.println("1.Altas");
        System.out.println("2.Bajas");
        System.out.println("3.Modificar salario");
        System.out.println("4.Listado de empleados, ordenado por DNI");
        System.out.println("5.Listado de empleados, orden de almacenamiento");
        System.out.println("6.Salir");
        System.out.println("");
        System.out.println("Introduce una opcion: ");
    }

    public static void main(String[] args) {
        try {
            boolean esValido;
            Empleado emple;
            FichendxDAO fichendxDAO;
            RandomAccessFile raf;
            BufferedReader teclado;
            byte opcion = 0;
            String dni;
            float salario = 0;
            teclado = new BufferedReader(new InputStreamReader(System.in));
            raf = new RandomAccessFile("fichendx.dat", "rw");
            fichendxDAO = new FichendxDAO(raf);
            do {
                try {
                    do {
                        Practica.menu();
                        opcion = Byte.parseByte(teclado.readLine());
                    } while (opcion < 1 || opcion > 6);
                } catch (NumberFormatException nfe) {
                    System.out.println("Dato invalido, teclee otro.");
                } catch (IOException ex) {
                    System.out.println("Error de E/S con teclado");
                }
                switch (opcion) {
                    case 1://Alta de empleado
                        emple = Practica.pedirDatos();
                        fichendxDAO.escribirRegistro(emple);
                        break;
                    case 2://Baja de empleado
                        System.out.println("Introduce el DNI del empleado a borrar: ");
                        try {
                            do {
                                dni = teclado.readLine();
                            } while (!Practica.esDNIValido(dni));

                            System.out.println(fichendxDAO.
                                    borrarRegistro(dni, raf)
                                    ? "Empleado borrado correctamente"
                                    : "Empleado no ha podido ser borrado");
                        } catch (IOException ex) {
                            System.out.println("Error de E/S pidiendo DNI");
                        }

                        break;
                    case 3://Modificar salario empleado
                        try {
                            System.out.println("Introduce el DNI del empleado a cambiar sueldo: ");
                            do {
                                dni = teclado.readLine();
                            } while (!Practica.esDNIValido(dni));
                            emple = (Empleado) fichendxDAO.leerRegistro(dni, raf);
                            System.out.println(emple != null
                                    ? "Introduce nuevo salario"
                                    : "No existe el empleado");
                            if (emple != null) {
                                do {
                                    esValido = true;
                                    try {
                                        salario = Float.parseFloat(teclado.readLine());
                                    } catch (NumberFormatException nfe) {
                                        System.out.println("dato invalido, teclee otro");
                                        esValido = false;
                                    }
                                } while (!esValido);
                                emple.setSalario(salario);
                                System.out.println(fichendxDAO.modificarRegistro(emple,
                                        emple.getDni(), raf)
                                        ? "Empleado modificado correctamente"
                                        : "No se ha podido modificar empleado");
                            }

                        } catch (IOException ex) {
                            System.out.println("Error de E/S pidiendo salario");
                        }


                        break;






                    case 4://Listado por dni
                        System.out.println("Listando por DNI");
                        System.out.println("");
                        for (Map.Entry<Object, Long> entrada : fichendxDAO.indices.entrySet()) {
                            try {
                                emple = (Empleado) fichendxDAO.
                                        leerRegistro(entrada.getKey(), raf);
                                System.out.println(emple.toString());
                            } catch (IOException ex) {
                                System.out.println("Error de E/S leyendo");
                            }

                        }
                        break;
                    case 5:
                        try {
                            //Listado por orden de almacenamiento
                            raf.seek(0);
                            while (raf.getFilePointer() < raf.length()) {
                                emple = (Empleado) fichendxDAO.leerRegistro();
                                if (emple != null) {
                                    System.out.println(emple.toString());
                                }
                            }
                        } catch (IOException ex) {
                            System.out.println("Error de E/S listando por orden de almacenamiento");
                        }
                        break;
                }
            } while (opcion != 6);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Practica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
