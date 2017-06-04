#ifndef FUNCTIONS_H_INCLUDED
#define FUNCTIONS_H_INCLUDED


namespace benchmark {
    double rosenbrock(std::vector<double> x);
    double rastringin(std::vector<double> x);
    double schwefel(std::vector<double> x);
    double ackley(std::vector<double> x);
    double epsilon_ZDT2_1 (std::vector<double> x, double e2);
    double epsilon_ZDT2_2 (std::vector<double> x, double e1);
}



#endif // FUNCTIONS_H_INCLUDED
