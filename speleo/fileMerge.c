/*
 * --------------------------
 * fileMerge.c (files Merge)
 * --------------------------
 * t01():	Merge/Fusion de 2 tableaux INT dans un 3 ème taleaux en sortie
 * t022():
	- Génération de valeurs aléatoires pour 1 tableau1 d'entiers (int)
	- tri du tableau
	- sauvegarde dans un fichier (sans sauvegarder taille du tableau comme firstValue) !!!
 * t03():
	- - Merge/Fusion de 2 fichiers INT dans un 3 ème fichier en sortie
 * 
 * gcc -Wall mergeT01.c ../src/tabs.c ../src/escapeSequences.c -o mergeT01
 * OR with -DTRACE --> DEBUG
 * gcc -Wall mergeT01.c ../src/tabs.c ../src/escapeSequences.c -DTRACE=0 -o mergeT01
 * 
 */


#include <stdio.h>
#include "../../include/tabs.h"
/* FILES */
#include <fcntl.h>	/* file IO */
#include <unistd.h>	/* file close() */
/* =========================================================================
 * The following flag values are accessible only to the open subroutine:
Item 		Description
---------	-------------
O_RDONLY 	Read-only
O_WRONLY 	Write-only
O_RDWR 		Read and write
O_CREAT 	Open with file create (uses the third open argument)
O_TRUNC 	Open with truncation
O_DIRECT 	Open for Direct I/O
O_EXCL 		Exclusive open 
* 
* mode_t mode = 
	S_IRUSR 	USER  can Read
	S_IWUSR  	USER  can Write
	S_IRGRP  	GROUP can Read
	S_IROTH		OTHER can Read
	* open(char *fileName, flag, mode)
	* open(char *fileName, flag)
* ---------------------------------------------------------------------------- */



int t01()
{
	int tab1[]={4, 3, 6, 7, 1 };
	int tab2[]={ 8, 5, 2 };
	
	int tab3[8];
	
	printf("\n=== tab1 BEFORE & AFTER sorting ===\n");
	tabShow(tab1, sizeof(tab1)/sizeof(int) );
	//printf("\n");
	sort02(tab1, sizeof(tab1)/sizeof(int), ASC);
	//printf("=== tab1 AFTER sorting ===\n");
	tabShow(tab1, sizeof(tab1)/sizeof(int) );
	
	printf("\n");
	printf("\n=== tab2 BEFORE & AFTER sorting ===\n");
	tabShow(tab2, sizeof(tab2)/sizeof(int) );
	//printf("\n");
	sort02(tab2, sizeof(tab2)/sizeof(int), ASC);
	//printf("=== tab2 AFTER sorting ===\n");
	tabShow(tab2, sizeof(tab2)/sizeof(int) );
	
	printf("\n");
	mergeIntTabs(tab1, sizeof(tab1)/sizeof(int),  tab2, sizeof(tab2)/sizeof(int),   tab3, sizeof(tab3)/sizeof(int)  );
	printf("\n=== tab3 AFTER MERGING ===\n");
	tabShow(tab3, sizeof(tab3)/sizeof(int) );
	return(0);

}
/*
 * - Génération de valeurs aléatoires pour 2 tableaux d'entiers (int)
 * - création d'un 3ème tableau, dont la dimenstion = somme dimensions des 2 premiers tableaux
 * - tri des 2 tablexu en entrée
 * - fusion des 2 tableaux dans le 3 ème.
 * 
 */
int t021()
{
	int *tabInt1Ptr, *tabInt2Ptr,  *tabInt3Ptr;;
	int tab1Len, tab2Len, tab3Len=0;
	/* ============================================= */
	/* ========= */
	/* TABLEAU 1 */
	/* ========= */
	int tabLen;
	printf("taille du tableau 1 ? ");	scanf("%i", &tabLen);
	/* ---------------------------------------------- */
	/**/
	if(tabLen<=0)
	{
		fprintf(stderr, "ERROR !!!!!! tabLen<=0 ..... \n");
		return(-1);
	}
	tab1Len=tabLen;
	tabInt1Ptr=createIntTab(tab1Len);
	if(tabInt1Ptr==NULL)
	{
		fprintf(stderr, "createIntTab() ... FAILED\n");
		return(-1);
	}
	tab3Len+=tab1Len;
	tabIntInitValues(tabInt1Ptr,  tab1Len);
	
	/* ========= */
	/* TABLEAU 2 */
	/* ========= */
	printf("taille du tableau 2 ? ");	scanf("%i", &tab2Len);
	/* ---------------------------------------------- */
	/**/
	if(tab2Len<=0)
	{
		freeIntTab(tabInt1Ptr);
		fprintf(stderr, "ERROR !!!!!! tabLen<=0 ..... \n");
		return(-1);
	}
	tabInt2Ptr=createIntTab(tab2Len);
	tab3Len+=tab2Len;
	if(tabInt2Ptr==NULL)
	{
		freeIntTab(tabInt1Ptr);
		fprintf(stderr, "createIntTab() ... FAILED\n");
		return(-1);
	}
	tabIntInitValues(tabInt2Ptr,  tab2Len);
	/* ========= */
	/* TABLEAU 3 */
	/* ========= */
	tabInt3Ptr=createIntTab(tab3Len);
	if(tabInt3Ptr==NULL)
	{
		freeIntTab(tabInt1Ptr);
		freeIntTab(tabInt2Ptr);
		fprintf(stderr, "createIntTab() ... FAILED\n");
		return(-1);
	}
	
	/**/
	
	printf("\n=== tab1 BEFORE & AFTER sorting ===\n");
	tabShow(tabInt1Ptr, tab1Len );
	//printf("\n");
	sort02(tabInt1Ptr, tab1Len, ASC);
	//printf("=== tab1 AFTER sorting ===\n");
	tabShow(tabInt1Ptr, tab1Len );
	
	printf("\n");
	printf("\n=== tab2 BEFORE & AFTER sorting ===\n");
	tabShow(tabInt2Ptr, tab2Len );
	//printf("\n");
	sort02(tabInt2Ptr, tab2Len, ASC);
	//printf("=== tab2 AFTER sorting ===\n");
	tabShow(tabInt2Ptr, tab2Len );
	
	printf("\n");
	mergeIntTabs(tabInt1Ptr, tab1Len,  tabInt2Ptr, tab2Len,   tabInt3Ptr, tab3Len  );
	printf("\n=== tab3 AFTER MERGING ===\n");
	tabShow(tabInt3Ptr, tab3Len );
	

	
	freeIntTab(tabInt1Ptr);
	freeIntTab(tabInt2Ptr);
	freeIntTab(tabInt3Ptr);
	
	printf("\n");
	return 0;

}
/*
 * - Génération de valeurs aléatoires pour 1 tableau1 d'entiers (int)
 * - tri du tableau
 * - sauvegarde dans un fichier (sans sauvegarder taille du tableau comme firstValue) !!!
 */
int saveTabIntDatasToFile_2(int tab[], int tabLen,char *fileName)
{
	if(TRACE)	printf("\n------------- saveTabIntDatasToFile(%s) -----------------\n", fileName );
	//int handle=ouvrirFichier(fileName);
	int handle;
	/* See opening file flags & mode above */
	int openingFileFlag=O_WRONLY | O_CREAT | O_TRUNC;
	mode_t mode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH;
	//if ((handle=open(fileName, O_WRONLY | O_CREAT | O_TRUNC, mode))==-1)
	if ((handle=open(fileName, openingFileFlag, mode))==-1)
	{
		fprintf(stderr,"saveTabIntDatasToFile() %s ...  FAILED\n", fileName);
		return(-1);
	}
	/* */
	/* write tabLen : first data */
	/* PAS NECESSAIRE ICI !
	 write(handle, &tabLen, sizeof(int) ); */
	
	for(int i=0; i<tabLen; i++)
	{
		write(handle, &tab[i], sizeof(tab[i]) );
	}
	close(handle);
	return(0);
}
int t022(char *fileName)
{
	int *tabInt1Ptr;
	int tab1Len=0;
	/* ============================================= */
	/* ========= */
	/* TABLEAU  */
	/* ========= */
	int tabLen;
	printf("taille du tableau ? ");	scanf("%i", &tabLen);
	/* ---------------------------------------------- */
	/**/
	if(tabLen<=0)
	{
		fprintf(stderr, "ERROR !!!!!! tabLen<=0 ..... \n");
		return(-1);
	}
	tab1Len=tabLen;
	tabInt1Ptr=createIntTab(tab1Len);
	if(tabInt1Ptr==NULL)
	{
		fprintf(stderr, "createIntTab() ... FAILED\n");
		return(-1);
	}
	tabIntInitValues(tabInt1Ptr,  tab1Len);
	/**/
	
	printf("\n=== tab BEFORE & AFTER sorting ===\n");
	tabShow(tabInt1Ptr, tab1Len );
	//printf("\n");
	sort02(tabInt1Ptr, tab1Len, ASC);
	//printf("=== tab AFTER sorting ===\n");
	tabShow(tabInt1Ptr, tab1Len );
	
	/* Sauvegarde dans fichier en sortie */
	saveTabIntDatasToFile_2(tabInt1Ptr, tab1Len,fileName);


	
	freeIntTab(tabInt1Ptr);
	
	printf("\n");
	return 0;

}
/*
 * - fusion des 2 fichiers dans un 3 ème.
 * */
int t03(char *inputFileName1, char *inputFileName2, char *outputFileName)
{
	int f1, f2, f3;

	if ((f1=open(inputFileName1, O_RDONLY ))==-1)
	{
		printf(" opening File  %s.... FAILED \n", inputFileName1);
		return(-1);
	}
	if ((f2=open(inputFileName2, O_RDONLY ))==-1)
	{
		printf(" opening File  %s.... FAILED \n", inputFileName2);
		close(f1);
		return(-1);
	}
	/* ===== */
	mode_t mode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH;
	if ((f3=open(outputFileName, O_WRONLY | O_CREAT | O_TRUNC, mode))==-1)
	{
		fprintf(stderr," opening File  %s....  FAILED\n", outputFileName);
		close(f1);
		close(f2);
		return(-1);
	}
	/* FUSION */
	int  status1=0;
	int value1;
	int  status2=0;
	int value2;

	/**/
	status1=read(f1,&value1,sizeof(int) );
	status2=read(f2,&value2,sizeof(int) );
	while(status1!=0 &&  status2!=0)
	{
		if(value1<value2)
		{
			write(f3, &value1, sizeof(int) );
			status1=read(f1,&value1,sizeof(int) );
		}
		else
		{
			write(f3, &value2, sizeof(int) );
			status2=read(f2,&value2,sizeof(int) );
		}
	}
	//---
	if(status1!=0)
	{
		while(status1!=0)
		{
			write(f3, &value1, sizeof(int) );
			status1=read(f1,&value1,sizeof(int) );
		}
	}
	else
	{
		while(status2!=0)
		{
			write(f3, &value2, sizeof(int) );
			status2=read(f2,&value2,sizeof(int) );
		}
	}
	

	close(f1);
	close(f2);
	close(f3);

	return(0);
}
int dumpIntFile(char *fileName)
{
	int fileHandle;
	if ((fileHandle=open(fileName, O_RDONLY ))==-1)
	{
		fprintf(stderr," opening File  %s.... FAILED\n", fileName);
		return(-1);
	}

	int  status=0;
	int value;
	/**/
	status=read(fileHandle,&value,sizeof(int) );
	while(status!=0 )
	{
		printf("!%4d ", value );
		status=read(fileHandle,&value,sizeof(int) );	
	}
	if(1)
		printf("!");
	
	close(fileHandle);
	return(0);

}
int main(int argc, char **argv)
{
	//OK t01();
	//OK t021();
	t022("data/f1.dat");
	t022("data/f2.dat");
	t03("data/f1.dat", "data/f2.dat", "data/f3.dat");
	printf("\n\n --- f1.dat ---\n");
	dumpIntFile("data/f1.dat");
	
	printf("\n\n --- f2.dat ---\n");
	dumpIntFile("data/f2.dat");
	
	printf("\n\n --- f3.dat ---\n");
	dumpIntFile("data/f3.dat");
	printf("\n\n");
	return 0;
}

