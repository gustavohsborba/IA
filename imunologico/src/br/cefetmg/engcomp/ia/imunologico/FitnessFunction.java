package br.cefetmg.engcomp.ia.imunologico;

import java.util.List;

/**
 * Created by gustavo on 5/26/17.
 */

class FitnessFunction {

    public enum FUNCTION { ROSENBROCK, ACKLEY, RASTRINGIN, SHWEFEL, EPSILON_ZDT2_1, EPSILON_ZDT2_2}
    private FUNCTION f;
    private double epsilon;

    // CONSTRUCTORS

    FitnessFunction(FUNCTION f, double epsilon){
        this.f = f;
        this.epsilon = epsilon;
    }

    FitnessFunction(FUNCTION f){
        this(f, 0.01);
    }


    // ACTUALLY EVAL FITNESS FUNCTION

    double eval(List<Double> x){
        switch (f){
            case ROSENBROCK:
                return rosenbrock(x);
            case RASTRINGIN:
                return rastringin(x);
            case ACKLEY:
                return ackley(x);
            case SHWEFEL:
                return schwefel(x);
            case EPSILON_ZDT2_1:
                return epsilon_ZDT2_1(x, epsilon);
            case EPSILON_ZDT2_2:
                return epsilon_ZDT2_2(x, epsilon);
            default:
                throw new UnsupportedOperationException("Função de testes não definida!");
        }
    }



    // --------- FITNESS FUNCTIONS IMPLEMENTATION


    // Suitable only for 2 dimensions
    private double rosenbrock(List<Double> x) {
        if(x.size()!= 2) throw new UnsupportedOperationException("Dimensão não suportada para função Rosenbrock!");
        double fit = Math.pow( (1-x.get(0)) , 2 ) + 100 * Math.pow( (x.get(1)-x.get(0)) , 2 );
        return fit;
    }

    private double rastringin(List<Double> x) {
        if(x.size()!= 2) throw new UnsupportedOperationException("Dimensão não suportada para função Rastringin!");
        double fit = 0.0;
        fit += 4 * Math.pow(x.get(0), 2) - 10 * Math.cos(2*Math.PI*x.get(0));
        fit += 9 * Math.pow(x.get(1), 2) - 10 * Math.cos(2*Math.PI*x.get(1));
        return fit;
    }

    private double schwefel(List<Double> x) {
        double fit = 418.9829 * x.size();
        double sum = 0.0;
        for(double xi : x)
            sum += xi*Math.sin( Math.sqrt( Math.abs(xi) )  );
        return (fit - sum);
    }

    private double epsilon_ZDT2_1 (List<Double> x, double e2){
        double[] f = new double[2];
        long m = x.size();
        f[0] = x.get(0);

        double sum = 0;
        for(double xi : x)
            sum += xi;
        double g = 1+ (9/m-1)*sum;
        double h = 1 - Math.pow((f[0]/g), 2);

        f[1] = g * h;

        if(f[1] > e2) f[0] += 10*f[1];

        return f[0];
    }

    private double epsilon_ZDT2_2 (List<Double> x, double e1){
        double[] f = new double[2];
        long m = x.size();
        f[0] = x.get(0);

        double sum = 0;
        for(double xi : x)
            sum += xi;
        double g = 1+ (9/m-1)*sum;
        double h = 1 - Math.pow((f[0]/g), 2);

        f[1] = g * h;

        if(f[0] > e1) f[0] += 10*f[0];

        return f[0];
    }

    private double ackley(List<Double> x){
        double sqrt_term = 0;
        double osc_term = 0;

        for(double xi : x)
            sqrt_term += xi*xi;
        sqrt_term *=0.5;
        sqrt_term = -0.2*Math.sqrt(sqrt_term);

        for(double xi : x)
            osc_term += Math.cos(2*Math.PI*xi);
        osc_term *=0.5;

        return (-20.00*Math.exp(sqrt_term) -Math.exp(osc_term) + Math.exp(1.0) + 20.00);
    }



}
