/*
 * Matrix.java
 * Trabalho de Linguagens Formais e Autômatos
 * @author Thiago Almeida Martins Marques - 201120002
 * @author João Vitor Squillace Teixeira - 201120518
 * @author - Caio César Freitas Lara - 201310584
 */

package lfa;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.IOException;


public class Matrix {

    List<String>[][] tabela;
    List<String> listaTemporaria = new ArrayList<String>();
    int numLinhas;
    int numColuna;

    public Matrix(int numLinhas, int numColuna) {
        this.numColuna = numColuna;
        this.numLinhas = numLinhas;

        List myList = new ArrayList<String>();
        tabela = new List[numLinhas][numColuna];

        int contConteudo = 0;
        for (int linha = 0; linha < numLinhas; linha++) {
            for (int coluna = 0; coluna < numColuna; coluna++) {
                tabela[linha][coluna] = myList;
                contConteudo++;
            }
        }
    }
    

    /** Método para retorno da quantidade de estados
     *   @return int - numLinhas*/    
    public int getEstadosQuantidade(){
        
        return numLinhas;
    }
 
        /** Método para gravar as tabelas no arquivo
        *   @param BufferedWriter bw -  manipulação do arquivo
        *   @paraM List<String> alphabet -  alfabeto
        *   @param List<String> estados - estados
        *   @param String nomeAutomato - nome do Autômato a ser escrito no arquivo de saída
        *   @param int quantidadeEstados - quantidade de estados para definição da largura do campo da tabela (formatação)
        */  
    public void exibeTabela(BufferedWriter bw, List<String> alphabet, List<String> estados, String nomeAutomato, int quantidadeEstados) throws IOException {

        bw.write("\n\n\n");
        String temporaria = "";
        String conteudo = "";
        String larguraTabulacao="";
        String tamanhoFormat = "";
        
        switch (nomeAutomato) {
            case "AFNDL":
                 tamanhoFormat = "%"+Integer.toString(quantidadeEstados*5)+"s";
            
                temporaria = String.format(tamanhoFormat, nomeAutomato);
                bw.write(temporaria);
                for (int k = 0; k < alphabet.size(); k++) {
                    temporaria = String.format(tamanhoFormat, alphabet.get(k));
                    bw.write(temporaria);            
                }

                temporaria = String.format(tamanhoFormat, "lambda");
                bw.write(temporaria);
                bw.write("\n");

                for (int linha = 0; linha < numLinhas; linha++) {

                    /*imprime primeira coluna (estados) */
                    conteudo = String.format(tamanhoFormat, estados.get(linha));
                    bw.write(conteudo);

                    for (int coluna = 0; coluna < numColuna - 1; coluna++) {
                        conteudo = tabela[linha][coluna].toString();
                        /*acrescentar {}, remover [], formatar tamanho string*/
                        String newConteudo = conteudo.replace("[", "");
                        newConteudo = newConteudo.replace("]", "");
                        conteudo = String.format(tamanhoFormat, "{" + newConteudo + "}");
                        bw.write(conteudo);
                    }
                    bw.write("\n");
                }

                break;
                
            case "AFND":
             
                tamanhoFormat = "%"+Integer.toString(quantidadeEstados*5)+"s";

                temporaria = String.format(tamanhoFormat, nomeAutomato);
                bw.write(temporaria);

                //imprime cabecalho    
                for (int k = 0; k < alphabet.size(); k++) {
                    temporaria = String.format(tamanhoFormat, alphabet.get(k));
                    bw.write(temporaria);                   
                }

                bw.write("\n");

                for (int linha = 0; linha < numLinhas; linha++) {

                    /*imprime primeira coluna (estados) */
                    conteudo = String.format(tamanhoFormat, estados.get(linha));
                    bw.write(conteudo);

                    for (int coluna = 0; coluna < numColuna; coluna++) {
                        conteudo = tabela[linha][coluna].toString();
                        /*acrescentar {}, remover [], formatar tamanho string*/
                        String newConteudo = conteudo.replace("[", "");
                        newConteudo = newConteudo.replace("]", "");
                        conteudo = String.format(tamanhoFormat, "{" + newConteudo + "}");

                        bw.write(conteudo);

                    }
                    bw.write("\n");
                }

                break;

            case "AFD":
                
                tamanhoFormat = "%"+Integer.toString(quantidadeEstados*6)+"s";
                temporaria = String.format(tamanhoFormat, nomeAutomato);
                bw.write(temporaria);

                for (int k = 0; k < alphabet.size(); k++) {
                    temporaria = String.format(tamanhoFormat, alphabet.get(k));
                    bw.write(temporaria);
                   // System.out.format(tamanhoFormat, alphabet.get(k));
                }

                bw.write("\n");

                for (int linha = 0; linha < numLinhas; linha++) {
                    for (int coluna = 0; coluna < numColuna; coluna++) {
                        conteudo = tabela[linha][coluna].toString();
                        if (!tabela[linha][0].toString().equals("[]")) {
                            /*acrescentar {}, remover [], formatar tamanho string*/
                            String newConteudo = conteudo.replace("[", "");
                            newConteudo = newConteudo.replace("]", "");

                            conteudo = String.format(tamanhoFormat, "<" + newConteudo + ">");

                            String vazio = String.format(tamanhoFormat, "<>");

                            if (conteudo.equals(vazio)) {

                                conteudo = String.format(tamanhoFormat, "-");
                            }

                            if (coluna == 0 && conteudo.equals("-")) {
                                conteudo = " ";
                            }

                            bw.write(conteudo);
                        }
                    }
                  
                    bw.write("\n");
                }
                break;
        }

        bw.write("\n\n");

    }

    
    /** Método para retorno da lista da tabela
     *   @return tabela*/        
    public List getList(int linhaDesejada, int colunaDesejada) {
        return tabela[linhaDesejada][colunaDesejada];
    }

    /** Método para escrever na tabela
     *   @return tabela*/        
    public boolean escreve(int linhaDesjada, int colunaDesejada, List<String> conjunto) {
        tabela[linhaDesjada][colunaDesejada] = conjunto;
        return true;
    }

    /** Método para adicionar os estados na tabela para o AFNDY 
     *   @param int linhaDesjada - variável da linha 
     *   @param int colunaDesejada - variável da coluna
     *   @return tabela*/        
    public boolean add(int linhaDesjada, int colunaDesejada, List<String> conjunto) {
        listaTemporaria.addAll(tabela[linhaDesjada][colunaDesejada]);
        listaTemporaria.addAll(conjunto);
        tabela[linhaDesjada][colunaDesejada] = listaTemporaria;
        return true;
    }

    /** Método para adicionar os estados na lista a ser utilizada no AFD 
     *   @param List<String> lista
     *   @return boolean*/   
    public boolean addProximo(List<String> lista) {
        for(int i=0;i<numLinhas;i++){
            if(tabela[i][0].isEmpty()){
                tabela[i][0] = lista;
                return true;
            }
        }
        return false;
    }
    
     /** Método para adicionar a lista no AFD 
     *   @param List<String> lista
     *   @return boolean*/        
    public boolean addAFD(List<String> listaCheck,int j, List<String> listaPut) {
        for(int i=0;i<numLinhas;i++){
            if(tabela[i][0].equals(listaCheck)){
                tabela[i][j+1] = listaPut;
                return true;
            }
        }
        return false;
    }
    
    /** Método para converter string to Int */     
    public void stringToInt(){
        for(int i = 0;i<numLinhas;i++){
            for(int j = 0;j<numColuna;j++){
                for(int k=0; k< tabela[i][j].size();k++){
                    tabela[i][j].get(k).replace("q", "");
                }
            }
        }
    }
}
