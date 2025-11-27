/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author andyj
 */
public enum Sexo {
    HOMBRE('H'),
    MUJER('M');

    private final char codigo;

    Sexo(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    public static Sexo fromCodigo(char codigo) {
        for (Sexo s : values()) {
            if (s.codigo == codigo) return s;
        }
        return null;
    }
}
