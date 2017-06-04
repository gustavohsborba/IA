package br.cefetmg.engcomp.ia.imunologico;
import br.cefetmg.engcomp.ia.imunologico.FitnessFunction.FUNCTION;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static FUNCTION funcao = FUNCTION.ACKLEY;
    private static FitnessFunction f = new FitnessFunction(funcao);
    private static int QTD_EXEC = 30;  // PARA CALCULAR OS VALORES MÉDIOS.

    private static int MAX_IT   = 50;  // QUANTIDADE DE ITERAÇÕES
    private static int POP_SIZE = 100; // TAMANHO DA POPULAÇÃO

    private static int DIMENSAO = 2;   // TAMANHO DO VETOR X
    private static int VAL_MIN = -5;   // VALORES MÍNIMO E MÁXIMO PODEM SER
    private static int VAL_MAX = 5;    // GERAIS (DOUBLE) OU RELATIVOS ÀS DIMENSÕES DE X

    private static int N1 = 100;       // QUANTIDADE DE ANTICORPOS SELECIONADOS
    private static int N2 = 100;       // QUANTIDADE DE CLONES TROCADOS
                                       // BETA E RO SÃO PARÂMETROS INTERNOS DE SelecaoClonal

    public static void main(String[] args) {

        // Inicializa parâmetros:
        Ponto.setParameters(DIMENSAO, VAL_MIN, VAL_MAX);
        SelecaoClonal selecaoClonal = new SelecaoClonal(f, MAX_IT, POP_SIZE);
        ImprimeResultados imprimeResultados = new ImprimeResultados(funcao);

        // Para que as funções utilizem o mesmo set inicial de anticorpos
        List<Ponto> pop_inicial = selecaoClonal.geraAnticorpos();
        List<Ponto> anticorpos = new ArrayList<Ponto>();
        anticorpos.addAll(pop_inicial);

        // Executa 30 vezes
        for(int i=1; i<=QTD_EXEC; i++) {
            selecaoClonal.setAnticorpos(new ArrayList<Ponto>());
            selecaoClonal.getAnticorpos().addAll(pop_inicial);
            for (int t = 1; t <= MAX_IT; t++) {
                anticorpos = selecaoClonal.OT_CLONAL_ITERACAO(N1, N2);
                imprimeResultados.acumulaValores(anticorpos);
            }
            imprimeResultados.imprimeExecucaoAtual(anticorpos, i);
            //imprimeResultados.salvaExecucaoAtual(i);
            imprimeResultados.preparaNovaExecucao();
        }
        imprimeResultados.salvaMediasEMelhores();

    }
}
