package br.cefetmg.engcomp.ia.imunologico;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by gustavo on 5/26/17.
 */
public class Ponto implements Comparable, Comparator {

    private static List<Double> min_values;
    private static List<Double> max_values;
    private static long dimension;

    List<Double> x;
    double y;
    double fitness;
    Ponto original; // no caso de um clone


    Ponto(){
        x = new ArrayList<Double>((int) Ponto.dimension);
        for(int i=0; i<Ponto.dimension; i++)
            this.x.add(i, Ponto.nextDoubleForDimension(i));
        this.y = Double.NaN;
        this.fitness = Double.NaN;
        this.original = null;
    }

    // Gera um clone
    Ponto(Ponto p){
        x = new ArrayList<Double>();
        this.x.addAll(p.x);
        this.y = p.y;
        this.fitness = p.fitness;
        this.original = p;
    }


    static void setParameters(long dimension, List<Double> min_values, List<Double> max_values){
        Ponto.dimension = dimension;
        Ponto.min_values = min_values;
        Ponto.max_values = max_values;
    }

    static void setParameters(long dimension, double min, double max){
        List<Double> min_values = new ArrayList<Double>();
        List<Double> max_values = new ArrayList<Double>();
        for(int i=0; i<dimension; i++){
            min_values.add(i, min);
            max_values.add(i, max);
        }
        Ponto.setParameters(dimension, min_values, max_values);
    }

    private void trim() {
        for (int i = 0; i < Ponto.dimension; i++) {
            if (this.x.get(1) < Ponto.min_values.get(i))
                this.x.set(i, Ponto.min_values.get(i));
            if (this.x.get(1) > Ponto.max_values.get(i))
                this.x.set(i, Ponto.max_values.get(i));
        }
    }

    void mutacao(double taxa_mutacao){
        for(int i=0; i<this.x.size(); i++){
            //if(Ponto.nextDouble(0, 1) < taxa_mutacao) {
                double antigo = this.x.get(i);
                double min_limit = Ponto.min_values.get(i);
                double max_limit = Ponto.max_values.get(i);
                double variacao = (max_limit - min_limit) / 2;
                double limInferior = antigo - variacao * taxa_mutacao;
                double limSuperior = antigo + variacao * taxa_mutacao;
                if(limInferior < limSuperior)
                    this.x.set(i, Ponto.nextDouble(limInferior, limSuperior));
            //}
        }
        this.trim();
    }

    @Override
    public boolean equals(Object o){
        if (!(o.getClass() == Ponto.class))
            return false;
        for(int i=0; i<this.x.size(); i++)
            if(! ((Ponto)o).x.get(i).equals(this.x.get(i)))
                return false;
        return true;
    }

    @Override
    public int compareTo(Object p) {
        Ponto p2 = (Ponto) p;
        if(this.y < p2.y) return -1;
        if(this.y==p2.y) return 0;
        return 1;
    }

    @Override
    public int compare(Object p1, Object p2) {
        if(((Ponto)p1).y < ((Ponto)p2).y) return -1;
        if(((Ponto)p1).y == ((Ponto)p2).y) return 0;
        return 1;
    }

    public int compareToFitness(Object p) {
        Ponto p2 = (Ponto) p;
        if(this.fitness < p2.fitness) return -1;
        if(this.fitness==p2.fitness) return 0;
        return 1;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("(");
        for(double xi : this.x)
            str.append(String.format("%.3g", xi)).append(",");
        str = str.deleteCharAt(str.length()-1);
        str.append(") = ");
        str.append(String.valueOf(this.y));
        return str.toString();
    }


    private static Double nextDouble(double min, double max){
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    private static Double nextDoubleForDimension(int dim){
        return Ponto.nextDouble(Ponto.min_values.get(dim), Ponto.max_values.get(dim));
    }
}
