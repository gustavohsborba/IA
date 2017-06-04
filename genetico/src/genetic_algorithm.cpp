/***************************************************************************
* 2015 - BORBA, Gustavo H.S.                                               *
* Genetic Algorithm Implementation                                         *
***************************************************************************/

#include<algorithm>
#include <chrono>
#include <stdlib.h>
#include "genetic_algorithm.h"
#include "functions.h"

#include <iostream> // debug

using namespace std;


mt19937 gerador = mt19937(seed);

uniform_real_distribution urand = uniform_real_distribution<double>(0,1);

double valor = urand(gerador);



/**
 * FUNCTION genetic_algorithm: Constructor of GE class.
 * INSTANTIATES a genetic_algorithm object.
 * @param parameters: Parameters of the Genetic Algorithm previously set.
 */
genetic_algorithm::genetic_algorithm(evolutionary_algorithm_parameters parameters){
    param = parameters;

    auto start = chrono::high_resolution_clock::now().time_since_epoch();
    unsigned long seed = (unsigned long) start.count();
    srand(seed);

    twistergenerator = mt19937(seed);
    urand = vector<uniform_real_distribution <double> > (param.dimension);
    for(int i=0; i<param.dimension; i++){
        urand[i] = uniform_real_distribution <double> (param.min_values.x[i], param.max_values.x[i]);
    }

    n_generations = 0;
}




vector<particle> genetic_algorithm::generate_population(){
    vector<particle> population (param.pop_size);

    for(unsigned int i=0; i<population.size(); i++){
        population[i] = particle(param.dimension);
        for(long j=0; j<param.dimension; j++)
            population[i].x[j] = urand[j](twistergenerator);
    }

    return population;
}




vector<particle> genetic_algorithm::evaluate( vector<particle> population ) {

    if(param.func == FUNCTION::ROSENBROCK){
        for(unsigned int i=0; i<population.size(); i++)
            population[i].y = benchmark::rosenbrock(population[i].x);
    }
    else if (param.func == FUNCTION::RASTRINGIN) {
        for(unsigned int i=0; i<population.size(); i++)
            population[i].y = benchmark::rastringin(population[i].x);
    }
    else if (param.func == FUNCTION::SCHWEFEL) {
        for (unsigned int i = 0; i < population.size(); i++)
            population[i].y = benchmark::schwefel(population[i].x);
    }
    else if (param.func == FUNCTION::ACKLEY) {
        for(unsigned int i=0; i<population.size(); i++)
            population[i].y = benchmark::ackley(population[i].x);
    } else if (param.func == FUNCTION::E_ZDT2_1) {
        for(unsigned int i=0; i<population.size(); i++)
            population[i].y = benchmark::epsilon_ZDT2_1(population[i].x, e2);
    } else if (param.func == FUNCTION::E_ZDT2_2) {
        for(unsigned int i=0; i<population.size(); i++)
            population[i].y = benchmark::epsilon_ZDT2_2(population[i].x, e1);
    }

    return population;
}





vector<particle> genetic_algorithm::fitness(vector<pa   rticle> population) {
    sort(population.begin(), population.end(), particle::compare);
    double max_value = population[population.size()-1].y;
    double min_value = population[0].y;

    // Equacao da reta:
    for(unsigned int i=0; i<population.size(); i++)
        population[i].fitness = (-1 / (max_value-min_value))*population[i].y + (max_value/(max_value-min_value));

    return population;
}


/// Random selection of an individual among the entire population:
particle genetic_algorithm::random_selection(vector<particle> population) {
    uniform_int_distribution <int> randu(0, population.size()-1);
    int randselection = randu(twistergenerator);
    return population[randselection];
}


/// Roulette implementation:
particle genetic_algorithm::roulette_selection(vector<particle> population) {
    particle par = population[population.size()-1];
    double accumulated_fitness = 0.0;
    for(unsigned int i=0; i<population.size(); i++) {
        accumulated_fitness += population[i].fitness;
        population[i].accumulated_fitness = accumulated_fitness;
    }
    uniform_real_distribution <double> randu(0, accumulated_fitness);
    double randselection = randu(twistergenerator);

    for(particle p : population)
        if(randselection <= p.accumulated_fitness)
            return p;

    return par;
}


vector<double> genetic_algorithm::linear_combination_weights(long n){
    uniform_real_distribution <double> randu(0, 1);
    normal_distribution <double> randn(0, 1);

    vector<double> weights(n);
    double weights_sum = 0.0;
    double normalization = 0.0;
    for(int i=0; i<n; i++){
        weights[i] = randu(twistergenerator);
        weights_sum += weights[i];
    }
    for(int i=0; i<n-1; i++){
        weights[i] = weights[i]/weights_sum;
        normalization = weights[i];
    }
    weights[n-1] = 1-normalization;

    return weights;
}


vector<particle> genetic_algorithm::convex_crossover(vector<particle> parents){
    uniform_real_distribution <double> randu(0, 1);
    normal_distribution <double> randn(0, 1);
    double extrapolation = 0.01*randn(twistergenerator);

    vector<particle> offspring;
    particle p1 (param.dimension);
    particle p2 (param.dimension);
    for(int i=0; i<param.dimension; i++){
        double linear_comb = randu(twistergenerator) + extrapolation;
        p1.x[i] = linear_comb*parents.at(0).x[i] + (1-linear_comb)*parents.at(1).x[i];
        p2.x[i] = linear_comb*parents.at(1).x[i] + (1-linear_comb)*parents.at(0).x[i];
    }

    for(int i=0; i<param.dimension; i++){
        if(p1.x[i] < param.min_values.x[i])
            p1.x[i] = param.min_values.x[i];
        if(p1.x[i] > param.max_values.x[i])
            p1.x[i] = param.max_values.x[i];
        if(p2.x[i] < param.min_values.x[i])
            p2.x[i] = param.min_values.x[i];
        if(p2.x[i] > param.max_values.x[i])
            p2.x[i] = param.max_values.x[i];
    }


    offspring.push_back(p1);
    offspring.push_back(p2);
    return offspring;
}




vector<particle> genetic_algorithm::crossover(vector<particle> parents){

    vector<particle> offspring (parents.size());
    for(unsigned int j=0; j<offspring.size(); j++){
        offspring[j].x = vector<double> (param.dimension);
        for(unsigned int i = 0; i<offspring[j].x.size(); i++ ){
            vector<double> lin_comb = linear_combination_weights(parents.size());
            offspring[j].x[i] = 0.0;
            for(particle p : parents)
                offspring[j].x[i] += lin_comb[i] * p.x[i];
            for(int i=0; i<param.dimension; i++){
                if(offspring[j].x[i] < param.min_values.x[i])
                    offspring[j].x[i] = param.min_values.x[i];
                if(offspring[j].x[i] > param.max_values.x[i])
                    offspring[j].x[i] = param.max_values.x[i];
            }
        }
    }

    return offspring;
}


particle genetic_algorithm::mutation(particle par){
    normal_distribution <double> randn(0, 1);
    uniform_real_distribution <double> randu(0, 1);
    for(unsigned int i =0; i<par.x.size(); i++) {
        if( param.mutation_rate >= randu(twistergenerator) )
            par.x[i] = par.x[i]
                   + param.mutation_rate*randn(twistergenerator)
                     * (param.max_values.x[i] - param.min_values.x[i]);
        if(par.x[i] < param.min_values.x[i])
            par.x[i] = param.min_values.x[i];
        if(par.x[i] > param.max_values.x[i])
            par.x[i] = param.max_values.x[i];
    }
    return par;
}




vector<particle> genetic_algorithm::recombination(vector<particle> population){

    vector<particle> parents;
    vector<particle> children;
    vector<particle> offspring;

    // for each 2 parents, generate 2 children:
    while(offspring.size() < param.pop_size){
        parents.push_back( random_selection(population) );
        parents.push_back( random_selection(population) );
        children = convex_crossover(parents);
        offspring.insert(offspring.end(), children.begin(), children.end());
        parents.clear();
    }

    // Even if the offspring exceed, returns exactly a population of initial size:
    vector<particle> ret(offspring.begin(), offspring.begin()+param.pop_size);
    return ret;
}



vector<particle> genetic_algorithm::run_generation(vector<particle> population){

    // EVALUATE CURRENT GENERATION:
    population = evaluate(population);
    population = fitness(population);


    // CREATE an OFFSPRING GENERATION from CURRENT GENERATION:
    vector<particle> offspring = recombination(population);


    // EVALUATE OFFSPRING GENERATION:
    offspring = evaluate(offspring);
    particle best = population[0];


    // FITNESS BOTH GENERATIONS:
    population.insert(population.end(), offspring.begin(), offspring.end());
    population = fitness(offspring);

    // SELECT NEW GENERATION:
    particle best2 = population[0];
    vector<particle> selection;
    selection.clear();
    selection.push_back(best);
    selection.push_back(best2);
    for(int i=0; i<param.pop_size-2; i++)
        selection.push_back(roulette_selection(population));
    population = selection;


    // MUTATE NEW GENERATION:
    for(unsigned int i=1; i<population.size(); i++)
        population[i] = mutation(population[i]);

    // UPDATE generation counter:
    n_generations++;

    return population;
}


