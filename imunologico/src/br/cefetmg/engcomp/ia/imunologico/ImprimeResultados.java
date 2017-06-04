package br.cefetmg.engcomp.ia.imunologico;
import br.cefetmg.engcomp.ia.imunologico.FitnessFunction.FUNCTION;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 6/3/17.
 */
public class ImprimeResultados {

    private FUNCTION f;
    private String funcname;
    private String run_filename;
    private String plot_filename;
    private String best_filename;
    private String mean_filename;

    private List<Double> valoresMedios = new ArrayList<Double>();
    private List<Double> melhoresValores = new ArrayList<Double>();
    private List<List<Double>> registroMedios = new ArrayList<List<Double>>();
    private List<List<Double>> registroMelhores = new ArrayList<List<Double>>();


    private Ponto melhorPonto;


    ImprimeResultados(FUNCTION f){
        this.f = f;
        melhorPonto = new Ponto();
        melhorPonto.y = Double.MAX_VALUE;
        switch (f) {
            case ROSENBROCK:
                funcname = "Rosenbrock";
                run_filename = "ROSENBROCK_run";
                best_filename = "ROSENBROCK_best.txt";
                mean_filename = "ROSENBROCK_mean.txt";
                plot_filename = "ROSENBROCK_plot";
                break;
            case RASTRINGIN:
                funcname = "Rastringin";
                run_filename = "RASTRINGIN_run";
                best_filename = "RASTRINGIN_best.txt";
                mean_filename = "RASTRINGIN_mean.txt";
                plot_filename = "RASTRINGIN_plot";
                break;
            case ACKLEY:
                funcname = "Akley";
                run_filename = "ACKLEY_run";
                best_filename = "ACKLEY_best.txt";
                mean_filename = "ACKLEY_mean.txt";
                plot_filename = "ACKLEY_plot";
                break;
            case SHWEFEL:
                funcname = "Shwefel";
                run_filename = "SHWEFEL_run";
                best_filename = "SHWEFEL_best.txt";
                mean_filename = "SHWEFEL_mean.txt";
                plot_filename = "SHWEFEL_plot";
                break;
            default:
                throw new UnsupportedOperationException("Função ainda não implementada no filename do Result Printer");
        }
    }


    public void salvaExecucaoAtual(int execno){
        StringBuilder strMean = new StringBuilder();
        String filename = run_filename + "_" + execno + ".txt";
        try (PrintStream out_run = new PrintStream(new FileOutputStream(filename))) {
            for(Double d : valoresMedios) {
                strMean.append(String.valueOf(d)).append(" ");
            }
            out_run.println(strMean);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void salvaMediasEMelhores(){
        try {
            PrintStream out_best = new PrintStream(new FileOutputStream(best_filename));
            try {
                StringBuilder strBest = new StringBuilder();
                for (List<Double> arr : registroMelhores) {
                    for (Double d : arr) {
                        strBest.append(String.valueOf(d)).append(" ");
                    }
                    strBest.append("\n");
                }
                out_best.print(strBest.toString());
            } finally {
                out_best.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintStream out_mean = new PrintStream(new FileOutputStream(mean_filename))) {
            StringBuilder strMean = new StringBuilder();
            for(List<Double> arr : registroMedios) {
                for(Double d : arr) {
                    strMean.append(String.valueOf(d)).append(" ");
                }
                strMean.append("\n");
            }
            out_mean.print(strMean.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void acumulaValores(List<Ponto> pontos){
        List<Double> valores = new ArrayList<Double>();
        double mediaPopulacao=0.0;
        for(Ponto p:pontos) {
            mediaPopulacao += p.y;
            valores.add(p.y);
        }
        valoresMedios.add( mediaPopulacao / (double) pontos.size());
        melhoresValores.add(pontos.get(0).y);
        Ponto melhor = pontos.get(pontos.size()-1);
        if(melhorPonto.y > melhor.y)
            melhorPonto = melhor;
    }

    public void preparaNovaExecucao(){
        registroMedios.add(valoresMedios);
        registroMelhores.add(melhoresValores);
        valoresMedios = new ArrayList<Double>();
        melhoresValores = new ArrayList<Double>();
    }

    public void imprimeExecucaoAtual(List<Ponto> pontos, int nExecucao){
        Ponto melhor = pontos.get(0);
        Ponto pior = pontos.get(pontos.size()-1);
        double mediaPopulacao=0.0;
        for(Ponto p:pontos) {
            mediaPopulacao += p.y;
        }
        mediaPopulacao = mediaPopulacao / (double) pontos.size();
        System.out.printf("\n================= Execucao %d ===================\n", nExecucao);
        System.out.printf("Função: %s\n", funcname);
        System.out.printf("Quantidade de anticorpos: %s\n", pontos.size());
        System.out.printf("Melhor Anticorpo: %s\n", melhor.toString());
        System.out.printf("Média da população: %s\n", String.format("%.3g",mediaPopulacao));
        System.out.printf("Pior Anticorpo: %s\n", pior.toString());
    }


}
