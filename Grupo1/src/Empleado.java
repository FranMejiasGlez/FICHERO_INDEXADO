
import java.util.Comparator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author andyj
 */
public class Empleado {

    private String nomApe;
    private Sexo sexo;
    private float salario;
    private Fecha fechaIngreso;
    private Tipo tipo;
    private Provincia provincia;

    public Empleado(String nomApe, Sexo sexo, float salario, Fecha fechaIngreso, Tipo tipo, Provincia provincia) {
        if(nomApe.length() <= 30){
           this.nomApe = nomApe; 
        } else {
            this.nomApe = nomApe.substring(0, 30);
        }
        
        this.sexo = sexo;
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
        this.tipo = tipo;
        this.provincia = provincia;
    }

    public float getSueldo() {
        float total = salario;

        total += (fechaIngreso.aniosTranscurridos() / 3) * 24;

        if (provincia == Provincia.JAEN || provincia == Provincia.HUELVA || provincia == Provincia.ALMERIA) {
            total += salario * 0.10f;
        }
        if (sexo == Sexo.HOMBRE) {
            total += 120;
        }
        return total;
    }

    // Getters y Setters
    public String getNomApe() {
        return nomApe;
    }

    public void setNomApe(String nomApe) {
        this.nomApe = nomApe;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public Fecha getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Fecha fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
//author Mejias Gonzalez Francisco

    @Override
    public String toString() {
        return String.format(
                "Empleado {%n"
                + "  Nombre: %s%n"
                + "  Sexo: %s%n"
                + "  Salario: %.2f€%n"
                + "  Sueldo: %.2f€%n"
                + "  Fecha Ingreso: %s%n"
                + "  Años de Antigüedad: %d%n"
                + "  Tipo: %s%n"
                + "  Provincia: %s%n"
                + "}",
                nomApe, sexo, salario, getSueldo(),
                fechaIngreso, fechaIngreso.aniosTranscurridos(),
                tipo, provincia);
    }
    
    //@author andyj

    public static final Comparator<Empleado> BY_SALARIO = new Comparator<Empleado>() {
        @Override
        public int compare(Empleado o1, Empleado o2) {
            return Float.compare(o1.getSalario(), o2.getSalario());
        }
    };
    public static final Comparator<Empleado> BY_SUELDO = new Comparator<Empleado>() {
        @Override
        public int compare(Empleado o1, Empleado o2) {
            return Float.compare(o1.getSueldo(), o2.getSueldo());
        }
    };
    public static final Comparator<Empleado> BY_NOMBRE = new Comparator<Empleado>() {
        @Override
        public int compare(Empleado o1, Empleado o2) {
            return o1.getNomApe().compareTo(o2.getNomApe());
        }
    };
    public static final Comparator<Empleado> BY_FECHA = new Comparator<Empleado>() {
        @Override
        public int compare(Empleado o1, Empleado o2) {
            return o1.getFechaIngreso().compareTo(o2.getFechaIngreso());
        }
    };
}
