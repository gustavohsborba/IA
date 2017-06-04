#ifndef GENETIC_ALGORITHM_H_INCLUDED
#define GENETIC_ALGORITHM_H_INCLUDED

#include <random>
#include "evolutionary.h"

/**
 * @class genetic_algorithm
 *
 */

class genetic_algorithm {

    public:
        genetic_algorithm(evolutionary_algorithm_parameters param);

        long n_generations;

        std::vector<particle> generate_population();
        std::vector<particle> run_generation(std::vector<particle> population);

        std::vector<particle> evaluate(std::vector<particle> population);
        std::vector<particle> fitness(std::vector<particle> population);
        std::vector<particle> recombination(std::vector<particle> population);
        particle roulette_selection(std::vector<particle> population);
        particle mutation(particle par);
        double e1, e2;

    private:
        evolutionary_algorithm_parameters param;
        //std::vector<particle> population;

        std::mt19937 twistergenerator;
        std::vector < std::uniform_real_distribution <double> > urand;

        std::vector<particle> crossover(std::vector<particle> parents);
        std::vector<particle> convex_crossover(std::vector<particle> parents);
        std::vector<particle> simple_crossover(std::vector<particle> parents);
        particle random_selection(std::vector<particle> population);
        std::vector<double> linear_combination_weights(long n);
};




#endif // GENETIC_ALGORITHM_H_INCLUDED
