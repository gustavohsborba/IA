#include <vector>    // vector
#include <iostream>  // cout
#include <fstream>   // ofstream
#include <string>    // string
#include <sstream>   // stringstream

#include "genetic_algorithm.h"

using namespace std;

void register_in_file(FUNCTION func, vector <vector<particle> > population, int n_run){

    string filename;
    if(func == FUNCTION::RASTRINGIN){
        filename = "results/rastringin/rastringin_run_";
    } else if (func == FUNCTION::ROSENBROCK){
        filename = "results/rosenbrock/rosenbrock_run_";
    } else if (func == FUNCTION::SCHWEFEL){
        filename = "results/schwefel/schwefel_run_";
    } else if (func == FUNCTION::ACKLEY){
        filename = "results/ackley/ackley_run_";
    }


    vector< vector<double> > result (population.size());
    for(unsigned int i=0; i<result.size(); i++){
        result[i] = vector<double> (population[i].size());
        for(unsigned int j=0; j<result[i].size(); j++){
            particle p = population[i][j];
            result[i][j] = p.y;
        }
    }


    ostringstream run_number;
    run_number << n_run;
    filename += run_number.str();
    string filename2 = filename + "points.txt";
    filename += ".txt";

    ofstream outputFile(filename);
    ofstream outputFile2( (filename2) );
    if(!outputFile) {
         cerr << "Failure opening " << filename << endl;
    } else {
        for(unsigned int i=0; i<result.size(); i++){
            for(unsigned int j=0; j<result[i].size(); j++){
                outputFile << result[i][j] << " ";
                outputFile2 << "( " << population[i][j].x[0] <<
                              " , " << population[i][j].x[1] << ") = "
                              << population[i][j].y << endl;
            }
            outputFile << endl;
            outputFile2 << endl;
        }
    }


    //cout << "Run " << n_run << " saved into " << filename << endl;
}



void register_in_file_mean_best(FUNCTION func, vector<vector<double> > best, vector < vector<double> > mean){
    string filename;
    if(func == FUNCTION::RASTRINGIN){
        filename = "results/rastringin/rastringin_";
    } else if (func == FUNCTION::ROSENBROCK){
        filename = "results/rosenbrock/rosenbrock_";
    } else if (func == FUNCTION::SCHWEFEL){
        filename = "results/schwefel/schwefel_";
    } else if (func == FUNCTION::ACKLEY){
        filename = "results/ackley/ackley_";
    }

    string filenameBest = filename + "best.txt";
    string filenameMean = filename + "mean.txt";

    ofstream outputFileBest(filenameBest);
    ofstream outputFileMean(filenameMean);
    for(unsigned int i=0; i<best.size(); i++){
        for(unsigned int j=0; j<best[i].size(); j++){
            outputFileBest << best[i][j] << " ";
            outputFileMean << mean[i][j] << " ";
        }
        outputFileBest << endl;
        outputFileMean << endl;
    }
    //cout << "Mean and Best saved into " << filenameMean << " and " << filenameBest << endl;

}


void register_population(){

}



int main()
{
    evolutionary_algorithm_parameters param;


    // INITIALIZE parameters for FUNCTION:
    param.setDimension(2);
    param.pop_size = 50;
    param.max_generations = 50;
    param.mutation_rate = 0.00;

    param.min_values.x[0] = -5;
    param.max_values.x[0] = 5;
    param.min_values.x[1] = -5;
    param.max_values.x[1] = 5;
    param.func = FUNCTION::ACKLEY;

    // GENERATE INITIAL POPULATION:
    genetic_algorithm genetic (param);
    vector<particle> initial_population = genetic.generate_population();


    // GENERATE RESULT VECTORS:
    vector<particle> population;
    vector<double> memGBest;
    vector<double> memGMean;
    vector< vector<double> > vecMemGBest, vecMemGMean;
    vector< vector<particle> > result;
    particle bestOfAll;

    genetic.e2 = 10;
    for(int i=1; i<=30; i++) {

        genetic.e2 -= (9/30);

        // RESTART GENETIC ALGORITHM:
        genetic = genetic_algorithm (param);

        // COPY INITIAL POPULATION:
        population.clear();
        for(particle p : initial_population)
            population.push_back(p);

        // RUN GA for FUNCTION 3:
        result.clear();
        memGBest.clear();
        memGMean.clear();
        while(genetic.n_generations < param.max_generations){
            population = genetic.run_generation(population);

            result.push_back(population);
            memGBest.push_back(population.at(0).y);
            double mean = 0.0;
            for(particle p:population)
                mean += p.y;
            memGMean.push_back( (mean/param.pop_size) );
        }

        particle best = population.at(0);
        string bestItem = "(";
        for(double xi : best.x)
            bestItem += to_string(xi);
        printf("Run %d: Melhor Individuo encontrado: (%e, %e) = %e\n", i, best.x[0], best.x[1], best.y);
        if (i==1)
            bestOfAll = best;
        if(bestOfAll.y > best.y)
            bestOfAll = best;

        vecMemGBest.push_back(memGBest);
        vecMemGMean.push_back(memGMean);

        // KEEP REGISTER of CURRENT EXECUTION:
        register_in_file(param.func, result, i);
    }
    printf("Melhor Individuo geral: (%e, %e) = %e\n", bestOfAll.x[0], bestOfAll.x[1], bestOfAll.y);

    register_in_file_mean_best(param.func, vecMemGBest, vecMemGMean);

}
