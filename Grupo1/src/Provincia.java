/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author andyj
 */
public enum Provincia {

    ALMERIA((byte) 1),
    CADIZ((byte) 2),
    CORDOBA((byte) 3),
    GRANADA((byte) 4),
    HUELVA((byte) 5),
    JAEN((byte) 6),
    MALAGA((byte) 7),
    SEVILLA((byte) 8);
    private final byte codigo;

    Provincia(byte codigo) {
        this.codigo = codigo;
    }

    public byte getCodigo() {
        return codigo;
    }

    public static Provincia fromCodigo(byte codigo) {
        for (Provincia p : values()) {
            if (p.codigo == codigo) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {

        return name();
    }
}