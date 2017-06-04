/***************************************************************************
* 2014 - Carvalho, L. and Marcelino, C.                                    *
***************************************************************************/

#include<vector>
#include<cmath>

#include "functions.h"
#define M_PI 3.14159265358979323846

using namespace std;


double benchmark::rosenbrock(vector<double> x) {
    long double fit = pow( (1-x[0]) , 2 ) + 100 * pow( (x[1]-x[0]) , 2 );
    return (double) fit;
}



double benchmark::rastringin(vector<double> x) {
    double fit = 0.0;
    fit += 4 * pow(x[0], 2) - 10 * cos(2*M_PI*x[0]);
    fit += 9 * pow(x[1], 2) - 10 * cos(2*M_PI*x[1]);
    return fit;
}



double benchmark::schwefel(vector<double> x) {
    double fit = 418.9829 * x.size();
    double sum = 0.0;
    for(double xi : x)
        sum += xi*sin( sqrt( abs(xi) )  );
    return (fit - sum);
}


double benchmark::epsilon_ZDT2_1 (vector<double> x, double e2){
	vector<long double> f(2);
	unsigned long m = x.size();
	f[0] = x[0];

	double sum = 0;
	for(double xi : x)
		sum += xi;
	double g = 1+ (9/m-1)*sum;
    long double h = 1 - pow((f[0]/g), 2);

	f[1] = g * h;

	if(f[1] > e2) f[0] += 10*f[1];

	return (double) f[0];
}


double benchmark::epsilon_ZDT2_2 (vector<double> x, double e1){
	vector<long double> f(2);
	unsigned long m = x.size();
	f[0] = x[0];

	double sum = 0;
	for(double xi : x)
		sum += xi;
	double g = 1+ (9/m-1)*sum;
    long double h = 1 - pow((f[0]/g), 2);

	f[1] = g * h;

	if(f[0] > e1) f[0] += 10*f[0];

	return (double) f[0];
}



double benchmark::ackley(std::vector<double> x){
    long double sqrt_term = 0;
    long double osc_term = 0;

    for(double xi : x)
        sqrt_term += xi*xi;
    sqrt_term *=0.5;
    sqrt_term = -0.2*sqrt(sqrt_term);

    for(double xi : x)
        osc_term += cos(2*M_PI*xi);
    osc_term *=0.5;

    return (double) (-20.00*exp(sqrt_term) -exp(osc_term) + exp(1.0) + 20.00);
}
