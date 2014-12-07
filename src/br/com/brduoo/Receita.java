package br.com.brduoo;

import java.util.*;

public class Receita {



    private String programa;
    private Token token;
    private String prox;
    private int linha;
    private int pont;
    private Hashtable<String,Token> Tabela; //Tokens
    private String strExpr;
    private int endBase; //Base do endereçamento de variáveis;




    public Receita(String programa) {
        this.programa = programa;
        pont = 0;
        linha = 0;
        Tabela = new Hashtable<String,Token>();


        Tabela.put("grama", new Token("grama","un_medida"));
        Tabela.put("xicara", new Token("xicara","un_medida"));
        Tabela.put("colher", new Token("colher","un_medida"));
        Tabela.put("pitada", new Token("pitada","un_medida"));
        Tabela.put("dente", new Token("dente","un_medida"));

        Tabela.put("ingredientes", new Token("ingredientes","ingredientes"));
        Tabela.put("modo_de_preparo", new Token("modo_de_preparo","modo_de_preparo"));

        prox = " ";
        token = analex();
        endBase = 0;

        if (programa()){
            System.out.println("Receita correta");
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

        if (!texto()) return Erro("titulo esperado!");
        if (!match("ingredientes")) return Erro("'ingredientes' esperado!");
        if (!match(":")) return Erro("':' esperado após ingredientes!");

        // enquanto não achar o modo de preparo continuar conferindo a lista
        while(!match("modo_de_preparo")){

            if (!lista_ing()) return Erro("erro na lista de ingredientes!");

        }

        if (!match(":")) return Erro("':' esperado após modo de preparo!");


        if(!lista_instrucoes()) return Erro("Esperada lista de instruçoes!");
        return true;
    }

    public boolean lista_ing() {
        if (!match("-")) return Erro("'-' esperado antes do item de ingrediente!");
        if (!match("num")) return Erro("num esperado!");

        // unidade de medida é opcional
        if(token.igual("un_medida")) {
            if (!match("un_medida")) return Erro("unidade de medida errado!");
        }

        if (!match("id")) return Erro("ingrediente esperado!");

        if (!match(";")) return Erro("; esperado!");

        return true;

    }

    public boolean texto(){

        while(!match(".")){

            if (!match("id")) return Erro("titulo da receita esperado!");
        }

        return true;
    }


    public boolean lista_instrucoes() {
        if (!match("num")) return Erro("numero esperado!");
        if (!match(")")) return Erro("')' esperado após o numero");

        if (!texto()) return Erro("instrução esperada!");

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
