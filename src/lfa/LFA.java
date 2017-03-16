/*
 * LFA.java
 * Trabalho de Linguagens Formais e Autômatos
 * @author Thiago Almeida Martins Marques - 201120002
 * @author João Vitor Squillace Teixeira - 201120518
 * @author - Caio César Freitas Lara - 201310584
 */

package lfa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class LFA {

    private static final int AFNDL = 1;
    private static final int AFND = 0;
    private static final int AFD = 2;

    static String conteudo = "";

    static List<String> q = new ArrayList<>(); //lista que armazena os estados do automato
    static List<String> alphabet = new ArrayList<>(); //lista que armazena o alfabeto do automato
    static List<Transitions> transitions = new ArrayList<>(); //objetos transitions do tipo Transitions
    static List<String> initialState = new ArrayList<>(); //lista que armazena os estados iniciais
    static List<String> finalState = new ArrayList<>(); //lista que armazena os estados  finais
    static List<String> conjFechoEstado = new ArrayList<>(); //lista que armazena os estados do automato
    static HashSet<String> fechoTemporario = new HashSet<>(); //usado para armazenar temporariamente a construcao do fecho

    /**
     * Método para tratar as transições - formatação dos caracteres. De modo que
     * conseguisse salvar no formato da classe Transictions. Cada transição é um
     * objeto.
     *
     * @param String transicoes - transicões do autômato
     */
    public static void tratandoTransicoes(String transicoes) {
        List<String> temp = new ArrayList(Arrays.asList(transicoes.split("[(]|[)]->[{]|[}],|[}]")));
        temp.removeAll(Arrays.asList("", null));
        for (int i = 0; i < temp.size(); i = i + 2) {
            String[] esquerda = temp.get(i).split(",");
            List<String> direita = new ArrayList(Arrays.asList(temp.get(i + 1).split(",")));
            Transitions obj = new Transitions(esquerda[0], esquerda[1], direita);
            transitions.add(obj);
        }
    }

    /**
     * Método para percorrer as transições e retornar quais são os estados
     * destino (lambda). É utilizada pela função pegaFechoLambida e responsável
     * por retornar todos os destinos do estado que tem leitura lambda.
     *
     * @param String nomeEstado - nome do estado
     * #param String leitura - estado lido
     */
    public static List<String> funcaoElder(String nomeEstado, String leitura) {
        for (Transitions t : transitions) {
            if (t.estadoOrigem.equals(nomeEstado) && t.leitura.equals(leitura)) {
                return t.estadosDestinoList;
            }
        }
        return null;
    }

    /**
     * Método recursivo que irá criar os fechos lambdas gerados de cada estado
     * da tabela AFNDY. O fecho é gerado a partir dessa função recursiva onde
     * percorre toda tabela e pega os lamdas respectivos.
     *
     * @param HashSet<String> lambida - variável que contém o lambda da transição 
     */
    public static void pegaFechoLambida(HashSet<String> lambida) {

        for (String elementoString : lambida) {
            if (fechoTemporario.contains(elementoString)) {
                continue;
            }
            int elementoInt = Integer.parseInt(elementoString.replace("q", ""));
            fechoTemporario.add(elementoString);
            List<String> proxConjunto = funcaoElder(elementoString, ".");
            if (proxConjunto != null) {
                pegaFechoLambida(new HashSet<String>(proxConjunto));
            }
        }
    }

    /**
     * Método que irá criar o AFNDY. - Primeira tabela Esse método percorre
     * todos os estados verificando cada estado e sua leitura e a partir disto é
     * salvo todos os estados destino para a respectiva leitura
     *
     * @param Matrix matrix - variável do objeto Matrix para criação o AFNDY
     */
    public static Matrix criaAFNDY(Matrix matrix) {
        System.out.println("\nCriado o AFNDY com sucesso!\n");
        alphabet.add(".");
        ArrayList<String> fechos = new ArrayList<String>();
        for (int i = 0; i < q.size(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                for (int k = 0; k < transitions.size(); k++) {
                    if (transitions.get(k).estadoOrigem.equals(q.get(i)) && transitions.get(k).leitura.equals(alphabet.get(j))) {
                        if (matrix.tabela[i][j].isEmpty()) {
                            matrix.escreve(i, j, transitions.get(k).estadosDestinoList);
                        } else {
                            matrix.add(i, j, transitions.get(k).estadosDestinoList);
                        }
                    }
                }
            }
            HashSet<String> h = new HashSet<>();
            h.add("q" + i);
            pegaFechoLambida(h);
            //System.out.println("fecho final " + fechoTemporario);

            fechos.addAll(fechoTemporario);
            matrix.escreve(i, alphabet.size(), fechos);
            fechos = new ArrayList<>();
            fechoTemporario.clear();
        }
        alphabet.remove(".");
        return matrix;
    }

    /**
     * Método que irá inicializar o AF, abrindo o arquivo e a partir de
     * expressões regulares vai realizar a configuração inicial nas listas e
     * variáveis.
     *
     * @param String arquivoEntrada - String contendo o arquivo em formato .txt
     */
    public static void inicializandoAF(String arquivoEntrada) {
        try {
            Scanner in = new Scanner(new File(arquivoEntrada));
            while (in.hasNext()) {
                String tmpStr = in.nextLine();
                if (!tmpStr.equalsIgnoreCase("")) {
                    String replaceAll = tmpStr.replaceAll("\\s+", "");
                    conteudo += replaceAll;
                }
            }

            String[] res = conteudo.split("[(][{]|[}],[{]|[}][)]|[}][}],|,[{]");

            // switch responsável por salvar cada configuração na lista correta
            for (int i = 0; i < res.length; i++) {

                switch (i) {

                    case 1:
                        q = new ArrayList(Arrays.asList(res[i].split(",")));
                        break;
                    case 2:
                        alphabet = new ArrayList(Arrays.asList(res[i].split(",")));
                        break;
                    case 3:
                        tratandoTransicoes(res[i]);
                        break;
                    case 4:
                        initialState = new ArrayList(Arrays.asList(res[i].split(",")));
                        break;
                    case 5:
                        finalState = new ArrayList(Arrays.asList(res[i].split(",")));
                        break;

                }

                /*if (i == 1) {
                    q = new ArrayList(Arrays.asList(res[i].split(",")));
                } else if (i == 2) {
                    alphabet = new ArrayList(Arrays.asList(res[i].split(",")));
                } else if (i == 3) {
                    tratandoTransicoes(res[i]);
                } else if (i == 4) {
                    initialState = new ArrayList(Arrays.asList(res[i].split(",")));
                } else if (i == 5) {
                    finalState = new ArrayList(Arrays.asList(res[i].split(",")));
                }*/
            }
            in.close();

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }

    /**
     * Método que irá inicializar a matriz com o espaço exato para cada
     * autômato.
     *
     * @param int tipo - variável que irá definir o tamanho da tabela de acordo
     * com o tipo do autômato
     */
    public static Matrix inicializandoMatriz(int tipo) {
        Matrix tabela;
        switch (tipo) {
            case 0:
                tabela = new Matrix(q.size(), alphabet.size());
                break;
            case 1:
                tabela = new Matrix(q.size(), 2 + alphabet.size());
                break;
            default:
                tabela = new Matrix((int) Math.pow(2, q.size()), 1 + alphabet.size());
                break;
        }
        return tabela;
    }

    /**
     * Método que irá criar o AFND. Ele vai percorrer o fecho lambda de cada estado
     * após isso ele vai a cada estado procurar pelos seus elementos de acordo com o alfabeto
     * uma vez encotrado e vai procurar o fecho daquele elemento e criar uma lista com todos os fechos
     * por fim vai salvar na matriz AFND. 
     *
     * @param Matrix matrixAFNDL - objeto matriz correspondente ao AFNDL
     * @param Matrix matrixAFNDL - objeto matriz correspondente ao AFND
     */
    public static Matrix AFND(Matrix matrixAFNDL, Matrix matrixAFND) {
        System.out.println("\nCriado o AFDN com sucesso!\n");
        for (int linhaAFNDL = 0; linhaAFNDL < matrixAFNDL.numLinhas; linhaAFNDL++) {
            List<String> estadosDentroAlphabet = new ArrayList<>();
            conjFechoEstado = matrixAFNDL.tabela[linhaAFNDL][matrixAFNDL.numColuna - 1];
            for (String qestadoNoFecho : conjFechoEstado) {
                int estadoNoFecho = Integer.parseInt(qestadoNoFecho.replace("q", ""));
                for (int alphabetAtual = 0; alphabetAtual < alphabet.size(); alphabetAtual++) {
                    estadosDentroAlphabet = matrixAFNDL.tabela[estadoNoFecho][alphabetAtual];
                    List<String> conjEstadosPego = new ArrayList<>();
                    for (String qestadoDentroAlphabet : estadosDentroAlphabet) {
                        int estadoDentroAlphabet = Integer.parseInt(qestadoDentroAlphabet.replace("q", ""));
                        conjEstadosPego.addAll(matrixAFNDL.tabela[estadoDentroAlphabet][matrixAFNDL.numColuna - 1]);
                        HashSet<String> conjEstadosPegoHash = new HashSet<>(conjEstadosPego);
                        conjEstadosPego.clear();
                        conjEstadosPego.addAll(conjEstadosPegoHash);
                        HashSet<String> castTemporarioFechoLambda = new HashSet<>(matrixAFND.tabela[linhaAFNDL][alphabetAtual]);
                        castTemporarioFechoLambda.addAll(conjEstadosPego);
                        List<String> castLambdaLista = new ArrayList<>(castTemporarioFechoLambda);
                        matrixAFND.tabela[linhaAFNDL][alphabetAtual] = castLambdaLista;
                    }
                }
            }
        }
        return matrixAFND;
    }

    /**
     * Método que irá chamar a função recursivo para a criação da tabela AFD.
     *
     * @param Matrix matrixAFNDL - objeto matriz correspondente ao AFND
     * @param Matrix matrixAFND - objeto matriz correspondente ao AFND
     * @param Matrix matrixAFD - objeto matriz correspondente ao AFD
     */
    private static void AFD(Matrix matrixAFNDL, Matrix matrixAFND, Matrix matrixAFD) {
        System.out.println("\nCriado o AFD com sucesso!\n");
        matrixAFD.tabela[0][0] = matrixAFNDL.tabela[0][matrixAFNDL.numColuna - 1];
        recursivo(matrixAFD.tabela[0][0], matrixAFND, matrixAFD, -1);
    }

    /**
     * Método responsável por criar a Matriz AFD recursivamente.
     *
     * @param List<String> conjunto - Conjunto de estados 
     * @param Matrix matrixAFND -  objeto matriz correspondente ao AFND
     * @param Matrix matrixAFD -  objeto matriz correspondente ao AFD
     * @param int posLinhaTabelaAFD - 
     */
    private static void recursivo(List<String> conjunto, Matrix matrixAFND, Matrix matrixAFD, int posLinhaTabelaAFD) {
        posLinhaTabelaAFD++;
        HashSet<String> conjuntoEncontradoHash = new HashSet<>();
        for (int alphabetAtual = 0; alphabetAtual < alphabet.size(); alphabetAtual++) {
            for (String qestado : conjunto) {
                int estado = Integer.parseInt(qestado.replace("q", ""));
                conjuntoEncontradoHash.addAll(matrixAFND.tabela[estado][alphabetAtual]);
            }
            List<String> conjuntoEncontrado = new ArrayList(conjuntoEncontradoHash);
            if (!checkConjuntoEmAFD(conjuntoEncontrado, matrixAFD) && !conjuntoEncontrado.isEmpty()) {
                matrixAFD.addAFD(conjunto, alphabetAtual, conjuntoEncontrado);
                matrixAFD.addProximo(conjuntoEncontrado);
                recursivo(conjuntoEncontrado, matrixAFND, matrixAFD, posLinhaTabelaAFD);
            }
            matrixAFD.addAFD(conjunto, alphabetAtual, conjuntoEncontrado);
            conjuntoEncontradoHash.clear();
        }
    }

    /**
     * Método responsável por verificar se ja existe um conjunto na tabela AFD.
     * Pois se tiver não é necessário adicionar novamente.
     * @param List<String> conjunto de estados
     * @param Matrix matrixAFD -  objeto matriz correspondente ao AFD
     */
    private static boolean checkConjuntoEmAFD(List<String> conjunto, Matrix matrixAFD) {
        for (int linhaAFD = 0; linhaAFD < matrixAFD.numLinhas; linhaAFD++) {
            if (matrixAFD.tabela[linhaAFD][0].equals(conjunto)) {
                return true;
            }
        }
        return false;
    }

      /**
     * Método principal responsável por receber os argumentos e criar os 
     * automados AFNDL - AFND - AFD. Também responsável por gerar o arquivo de saída
     *
     * @param List<String> conjunto de estados
     * @param Matrix matrixAFD -  objeto matriz correspondente ao AFD
     */
    public static void main(String[] args) {
        List<String> arquivos = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            arquivos.add(args[i]);
        }

        File arquivo = new File(arquivos.get(1));
        try {
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            FileWriter gravarArquivo = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(gravarArquivo);

            // ******** cria os autômatos aqui  *********
            bw.write("Saída:");

            inicializandoAF(arquivos.get(0));

            //inicializa e cria a matriz AFNDL, e por fim grava no arquivo
            Matrix matrixAFNDL = inicializandoMatriz(AFNDL);
            matrixAFNDL = criaAFNDY(matrixAFNDL);

            //obtem a quantidade de estados para formatação da largura da tabela
            int quantidadeEstados = matrixAFNDL.getEstadosQuantidade();

            matrixAFNDL.exibeTabela(bw, alphabet, q, "AFNDL", quantidadeEstados);

            //inicializa e cria a matriz AFND, e por fim grava no arquivo
            Matrix matrixAFND = inicializandoMatriz(AFND);
            matrixAFND = AFND(matrixAFNDL, matrixAFND);
            matrixAFND.exibeTabela(bw, alphabet, q, "AFND", quantidadeEstados);

            //inicializa e cria a matriz AFD, e por fim grava no arquivo
            Matrix matrixAFD = inicializandoMatriz(AFD);
            AFD(matrixAFNDL, matrixAFND, matrixAFD);
            matrixAFD.exibeTabela(bw, alphabet, q, "AFD", quantidadeEstados);

            bw.close();
            gravarArquivo.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("erro");
        };

    }

}
