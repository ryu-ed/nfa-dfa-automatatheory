/*
 * Transitions.java
 * Trabalho de Linguagens Formais e Autômatos
 * @author Thiago Almeida Martins Marques - 201120002
 * @author João Vitor Squillace Teixeira - 201120518
 * @author - Caio César Freitas Lara - 201310584
 */

package lfa;

import java.util.ArrayList;
import java.util.List;


public class Transitions {
    String estadoOrigem = ""; //estado de origem da transicao
    String leitura = ""; //o que o estado lê
    List<String> estadosDestinoList = new ArrayList<String>(); //estados para onde o estado de origem vai caso ele lê leitura
    
    public Transitions(String estadoOrigem,String leitura,List<String> estadosDestinoList){
        this.estadoOrigem = estadoOrigem;
        this.leitura = leitura;
        this.estadosDestinoList = estadosDestinoList;
    }
    
    public void print(){
        System.out.println(estadoOrigem);
        System.out.println(leitura);
        System.out.println(estadosDestinoList);
    }
}
