package br.cefetmg.engcomp.ia.imunologico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gustavo on 5/26/17.
 */
public class SelecaoClonal {

    private long max_it;
    private long pop_size;
    private double ro = 5;
    private double beta = 0.1;
    private FitnessFunction f;

    private List<Ponto> anticorpos;

    public SelecaoClonal(FitnessFunction f, int max_it, int pop_size){
        this.f = f;
        this.max_it = max_it;
        this.pop_size = pop_size;
        this.anticorpos = geraAnticorpos();
    }


    // Inicializa a população:
    public List<Ponto> geraAnticorpos() {
        List<Ponto> pop = new ArrayList<Ponto>();
        for(int i=0; i<pop_size; i++)
            pop.add(new Ponto());
        return pop;
    }


    // Avalia a galera com o antígeno
    public List<Ponto> avaliacao(List<Ponto> populacao) {
        for(int i=0; i<populacao.size(); i++)
            populacao.get(i).y = f.eval(populacao.get(i).x);
        return populacao;
    }

    // Determina a afinidade da galera com o antígeno
    public List<Ponto> afinidade(List<Ponto> populacao) {
        populacao.sort(Ponto::compareTo);
        double max_val = populacao.get(populacao.size()-1).y;
        double min_val = populacao.get(0).y;
        for(int i=0; i<populacao.size(); i++)
            populacao.get(i).fitness = (-1 / (max_val-min_val))*populacao.get(i).y + (max_val/(max_val-min_val));
        return populacao;
    }

    // Seleciona os n anticorpos com a maior afinidade
    public List<Ponto> selecao(List<Ponto> populacao, int n) {
        populacao.sort(Ponto::compareTo);
        n = Math.min(n, populacao.size());
        return populacao.subList(0, n);
    }

    // Clona os anticorpos de forma proporcional à afinidade
    public List<Ponto> clonagemProporcional(List<Ponto> pop) {
        List<Ponto> clones = new ArrayList<Ponto>();
        pop.sort(Ponto::compareTo);
        for(int i=0; i<pop.size(); ++i){
            int nclones = (int) Math.round((pop.size()*beta)/(i+1));
            for(int j=0; j<nclones; j++)
                clones.add(new Ponto(pop.get(i)));
        }
        return clones;
    }

    // Realiza a mutação da população, inversamente proporcional à afinidade
    public List<Ponto> mutacao(List<Ponto> populacao) {
        for(int i=0; i<populacao.size(); i++)
            populacao.get(i).mutacao(Math.exp(-ro*populacao.get(i).fitness));
        return populacao;
    }

    // Repõe os n piores pontos pelos melhores clones.
    public List<Ponto> reposicao(List<Ponto> populacao, List<Ponto> clones, int n){
        populacao.sort(Ponto::compareTo);
        clones.sort(Ponto::compareTo);
        int nMelhores = Math.min(n, clones.size()-1);
        int tamanhoMenosEne = Math.max(populacao.size()-1-nMelhores, 0);
        int eneMenosUm = Math.max(populacao.size()-1, 0);
        List<Ponto> melhores = clones.subList(0, nMelhores);
        List<Ponto> piores = populacao.subList(tamanhoMenosEne, eneMenosUm);
        populacao.removeAll(piores);
        populacao.addAll(melhores);
        for(int i=0; i<populacao.size(); i++)
            populacao.get(i).original = null;
        return populacao;
    }

    // Repõe os n piores pontos pelos melhores clones.
    public List<Ponto> reposicaoInteligente(List<Ponto> populacao, List<Ponto> clones, int n){
        populacao.sort(Ponto::compareTo);
        clones.sort(Collections.reverseOrder(Ponto::compareTo));
        for(int i=0; i<populacao.size(); i++){
            for(Ponto c : clones)
                if(c.original==populacao.get(i) && c.y < c.original.y)
                    populacao.set(i, c);
        }
        for(int i=0; i<populacao.size(); i++)
            populacao.get(i).original = null;
        return populacao;
    }


    public List<Ponto> CLONALG_OPT(int max_it, int n1, int n2){

        anticorpos = geraAnticorpos();// Inicializa População
        for(int t = 1; t <= max_it; t++){
            anticorpos = avaliacao(anticorpos);
            anticorpos = afinidade(anticorpos);
            List<Ponto> P1 = selecao(anticorpos, n1);
            List<Ponto> clones = clonagemProporcional(P1);
            clones = mutacao(clones);
            clones = avaliacao(clones);
            clones = afinidade(clones);
            P1 = selecao(clones, n1);
            anticorpos = reposicaoInteligente(anticorpos, P1, n2);
        }

        return anticorpos;
    }

    public List<Ponto> OT_CLONAL_ITERACAO(int n1, int n2){
        anticorpos = avaliacao(anticorpos);
        anticorpos = afinidade(anticorpos);
        List<Ponto> P1 = selecao(anticorpos, n1);
        List<Ponto> clones = clonagemProporcional(P1);
        clones = mutacao(clones);
        clones = avaliacao(clones);
        clones = afinidade(clones);
        P1 = selecao(clones, n1);
        anticorpos = reposicao(anticorpos, P1, n2);
        anticorpos.sort(Ponto::compareTo);
        return anticorpos;
    }




    public List<Ponto> getAnticorpos() {
        return anticorpos;
    }

    public void setAnticorpos(List<Ponto> anticorpos) {
        this.anticorpos = anticorpos;
    }
}
