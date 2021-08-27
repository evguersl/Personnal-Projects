#ifndef _________FONCTIONS____
#define _________FONCTIONS____


#include <iostream>
#include <vector>
#include <random>

typedef unsigned char BOOL;
#define FALSE 0
#define TRUE  1

using namespace std;

typedef vector<double> ligne;
typedef vector<ligne> Grille;

void traitementA(Grille &matrice,	Grille &matriceResultat);

void traitementB(Grille &matrice,	Grille &matriceResultat);

void traitementC(Grille &matrice,	Grille &matriceResultat);

void showGrille(const Grille &matrice);

void lectureGrilleA(Grille &matrice, int tailleTab);

bool candidateTerrain(const int &tailleTab, const int &i, const int &j);

bool celluleLibre(const Grille &matrice, const int &i, const int &j);

void changeCaseValue(Grille &matrice, const int &i, const int &j, const double &caseValue);

void initialiseGrille(Grille &matrice, const int tailleTab);

double caseValue(const Grille &matrice, const int &row, const int &col);

void construire_passage(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab);

bool changeBlacktoWhite(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab);

void parcoursPremiereLigne(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab);

double grillWithProbability(Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &p, const double &maxGeneration);

bool checkLastLine(const Grille &matrice, const int &tailleTab);

void probSucces_enVariantP_NBTFixe(Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration);

void diviserIntervalle(Grille &stocking, const double min, const double max, const double MIN_DELTA_P, const double MAX_ERROR, Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration);

bool premiereCondition(const double min, const double max, const double MIN_DELTA_P);

int deuxiemeCondition(Grille &stocking, const double min, const double max, const double MAX_ERROR, Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration);

void valeursInitilaes(const double min, const double max);

void stockValues(Grille &stocking, double &moyenne, double &pPrime);

void trierTab(Grille &stocking);

void permute(Grille &stocking, const int &row, const int &col);

#endif
