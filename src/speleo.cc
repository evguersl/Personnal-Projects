#include "speleo.h"

int main(int argc, char **argv)
{
	char typeTab;
	Grille matrice;
	Grille matriceResultat;
	
	cin>>typeTab;

	
	switch(typeTab)
	{
		case 'a':
			
		   traitementA(matrice, matriceResultat);
						
			break;
			
		case 'b':
			
			traitementB(matrice, matriceResultat);

			break;
			
		case 'c':
			
			traitementC(matrice, matriceResultat);
			
			break;
			
		default:
			break;
	}
	
	return 0;
}








