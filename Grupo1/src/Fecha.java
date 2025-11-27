
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author andyj
 */
public class Fecha implements Comparable<Fecha> {

    short yyyy;
    byte mm;
    byte dd;

    public Fecha() {//Crea la fecha de hoy
        GregorianCalendar fecha = new GregorianCalendar();
        int yyyy = fecha.get(Calendar.YEAR);
        int mm = fecha.get(Calendar.MONTH) + 1;
        int dd = fecha.get(Calendar.DAY_OF_MONTH);
    }

    public Fecha(short yyyy, byte mm, byte dd) {
        this.yyyy = yyyy;
        this.mm = mm;
        this.dd = dd;
    }

    public int aniosTranscurridos() {
        GregorianCalendar hoy = new GregorianCalendar();

        int yyyyHoy = hoy.get(Calendar.YEAR);
        int mmHoy = hoy.get(Calendar.MONTH) + 1;
        int ddHoy = hoy.get(Calendar.DAY_OF_MONTH);

        int aniosTranscurridos = yyyyHoy - this.yyyy;

        if (mmHoy < this.mm || (mmHoy == this.mm && ddHoy < this.dd)) {
            aniosTranscurridos--;
        }

        return aniosTranscurridos;
    }

    public int mesesTranscurridos() {
        GregorianCalendar hoy = new GregorianCalendar();
        int yearHoy = hoy.get(Calendar.YEAR);
        int monthHoy = hoy.get(Calendar.MONTH) + 1;
        int dayHoy = hoy.get(Calendar.DAY_OF_MONTH);

        // Total months since year 0 for both dates
        int totalMesesHoy = yearHoy * 12 + monthHoy;
        int totalMesesFecha = this.yyyy * 12 + this.mm;

        int meses = totalMesesHoy - totalMesesFecha;

        // Adjust if current day is before stored day in this month
        if (dayHoy < this.dd) {
            meses--;
        }

        return meses;
    }

    @Override
    public String toString() {
        return String.format("%02d-%02d-%04d", dd, mm, yyyy);
    }

    public static Fecha fromString(String dateString) throws NumberFormatException {
        //"dd-mm-yyyy"

        if (!(dateString == null)) {
            if (dateString.matches("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-[0-9]{4}$")) {

                String[] partes = dateString.split("-");
                byte dd = Byte.parseByte(partes[0]);
                byte mm = Byte.parseByte(partes[1]);
                short yyyy = Short.parseShort(partes[2]);
                if (esValida(yyyy, mm, dd)) {
                    return new Fecha(yyyy, mm, dd);
                }

            }
        }
        return null;
    }

    public static boolean esValida(short yyyy, byte mm, byte dd) {
        int diasDeMes;
        
        if(mm > 0 && mm <= 12 && dd > 0 && dd <= 31){
            switch(mm){
                case 4: case 6: case 9 :case 11:
                diasDeMes = 30;
                break;
                case 2:
                    if(Fecha.esBisiesto(yyyy, mm, dd)){
                        diasDeMes = 29;
                    } else{
                        diasDeMes = 28;
                    }
                    break;
                default:
                    diasDeMes = 31;
            }
            return dd <= diasDeMes;
        }
        return false;
    }

    public boolean equals(Fecha otra) {
        return this.yyyy == otra.yyyy && this.mm == otra.mm && this.dd == otra.dd;
    }

    public boolean esBisiesto() {
        return (yyyy % 4 == 0 && yyyy % 100 != 0) || (yyyy % 400 == 0);
    }
    public static boolean esBisiesto(short yyyy, byte mm, byte dd) {
        return (yyyy % 4 == 0 && yyyy % 100 != 0) || (yyyy % 400 == 0);
    }

    public short getAnio() {
        return yyyy;
    }

    public void setAnio(short yyyy) {
        this.yyyy = yyyy;
    }

    public byte getMes() {
        return mm;
    }

    public void setMes(byte mm) {
        this.mm = mm;
    }

    public byte getDia() {
        return dd;
    }

    public void setDia(byte dd) {
        this.dd = dd;
    }

    @Override
    public int compareTo(Fecha otra) {
        GregorianCalendar thisFecha = new GregorianCalendar(this.yyyy, this.mm - 1, this.dd);
        GregorianCalendar otraFecha = new GregorianCalendar(otra.yyyy, otra.mm - 1, otra.dd);
        
        if (thisFecha.before(otraFecha)) {
            return -1;
        }
        if (this.equals(otra)) {
            return 0;
        }
        return 1;
    }
}
