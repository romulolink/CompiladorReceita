package br.com.brduoo;

import java.io.*;

public class CompiladorReceita {

    public static void main(String[] args) {

        try {
            // Abre arquivo com o código fonte
            BufferedReader in = new BufferedReader(new FileReader("C:/Users/Work/Desktop/COMP-tmp/receita.re"));
            String linha, programa;
            linha = in.readLine();
            programa = "";
            // Preenche a variável programa com tod_ o código que estava no arquivo
            while (linha != null) {
                programa = programa + linha + "\n";
                linha = in.readLine();
            }

            Receita compilador = new Receita(programa);

            in.close();
        } catch (IOException e) {
            System.out.println("receita.re não encontrado!");
        }

    }

}
