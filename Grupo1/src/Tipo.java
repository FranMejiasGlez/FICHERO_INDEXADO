/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author andyj
 */
public enum Tipo {
    COMISION('C'),
    FIJO('F'),
    DOMICILIO('D');
    
    private final char codigo;

    Tipo(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    public static Tipo fromCodigo(char codigo) {
        for (Tipo t : values()) {
            if (t.codigo == codigo) return t;
        }
        return null;
    }
}
