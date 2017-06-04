/***************************************************************************
* 2015 - BORBA, Gustavo H.S                                                *
* Genetic Algorithm Implementation                                         *
***************************************************************************/

#include<cmath>
#include<vector>

#include "genetic_algorithm.h"


/**
 *
 */

particle::particle(){
    x = std::vector<double> (1);
    x[0] = NAN;
    y = NAN;
    fitness = NAN;
    accumulated_fitness = NAN;
}

particle::particle(long dimensions){
    x = std::vector<double> (dimensions);
    for(unsigned int i=0; i<x.size(); i++)
        x[i] = NAN;
    y = NAN;
    fitness = NAN;
    accumulated_fitness = NAN;
}

bool particle::compare(particle p1, particle p2){
    if(p1.y < p2.y) return true;
    else return false;
}



void evolutionary_algorithm_parameters::setDimension(long d){
    dimension = d;
    min_values = particle(d);
    max_values = particle(d);
}
