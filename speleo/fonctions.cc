#include "speleo.h"

//Fonctions qui traitent en fonction du type de tab

void traitementA(Grille &matrice,	Grille &matriceResultat)
{
	int tailleTab;
	cin>>tailleTab;
	cout<<endl<<"Tab de type a, taille : "<<tailleTab<<endl;
		
	lectureGrilleA(matrice, tailleTab);			
	showGrille(matrice);
	
	initialiseGrille(matriceResultat, tailleTab);
	showGrille(matriceResultat);
	
	parcoursPremiereLigne(matrice, matriceResultat, 0, 0, tailleTab);

	showGrille(matriceResultat);
}


void traitementB(Grille &matrice,	Grille &matriceResultat)
{
	int tailleTab;
	double probability;
	int maxGeneration;
	
	cin>>tailleTab;
	cin>>probability;
	cin>>maxGeneration;
	
	cout<<"Tab de type b, taille : "<<tailleTab<<", probabilite de 0 present : "<<probability<<", nbr de tableaux à generer : "<<maxGeneration<<endl;

	cout<<"Bilan : "<<grillWithProbability(matrice, matriceResultat, tailleTab, probability, maxGeneration)<<endl;
}

void traitementC(Grille &matrice,	Grille &matriceResultat)
{
	Grille stocking;
	int tailleTab;
	int maxGeneration;
	
	cin>>tailleTab;
	cin>>maxGeneration;
	
	double min = 0.0;
	double max = 1.0;
	
	
	//pas bon pour la redirection
	//cout<<"Tab de type c, taille : "<<tailleTab<<", nbr de tableaux à generer : "<<maxGeneration<<endl;
			
	//probSucces_enVariantP(matrice, matriceResultat, tailleTab, maxGeneration);
	
	//valeursInitilaes(min, max);
	
	diviserIntervalle(stocking, min, max, 0.000001, 0.01, matrice, matriceResultat, tailleTab, maxGeneration);
	showGrille(stocking);
	trierTab(stocking);
	showGrille(stocking);
	
}






//enregistre la grille dans un tab

void lectureGrilleA(Grille &matrice, int tailleTab)
{
	int valueCase;

	for(int row=0; row<tailleTab; row++)
	{
		ligne currentRow;
	
		for(int col=0; col<tailleTab; col++)  //comment gère-t-il le saut de ligne avec le compteur ???
		{
			cin>>valueCase;
			currentRow.push_back(valueCase);
		}
	
		matrice.push_back(currentRow);
		currentRow.clear();
	}
}

//affiche la grille

void showGrille(const Grille &matrice)
{
	for(int row=0; row<matrice.size(); row++)
	{
		for(int col=0; col<matrice[row].size(); col++)
		{
			cout<<matrice[row][col]<<" ";
		}
	cout<<endl;
	}
	
	
	cout<<endl<<endl<<endl;
}

//verifie si une case est bien comprise dans le tableau

bool candidateTerrain(const int &tailleTab, const int &row, const int &col)
{
	if(   row>=0    &&    row<tailleTab   &&   col>=0   &&   col<tailleTab )
	{
		return true;
	}
	else 
	{
		return false;
	}
}


//verifie si la cellule est libre 

bool celluleLibre(const Grille &matrice, const int &row, const int &col)
{
	if(matrice[row][col]==0)
	{
		return true;
	}
	else
	{
		return false;
	}
}


//Change la valeur d'une case  (peut on faire plus simple sans les pointeurs ???)

void changeCaseValue(Grille &matrice, const int &row, const int &col, const double &caseValue)
{
	//porque pas matrice[][]=caseValue; ??????
	
	double *value;
	
	value = &matrice[row][col];
	
	*value=caseValue;
}

//initialise à 1 toutes les cases de la grille

void initialiseGrille(Grille &matrice, const int tailleTab)
{
	
	for(int row=0; row<tailleTab; row++)
	{
		ligne currentRow;
		
		for(int col=0; col<tailleTab; col++)
		{
			currentRow.push_back(1);
		}
		matrice.push_back(currentRow);
		currentRow.clear();
	}
	
}

//Valeur d'une case

double caseValue(const Grille &matrice, const int &row, const int &col)
{
	return matrice[row][col];
}


//construction du passage

void construire_passage(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab)
{
	
	if( changeBlacktoWhite(libre, passage, row-1, col, tailleTab) )
	{
		changeCaseValue(passage, row-1, col, 0);
		//showGrille(passage);
		construire_passage(libre, passage, row-1, col, tailleTab);
	}
	
	if( changeBlacktoWhite(libre, passage, row, col+1, tailleTab) )
	{
		changeCaseValue(passage, row, col+1, 0);
		//showGrille(passage);
		construire_passage(libre, passage, row, col+1, tailleTab);
	}
	
	if( changeBlacktoWhite(libre, passage, row+1, col, tailleTab) )
	{
		changeCaseValue(passage, row+1, col, 0);
		//showGrille(passage);
		construire_passage(libre, passage, row+1, col, tailleTab);
	}
	
	if( changeBlacktoWhite(libre, passage, row, col-1, tailleTab) )
	{
		changeCaseValue(passage, row, col-1, 0);
		//showGrille(passage);
		construire_passage(libre, passage, row, col-1, tailleTab);
	}
	
}


//valide si une cellule passe de 1 à 0 (en verifiant todo)

bool changeBlacktoWhite(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab)
{
	if( candidateTerrain(tailleTab, row, col) && celluleLibre(libre, row, col) && !celluleLibre(passage, row, col) )
	{
		return true;
	}
	else
	{
		return false;
	}
	
	
}

//parcours de la premiere ligne pour affecter un construire_passage aux cases libres 

void parcoursPremiereLigne(const Grille &libre, Grille &passage, const int &row, const int &col, const int &tailleTab)
{
	for(int colonne=0; colonne<tailleTab; colonne++)
	{
		if( 0 == caseValue(libre, row, colonne) )
		{
			changeCaseValue(passage, row, colonne, 0);
			construire_passage(libre, passage, 0, colonne, tailleTab);
		}
	}
	
}

//prob de tab obtenus avec passage Nord-Sud pour une prob de p cases libres genere maxGeneration fois

double grillWithProbability(Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &p, const double &maxGeneration)
{

	double totaleReussite=0.0;
	double finalTot;

	//generateur de nbr aleatoire
	default_random_engine generator;
	bernoulli_distribution distribution(p);


	//on repete maxGeneration fois l'operation de creer un tab avec la prob de cellule libre p 
	//on verifie si il existe un passage Nord-Sud et on comptabilise les succes
	//on supprimme toutes les cases des grilles à chaque nouvelle generation
	for(int generation=0; generation<maxGeneration; generation++)
	{
		initialiseGrille(matrice, tailleTab);
		initialiseGrille(matriceResultat, tailleTab);
		
		for(int row=0; row<tailleTab; row++)
		{
			for(int col=0; col<tailleTab; col++)
			{
				if( distribution(generator) )
				{
					changeCaseValue(matrice, row, col, 0);
				}
			}
		}
		
		//essaie de creer un passage
		parcoursPremiereLigne(matrice, matriceResultat, 0, 0, tailleTab);

		//verifie si il existe un passage
		if( checkLastLine(matriceResultat, tailleTab) )
		{
			totaleReussite++;
			//showGrille(matrice);
			//showGrille(matriceResultat);
		}
		
		//supprime contenu
		matrice.clear();
		matriceResultat.clear();
	}
	
	finalTot = totaleReussite/maxGeneration;
	
	return finalTot;
}

//verife si un 0 est present sur la derniere ligne

bool checkLastLine(const Grille &matrice, const int &tailleTab)
{
	for(int col=0; col<tailleTab; col++)
	{
		if( 0==matrice[tailleTab-1][col] ) 
		{
			return true;
		}
	}
	return false;
}

//affiche la prob de succe de traversée d'une grille en faisant varier p selon un pas fixe

void probSucces_enVariantP_NBTFixe(Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration)
{
	double pas = 1.0/(102-2.0);
	
	for(double p=0; p<=1.0; p=p+pas)
	{
		cout<<p<<"  "<<grillWithProbability(matrice, matriceResultat, tailleTab, p, maxGeneration)<<endl;
	}
}

//initialise le fichier avec 0.0 - 0.0 et 1.0 - 1.0

void valeursInitilaes(const double min, const double max)
{
	cout<<min<<" "<<min<<endl;
	cout<<max<<" "<<max<<endl;
}



//verifie si l'on peut diviser notre intervalle

void diviserIntervalle(Grille &stocking, const double min, const double max, const double MIN_DELTA_P, const double MAX_ERROR, Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration)
{
	
	int scenarioNouvelIntervalle;
	scenarioNouvelIntervalle = deuxiemeCondition(stocking, min, max, MAX_ERROR, matrice, matriceResultat, tailleTab, maxGeneration);
	
	/*
	bool premiereCondition;
	premiereCondition = premiereCondition(min, max, MIN_DELTA_P);
	*/
	
	
	
	//cout<<"premiereCondition : "<<premiereCondition(min, max, MIN_DELTA_P)<<" :: scenarioNouvelIntervalle : "<<scenarioNouvelIntervalle;
	
	if( premiereCondition(min, max, MIN_DELTA_P) && (scenarioNouvelIntervalle !=0 ) )
	{
		if( scenarioNouvelIntervalle == 1)
		{
			diviserIntervalle(stocking, min, (max+min)/2, MIN_DELTA_P, MAX_ERROR, matrice, matriceResultat, tailleTab, maxGeneration);
		}
		else if( scenarioNouvelIntervalle == 2)
		{
			diviserIntervalle(stocking, (max+min)/2, max, MIN_DELTA_P, MAX_ERROR, matrice, matriceResultat, tailleTab, maxGeneration);
		}
	}
	
}

//check premiere condition pour la division

bool premiereCondition(const double min, const double max, const double MIN_DELTA_P)
{
	if( (max - min) > MIN_DELTA_P )
	{
		return true;
	}
	else
	{
		return false;
	}
}

//check deuxieme condition pour la division (0 echec, 1 [min ; max+min/2], 2 [max+min/2;max])

int deuxiemeCondition(Grille &stocking, const double min, const double max, const double MAX_ERROR, Grille &matrice, Grille &matriceResultat, const int &tailleTab, const double &maxGeneration)
{
	
	double pPrime;
	double p;
	double minPrime;
	double maxPrime;
	double a;
	double b;
	double error;
	double moyenne;
	
	
	moyenne = (max+min)/2;
	
	//cout<<endl;
	
	pPrime = grillWithProbability(matrice, matriceResultat, tailleTab, moyenne, maxGeneration);
	minPrime = grillWithProbability(matrice, matriceResultat, tailleTab, min, maxGeneration);
	maxPrime = grillWithProbability(matrice, matriceResultat, tailleTab, max, maxGeneration);
	
	//creation de la fonction affine y = ax +b
	
	a = ( maxPrime - minPrime ) / (max -min);
	b = maxPrime - a*max;
	
	
	
	p = a * ( moyenne ) + b;
	
	
	
	error = pPrime - p;
	
	//cout<<"min  :"<<min<<endl;
	//cout<<"max  :"<<max<<endl;
	//cout<<"pPrime : "<<pPrime<<endl;
	//cout<<"minPrime : "<<minPrime<<endl;
	//cout<<"maxPrime : "<<maxPrime<<endl;
	//cout<<"a : "<<a<<endl;
	//cout<<"b : "<<b<<endl;
	//cout<<"p : "<<p<<endl;
	//cout<<"error : "<<error<<endl;
	
	if(error<0)
	{
		if( (-error) > MAX_ERROR )
		{
			stockValues(stocking, moyenne, pPrime);
			//cout<<moyenne<<"   "<<pPrime<<endl;
			return 2;
		}
		else
		{
			stockValues(stocking, moyenne, pPrime);
			//cout<<moyenne<<"   "<<pPrime<<endl;
			return 0;
		} 
	}
	else
	{
		if( (error) > MAX_ERROR )
		{
			stockValues(stocking, moyenne, pPrime);
			//cout<<moyenne<<"   "<<pPrime<<endl;
			return 1;
		}
		else
		{
			stockValues(stocking, moyenne, pPrime);
			//cout<<moyenne<<"   "<<pPrime<<endl;
			return 0;
		} 
	}
}

//stocke les valeurs dans un tab2D pour après les trier

void stockValues(Grille &stocking, double &moyenne, double &pPrime)
{
	ligne currentRow;
	
	currentRow.push_back(moyenne);
	currentRow.push_back(pPrime);
	
	stocking.push_back(currentRow);

}

//tri notre tab avec les bonnes valeurs (code en C)


void permute(Grille &stocking, const int &row, const int &col)
{
	double l1 = caseValue(stocking, row, col);
	double l2 = caseValue(stocking, row+1, col);
	
	double c1 = caseValue(stocking, row, col+1);
	double c2 = caseValue(stocking, row+1, col+1);;
	
	changeCaseValue(stocking, row, col, l2);
	changeCaseValue(stocking, row, col+1, c2);
	
	changeCaseValue(stocking, row+1, col, l1);
	changeCaseValue(stocking, row+1, col+1, c1);
}

void trierTab(Grille &stocking)
{
	int lastElement = 8 ; //faire une méthode pour la taille
 	BOOL isTabSorted=FALSE;
	do
	{
		isTabSorted=TRUE;
		for(int i=0; i<lastElement; i++)
		{
			if( caseValue(stocking, i, 0) > caseValue(stocking, i+1, 0) )
			{
				permute(stocking, i, 0);
				isTabSorted=FALSE;
			}		
		}
		lastElement--;
	}while(!isTabSorted);
		
}



//void changeCaseValue(Grille &matrice, const int &row, const int &col, const int &caseValue)
//double caseValue(const Grille &matrice, const int &row, const int &col)
