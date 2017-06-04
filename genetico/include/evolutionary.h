#ifndef EVO_H_INCLUDED
#define EVO_H_INCLUDED


#include<vector>


enum FUNCTION {
    RASTRINGIN,
    ROSENBROCK,
    SCHWEFEL,
	ACKLEY,
	E_ZDT2_1,
	E_ZDT2_2
};



/**
 * @class particle
 * @var x : a point in an N-Dimensional space
 * @var y : It's f(x) value, in a function of N vars
 * @var fitness : value between 0 and 1 representing the
 * relative fitness among others particles
 */

class particle {
    public:
        std::vector<double> x;
        double y;
        double fitness;
        double accumulated_fitness;

        particle(long dimensions);
        particle();
        static bool compare(particle p1, particle p2);
};


/**
 * @class evolutionary_algorithm_parameters
 * @var dimension :
 * @var pop_size :
 * @var max_generations :
 * @var max_function_eval :
 * @var mutation_rate :
 * @var FUNCTION :
 */

class evolutionary_algorithm_parameters {
    public:
        long dimension;
        long pop_size;
        long max_generations;
        long max_function_eval;
        double mutation_rate;
		double crossover_rate;
        FUNCTION func;
        particle min_values;
        particle max_values;
        bool minimize;

        void setDimension(long d);

};


#endif // EVO_H_INCLUDED
