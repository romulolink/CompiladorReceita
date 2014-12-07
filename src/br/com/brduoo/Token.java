package br.com.brduoo;

/**
 * Created by Work on 07/12/2014.
 */
public class Token {
    private String lexema;
    private String tipo;

    public Token(String lx, String tp) {
        lexema = lx;
        tipo = tp;
    }

    public boolean igual(String tp) {
        return tipo.equals(tp);
    }

    public String toString() {
        return "<" + tipo + "," + lexema + ">";
    }

    public String lexema() {
        return lexema;
    }
}
