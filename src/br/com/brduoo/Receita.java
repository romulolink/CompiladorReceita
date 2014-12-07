package br.com.brduoo;

import java.util.*;

public class Receita {



    private String programa;
    private Token token;
    private String prox;
    private String strExpr;
    private Hashtable<String,Token> Tabela; //Tokens
    private int linha;
    private int pont;
    private int endBase; //Base do endereçamento de variáveis;


    public Receita(String programa) {
        this.programa = programa;
        Tabela = new Hashtable<String,Token>();
        prox = " ";
        token = analex();
        pont = 0;
        endBase = 0;

        if (programa()){
            System.out.println("Programa correto");
        }
    }


    public String proximo() {
        try {
            String resultado = programa.charAt(pont)+"";
            pont++;
            return resultado;
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean branco(String p) {
        return p.equals(" ") || p.equals("\t");
    }
    public boolean newLine(String p) {
        return p.equals("\n");
    }
    public void nada() {}

    public boolean digito(String p) {
        return ('0' <= p.charAt(0)) && (p.charAt(0)<='9');
    }
    public boolean letra(String p) {
        return (('a' <= p.charAt(0)) && (p.charAt(0)<='z'))  ||
                (('A' <= p.charAt(0)) && (p.charAt(0)<='Z')) ||
                (p.charAt(0) == '_');
    }


    // @return Token
    public Token analex() {
        try {
            for ( ; ; prox=proximo() ) {
                if ( branco(prox)) {
                    nada();
                } else if ( newLine(prox) ) {
                    linha = linha+1;
                } else {
                    break;
                }
            }
            if (digito(prox)) {
                String v="";
                do {
                    v = v + prox;
                    prox = proximo();
                } while (digito(prox));
                return new Token(v, "num");
            }
            if (letra(prox)) {
                String s="";
                do {
                    s = s + prox;
                    prox = proximo();
                } while (letra(prox));
                Token w = Tabela.get(s);
                if ( w != null ) {
                    return w;
                } else {
                    w = new Token(s,"id");
                    Tabela.put(s, w);
                    return w;
                }
            }
            /* se atingiu este ponto, o caracter em prox é uma token */
            if (prox.equals("=")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("==","==");
                } else {
                    return new Token("=","=");
                }
            } else if (prox.equals("!")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("!=","!=");
                } else {
                    return new Token("!","!");
                }
            } else if (prox.equals("<")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("<=","<=");
                } else {
                    return new Token("<","<");
                }
            } else if (prox.equals(">")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token(">=",">=");
                } else {
                    return new Token(">",">");
                }
            } else if (prox.equals("+")) {
                prox = proximo();
                if (prox.equals("+")) {
                    prox = " ";
                    return new Token("++","++");
                } else if (prox.equals("=")) {
                    prox = " ";
                    return new Token("+=","+=");
                } else {
                    return new Token("+","+");
                }
            } else if (prox.equals("-")) {
                prox = proximo();
                if (prox.equals("-")) {
                    prox = " ";
                    return new Token("--","--");
                } else if (prox.equals("=")) {
                    prox = " ";
                    return new Token("-=","-=");
                } else {
                    return new Token("-","-");
                }
            } else if (prox.equals("*")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("*=","*=");
                } else {
                    return new Token("*","*");
                }
            } else if (prox.equals("/")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("/=","/=");
                } else {
                    return new Token("/","/");
                }
            } else if (prox.equals("&")) {
                prox = proximo();
                if (prox.equals("&")) {
                    prox = " ";
                    return new Token("&&","&&");
                } else {
                    return new Token("&","&");
                }
            } else if (prox.equals("|")) {
                prox = proximo();
                if (prox.equals("|")) {
                    prox = " ";
                    return new Token("||","||");
                } else {
                    return new Token("|","|");
                }
            } else { // identifica os demais simbolos como por exeplo chaves e colchetes
                Token w = new Token(prox,prox);
                prox = " "; /* initialization */
                return w;
            }
        } catch (NullPointerException e) {
            return new Token("fim","fim");
        }
    }

    public boolean programa() {
        if (!match("id")) return Erro("receita: esperado!");
        return true;
    }

    public boolean bloco() {
        return true;
    }


    /* implementação dos diagramas hierárquicos */
    public boolean Erro(String msg) {
        System.out.println(linha + ": Erro: " + msg);
        return false;
    }

    public boolean match(String x) {
        if (token.igual(x)) {
            token = analex();
            return true;
        } else {
            return false;
        }
    }








}
