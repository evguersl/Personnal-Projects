#include <stdio.h>

#define TAILLE_MAX 10
typedef unsigned char BOOL;
#define FALSE 0
#define TRUE  1

void trierTab(int tab[], int tailleMax);
void improvedAfficherTab(int tab[], int tabLen);
void permute(int *a, int *b);

int main()
{
	int tab1[] = {3,2,1,8,7,0,4,9,6,5};
	improvedAfficherTab(tab1, TAILLE_MAX);
	trierTab(tab1, TAILLE_MAX);
	improvedAfficherTab(tab1, TAILLE_MAX);
	
	return 0;
}

void permute(int *a, int *b)
{
	int reserve=(*a);
	(*a)=(*b);
	(*b)=reserve;
}

void trierTab(int tab[], int tabLen)
{
	int tourPermutation=0;
	int tourBoucle=0;
	int lastElement=tabLen-1;
	BOOL isTabSorted=FALSE;
	do
	{
		isTabSorted=TRUE;
		for(int i=0; i<lastElement; i++)
		{
			if(tab[i]>tab[i+1])
			{
				permute(&tab[i], &tab[i+1]);
				tourPermutation++;
				isTabSorted=FALSE;
			}		
			tourBoucle++;
		}
		lastElement--;
	}while(!isTabSorted);
	printf("\tNombre de tours : %d \n\tNombre de permutations : %d \n", tourBoucle, tourPermutation);
		
}

void improvedAfficherTab(int *tab, int tabLen)
{
	for(int i=0; i<tabLen; i++)
	{
		printf("%5d",tab[i]);
	}
	printf("\n");
} 




