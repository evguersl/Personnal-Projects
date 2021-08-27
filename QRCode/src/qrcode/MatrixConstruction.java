package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	
	public static int W = 0xFF_FF_FF_FF;
	public static int B = 0xFF_00_00_00;
	public static int NumVersion = 1;

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {
		
		NumVersion=version;
		
		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);
		//damage(matrix,0.054);
		/*
		 * Bonus
		 * 
		 */
		
		System.out.println("Points de Penalite : "+evaluate(matrix));
		
		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		
		if(version<1 || version>4)
		{
			System.out.println("Version trop grande ou trop petite");
			System.exit(1);;
		}
		
		int matrice[][];
		matrice=initializeMatrix(version);
		
		addFinderPatterns(matrice);
		addAlignmentPatterns(matrice,version);
		addTimingPatterns(matrice);
		addDarkModule(matrice);
		addFormatInformation(matrice,mask);
		
		return matrice;

	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	
	//pas très clair : int red = 0xFF_FF_00_00;
	
	public static int[][] initializeMatrix(int version) {
		
		int tailleMatrice = QRCodeInfos.getMatrixSize(version);
		
		int[][] tabInt = new int[tailleMatrice][tailleMatrice]; 
		
		for(int i=0; i<tailleMatrice; i++)
		{
			for(int j=0; j<tailleMatrice; j++)
			{
				tabInt[i][j]= 0x00_00_00_00;
			}
		}
		
		return tabInt;
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		
		int tailleMax=matrix.length;
		
		int[][] tab1= {{W,B,B,B,B,B,B,B},{W,B,W,W,W,W,W,B},{W,B,W,B,B,B,W,B},{W,B,W,B,B,B,W,B},{W,B,W,B,B,B,W,B},{W,B,W,W,W,W,W,B},{W,B,B,B,B,B,B,B},{W,W,W,W,W,W,W,W}};
		int[][] tab2= {{B,B,B,B,B,B,B,W},{B,W,W,W,W,W,B,W},{B,W,B,B,B,W,B,W},{B,W,B,B,B,W,B,W},{B,W,B,B,B,W,B,W},{B,W,W,W,W,W,B,W},{B,B,B,B,B,B,B,W},{W,W,W,W,W,W,W,W}};
		int[][] tab3= {{W,W,W,W,W,W,W,W},{B,B,B,B,B,B,B,W},{B,W,W,W,W,W,B,W},{B,W,B,B,B,W,B,W},{B,W,B,B,B,W,B,W},{B,W,B,B,B,W,B,W},{B,W,W,W,W,W,B,W},{B,B,B,B,B,B,B,W}};
		
		placement2D(tailleMax-8,0,tab1, matrix);
		placement2D(0,0,tab2, matrix);
		placement2D(0,tailleMax-8,tab3, matrix);
		
		
		
		
		
		/*
		 * Dans le même genre possible???
		 * 
		int[][] tab= {l1,l2,l3,l3,l3,l2,l1};
		int[] l1 = {B,B,B,B,B,B,B};
		int[] l2 = {B,W,W,W,W,W,B};
		int[] l3 = {B,W,B,B,B,W,B};
		*/

	}
	
	
	public static void placement2D(int colonne, int ligne, int[][] pattern, int[][] matrix)
	{
		//colonne et ligne en coordonnées pixel !
		
		int colonneInitiale=colonne;
		
		int taille=pattern.length;
		
		for(int i=0; i<taille; i++)
		{
			for(int j=0; j<taille; j++)
			{
				matrix[ligne][colonne]=pattern[i][j];
				colonne++;
			}
			colonne=colonneInitiale;
			ligne++;
		}
		
		
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) 
	{
		
		if(version==1 )
		{
			return;
		}
		
		int tailleMax=(matrix[0]).length;
		
		
		int[][] tab= {{B,B,B,B,B},{B,W,W,W,B},{B,W,B,W,B},{B,W,W,W,B},{B,B,B,B,B}};
		
		placement2D(tailleMax-9,tailleMax-9,tab,matrix);
		
		
		
	}

	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) 
	{
		
		for(int i=8; i<matrix.length-8; i++)
		{
			if(i%2==0) 
			{
				matrix[6][i]=B;
				matrix[i][6]=B;
			}
			else
			{
				matrix[6][i]=W;
				matrix[i][6]=W;
			}
		}

	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		
		matrix[8][matrix.length-8]=B;
		
		
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) 
	{
		
		boolean tabBol[] = QRCodeInfos.getFormatSequence(mask);
				
		int compteur=0;
		
		for(int i=matrix.length-1; i>=0 ; i--)
		{	
			if(i==matrix.length-8)
			{
				i=8;
			}
			
			if(i==6)
			{
				i=5;
			}
			
			if(tabBol[compteur]==true)
			{
				matrix[8][i]=B;
			}
			else
			{
				matrix[8][i]=W;	
			}
			
			compteur++;
		}
		
		compteur=0;
		
		for(int i=0; i<matrix.length ;i++)
		{
			if(i==6) 
			{
				i=7;
			}
			
			if(i==8)
			{
				i=matrix.length-8;
			}
			
			if(tabBol[compteur]==true)
			{
				matrix[i][8]=B;
			}
			else
			{
				matrix[i][8]=W;	
			}
			
			compteur++;
		}
		
		
		
		
	}

	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) 
	{
		
		int couleur=0;
				
		switch(masking) {
		
		case 0:
			if( (col+row)%2==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 1:
			if( row%2==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 2:
			if( col%3==0 )
			{
				dataBit=!dataBit;
				
			}
			break;
			
		case 3:
			if( (col+row)%3==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 4:
			if( ( (row/2)+(col/3) )%2==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 5:
			if( ( (col*row)%2 ) + ( (col*row)%3 )==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 6:
			if( ( ( (col*row)%2 ) + ( (col*row)%3 ) )%2==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		case 7:
			if( ( ( (col+row)%2 ) + ( (col*row)%3 ) )%2==0 )
			{
				dataBit=!dataBit;
			}
			break;
			
		default:
			break;
		
		}
		
		if(dataBit==true)
		{
			couleur=B;
		}
		else
		{
			couleur=W;
		}
				
		
		return couleur;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	
	

	public static int p1 = 0xFF_00_FF_00;
	public static int p2 = 0xFF_00_00_FF;
	
	public static int compteurFinale1 = 0;
	public static int compteurFinale2 = 0;
	
	public static boolean entreM = false;
	public static boolean entreD = false;
	
	public static int monte=1;
	public static int descend=1;

	
	
	
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) 
	{

			if(NumVersion!=1)
			{
				int compteur1=0;
				int compteur2=0;
				
				boolean tabCompletion[] = new boolean[matrix.length*matrix.length];
				
				for(int i=0; i<matrix.length*matrix.length; i++)
				{
					tabCompletion[i]=false;
					//System.out.println("tabCompletion["+i+"] : "+tabCompletion[i]);
				}
				
				monte(matrix,data,tabCompletion,mask,matrix.length-1,matrix.length-1,8);
				
				descente(matrix,data,tabCompletion,mask,matrix.length-3,9,matrix.length);
				
				monte(matrix,data,tabCompletion,mask,matrix.length-5,matrix.length-1,matrix.length-5);
				monte(matrix,data,tabCompletion,mask,matrix.length-5,matrix.length-10,8);
				
				descente(matrix,data,tabCompletion,mask,matrix.length-7,9,matrix.length-9);
				descente(matrix,data,tabCompletion,mask,matrix.length-7,matrix.length-4,matrix.length);		
				
				monte(matrix,data,tabCompletion,mask,matrix.length-9,matrix.length-1,matrix.length-5);
				
				for(int i=0; i<5; i++)
				{
					matrix[matrix.length-10][matrix.length-(5+i)]=maskColor(matrix.length-10, matrix.length-(5+i), data[compteurFinale1], mask);
					compteurFinale1++;
				}
				
				monte(matrix,data,tabCompletion,mask,matrix.length-9,matrix.length-10,6);
				monte(matrix,data,tabCompletion,mask,matrix.length-9,5,-1);
				
				for(int i=matrix.length-11; i>=10;i-=2)
				{
					if(compteur1%2==0) 
					{
						descente(matrix,data,tabCompletion,mask,i,0,6);
						descente(matrix,data,tabCompletion,mask,i,7,matrix.length);
						compteur1++;
					}
					else
					{
						monte(matrix,data,tabCompletion,mask,i,matrix.length-1,6);
						monte(matrix,data,tabCompletion,mask,i,5,-1);
						compteur1++;
					}
				}
				
				monte(matrix,data,tabCompletion,mask,8,matrix.length-9,8);
				
				for(int i=5; i>0; i-=2)
				{
					if(compteur2%2==0) 
					{
						descente(matrix,data,tabCompletion,mask,i,9,matrix.length-8);
						compteur2++;
					}
					else
					{
						monte(matrix,data,tabCompletion,mask,i,matrix.length-9,8);
						compteur2++;
					}
				}			
			}
			else
			{
				boolean tabCompletion[] = new boolean[matrix.length*matrix.length];
				
				for(int i=0; i<matrix.length*matrix.length; i++)
				{
					tabCompletion[i]=false;
					//System.out.println("tabCompletion["+i+"] : "+tabCompletion[i]);
				}
				
				monte(matrix,data,tabCompletion,mask,matrix.length-1,matrix.length-1,8);
				
				descente(matrix,data,tabCompletion,mask,matrix.length-3,9,matrix.length);
				
				monte(matrix,data,tabCompletion,mask,matrix.length-5,matrix.length-1,8);
				
				descente(matrix,data,tabCompletion,mask,matrix.length-7,9,matrix.length);	
				
				monte(matrix,data,tabCompletion,mask,matrix.length-9,matrix.length-1,6);
				monte(matrix,data,tabCompletion,mask,matrix.length-9,5,-1);

				descente(matrix,data,tabCompletion,mask,matrix.length-11,0,6);	
				descente(matrix,data,tabCompletion,mask,matrix.length-11,7,matrix.length);	
				
				monte(matrix,data,tabCompletion,mask,matrix.length-13,12,8);

				descente(matrix,data,tabCompletion,mask,matrix.length-16,9,13);	

				monte(matrix,data,tabCompletion,mask,matrix.length-18,12,8);

				descente(matrix,data,tabCompletion,mask,matrix.length-20,9,13);	

			}
			
			
					
	}
	
	public static void monte(int[][] matrix,boolean[] data,boolean[] tabCompletion,int mask,int col, int row, int stopRow)
	{
		
		//System.out.println("monte : "+monte);
		monte++;
		
		
			if(compteurFinale1!=data.length)
			{
				while(row!=stopRow) 
				{
					matrix[col][row]=maskColor(col, row, data[compteurFinale1], mask);
					compteurFinale1++;
					col--;
					
					if(compteurFinale1==data.length)
					{
						entreM=true;
						break;
					}

					matrix[col][row]=maskColor(col, row, data[compteurFinale1], mask);
					compteurFinale1++;
					row--;
					col++;
					
					if(compteurFinale1==data.length)
					{
						entreM=false;
						break;
					}
					
				};
			}
			

			if(compteurFinale1==data.length)
			{
				while(row!=stopRow) 
				{
					
					if(entreM)
					{
						matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
						compteurFinale2++;
						row--;
						col++;
						entreM=false;
					}
					
					matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
					compteurFinale2++;
					col--;

					matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
					compteurFinale2++;
					row--;
					col++;
				};
			}
			


	}
	
	public static void descente(int[][] matrix,boolean[] data,boolean[] tabCompletion,int mask,int col, int row, int stopRow)
	{
		
		//System.out.println("descend ; "+descend);
		descend++;
		
			if(compteurFinale1!=data.length)
			{
				while(row!=stopRow) {
					
					matrix[col][row]=maskColor(col, row, data[compteurFinale1], mask);
					compteurFinale1++;
					col--;
					
					if(compteurFinale1==data.length)
					{
						entreD=true;
						break;
					}
					
					matrix[col][row]=maskColor(col, row, data[compteurFinale1], mask);
					compteurFinale1++;
					row++;
					col++;
					
					if(compteurFinale1==data.length)
					{
						entreD=false;
						break;
					}

				};
			}
		
			

			if(compteurFinale1==data.length)
			{
				while(row!=stopRow){
					
					if(entreD)
					{
						matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
						compteurFinale2++;
						row++;
						col++;
						entreD=false;
						continue;
					}
						
					
					matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
					compteurFinale2++;
					col--;
					
					matrix[col][row]=maskColor(col, row, tabCompletion[compteurFinale2], mask);
					compteurFinale2++;
					row++;
					col++;

				};
			}
			

	}
	
	
	/* 5.9% version 4
	 * 5.7% version 3
	 * 5.7% version 2
	 * 5.4% version 1
	 * 
	 * ENDOMMAGE
	 * 
	 */

	public static int[][] damage(int[][] qrccode, double errorPercentage)
	{
		int nbrUnite = (int)(qrccode.length*qrccode.length*errorPercentage+1);
		int compteur =0;
		
		System.out.println("Nbr UNite :"+nbrUnite);
		
		
		for(int col=qrccode.length-1; col>qrccode.length-5;col--)
		{
			if(compteur==nbrUnite)
			{
				break;
			}
			
			for(int row=qrccode.length-1; row>8; row--)
			{
				if(compteur==nbrUnite)
				{
					break;
				}
				
				if(qrccode[col][row]==W)
				{
					qrccode[col][row]=B;
				}
				else
				{
					qrccode[col][row]=W;
				}
				
				compteur++;
			}
			
		}
		
		System.out.println("Compteur : "+compteur);
		
		return qrccode;
		
	}
	
	
	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) 
	{
		
		//Pour : "https://www.epfl.ch/fr/" 515 pts de penalite
		
		int pointPenalite=0;
		int tailleTab = matrix.length;
		
		return traitement1(matrix)+traitement2(matrix)+traitement3_01(matrix)+traitement3_02(matrix)+traitement4(matrix);
	}
	
	public static int traitement1(int[][] matrix)
	{
		int pointPenalite=0;
		int tailleTab = matrix.length;
		
		for(int row=0; row<tailleTab; row++)
		{
			for(int col=0; col<tailleTab; col++)
			{
				if( checkCarre(matrix, row, col) )
				{
					pointPenalite+=3;
				}
			}
		}
		
		System.out.println("Traitement 1 : "+pointPenalite);
		return pointPenalite;
	}
	
	public static int traitement2(int[][] matrix)
	{
		int pointPenalite=0;
		int tailleTab = matrix.length;
		
		for(int row=0; row<tailleTab; row++)
		{
			for(int col=0; col<tailleTab; col++)
			{
				if( longueSequenceLigne(matrix, row, col) )
				{
					pointPenalite+= 40;
				}
				if( longueSequenceColonne(matrix, row, col) )
				{
					pointPenalite+= 40;
				}
			}
		}
		System.out.println("Traitement 2 : "+pointPenalite);
		return pointPenalite;
	}
	
	
	public static int traitement3_01(int[][] matrix)  //pour les colonnes
	{
		int pointPenalite=0;
		int tailleTab = matrix.length;
		
		for(int row=0; row<tailleTab; row++)
		{
			for(int col=0; col<tailleTab; col++)
			{
				if( memeColorLigne(matrix, row, col) )
				{
					pointPenalite+= 3;
					col+=4;
										
					while( (col+1)!=tailleTab && matrix[row][col+1] == matrix[row][col]) //attention bien gerer les compteurs
					{
						pointPenalite++;
						col++;	
					}
				}
			}
		}
		System.out.println("Traitement 3_01 : "+pointPenalite);
		return pointPenalite;
	}
	
	
	public static int traitement3_02(int[][] matrix)  //pour les lignes
	{
		int pointPenalite=0;
		int tailleTab = matrix.length;
		
		for(int col=0; col<tailleTab; col++)
		{
			for(int row=0; row<tailleTab; row++)
			{
				if( memeColorColonne(matrix, row, col) )
				{
					pointPenalite+= 3;
					row+=4;
					
					while( (row+1)!=tailleTab && matrix[row+1][col] == matrix[row][col])
					{
						pointPenalite++;
						row++;	
					}
				}
			}
		}
		System.out.println("Traitement 3_02 : "+pointPenalite);
		return pointPenalite;
	}
	
	
	public static int traitement4(int[][] matrix)
	{
		int pointPenalite=0;
		int tailleTab = matrix.length;
		int nbrModules = tailleTab*tailleTab;
		int nbrModulesNoirs=0;
		int pourcentageModulesNoirs=0;
		int petitMultiple=0;
		int grandMultiple=0;
		int plusPetiteValeur=0;
		
		for(int row=0; row<tailleTab; row++)
		{
			for(int col=0; col<tailleTab; col++)
			{
				if(matrix[row][col]==B)
				{
					nbrModulesNoirs++;
				}
			}
		}
				
		pourcentageModulesNoirs=(nbrModulesNoirs*100)/nbrModules;
		petitMultiple = (pourcentageModulesNoirs/5)*5;
		grandMultiple = petitMultiple+5;
		
		petitMultiple-=50;
		grandMultiple-=50;
		
		if(petitMultiple<0)
		{
			petitMultiple=-petitMultiple;
		}
		
		if(grandMultiple<0)
		{
			grandMultiple=-grandMultiple;
		}
		
		if(petitMultiple<grandMultiple)
		{
			plusPetiteValeur=petitMultiple;
		}
		else
		{
			plusPetiteValeur=grandMultiple;
		}
		
		pointPenalite+=plusPetiteValeur*2;
		
		System.out.println("Traitement 4 : "+pointPenalite);
		return pointPenalite;
	}
	
	//pour la case (i;j) on verifie si (i+1;j), (i+1;j+1), (i;j+1) sont bien de la même couleur
	//impliquant ainsi avoir un carré	
	
	public static boolean checkCarre(int[][] matrix, int row, int col)
	{
		int couleur1, couleur2, couleur3, couleur4;
		int tailleTab = matrix.length;
		
		if( col == tailleTab-1 || row == tailleTab-1)
		{
			return false;
		}
		
		couleur1 = matrix[row][col];
		couleur2 = matrix[row+1][col];
		couleur3 = matrix[row][col+1];
		couleur4 = matrix[row+1][col+1];
		
		if(couleur2==couleur1 && couleur3==couleur1 && couleur4==couleur1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//on cherche la sequence demande en brassant chaque colonne
	//on va comparer les valeurs recherchées en incrementant le compteur colonne
	
	public static boolean longueSequenceLigne(int[][] matrix, int row, int col)
	{
		int tailleTab = matrix.length;
		
		if(col>tailleTab-11 || row>tailleTab-11)
		{
			return false;
		}
		
		int couleurligne1, couleurligne2, couleurligne3, couleurligne4, couleurligne5, couleurligne6, couleurligne7, couleurligne8, couleurligne9, couleurligne10, couleurligne11;
		
		couleurligne1 = matrix[row][col];
		couleurligne2 = matrix[row][col+1];
		couleurligne3 = matrix[row][col+2];
		couleurligne4 = matrix[row][col+3];
		couleurligne5 = matrix[row][col+4];
		couleurligne6 = matrix[row][col+5];
		couleurligne7 = matrix[row][col+6];
		couleurligne8 = matrix[row][col+7];
		couleurligne9 = matrix[row][col+8];
		couleurligne10 = matrix[row][col+9];
		couleurligne11 = matrix[row][col+10];
	
		
		if( ( couleurligne1 == W && couleurligne2 == W && couleurligne3 ==W  && couleurligne4 == W && couleurligne5 == B && couleurligne6 == W && couleurligne7 == B && couleurligne8 == B && couleurligne9 == B && couleurligne10 == W && couleurligne11 == B)  ||  ( couleurligne1 == B && couleurligne2 == W && couleurligne3 == B && couleurligne4 == B && couleurligne5 == B && couleurligne6 == W && couleurligne7 == B && couleurligne8 == W && couleurligne9 == W && couleurligne10 == W && couleurligne11 == W) )
		{
			return true;	
		}
		else
		{
			return false;
		}
	}
	
	
	//on cherche la sequence demande en brassant chaque ligne
	//on va comparer les valeurs recherchées en incrementant le compteur ligne	
	
	public static boolean longueSequenceColonne(int[][] matrix, int row, int col)
	{
		int tailleTab = matrix.length;
		
		if(col>tailleTab-11 || row>tailleTab-11)
		{
			return false;
		}
		
		int couleurColonne1, couleurColonne2, couleurColonne3, couleurColonne4, couleurColonne5, couleurColonne6, couleurColonne7, couleurColonne8, couleurColonne9, couleurColonne10, couleurColonne11;
		
		couleurColonne1 = matrix[row][col];
		couleurColonne2 = matrix[row+1][col];
		couleurColonne3 = matrix[row+2][col];
		couleurColonne4 = matrix[row+3][col];
		couleurColonne5 = matrix[row+4][col];
		couleurColonne6 = matrix[row+5][col];
		couleurColonne7 = matrix[row+6][col];
		couleurColonne8 = matrix[row+7][col];
		couleurColonne9 = matrix[row+8][col];
		couleurColonne10 = matrix[row+9][col];
		couleurColonne11 = matrix[row+10][col];
		
		
		if( ( couleurColonne1 == W && couleurColonne2 == W && couleurColonne3 ==  W &&couleurColonne4 == W && couleurColonne5 == B && couleurColonne6 == W && couleurColonne7 == B && couleurColonne8 == B && couleurColonne9 == B && couleurColonne10 == W && couleurColonne11 == B) || ( couleurColonne1 == B && couleurColonne2 == W && couleurColonne3 == B && couleurColonne4 == B && couleurColonne5 == B && couleurColonne6 == W && couleurColonne7 == B && couleurColonne8 == W && couleurColonne9 == W && couleurColonne10 == W && couleurColonne11 == W) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//on regarde si 5 elements de la meme ligne ont la meme couleur
	
	public static boolean memeColorLigne(int[][] matrix, int row, int col)
	{
		int tailleTab = matrix.length;
		int couleur1, couleur2, couleur3, couleur4, couleur5;
		
		if( (row>tailleTab-5) || (col>tailleTab-5) )
		{
			return false;
		}
		
		couleur1 = matrix[row][col];
		couleur2 = matrix[row][col+1];
		couleur3 = matrix[row][col+2];
		couleur4 = matrix[row][col+3];
		couleur5 = matrix[row][col+4];
		
		if(couleur2==couleur1 && couleur3==couleur1 && couleur4==couleur1 && couleur5==couleur1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//on regarde si 5 elements de la meme colonne ont la meme couleur

	
	public static boolean memeColorColonne(int[][] matrix, int row, int col)
	{
		int tailleTab = matrix.length;
		int couleur1, couleur2, couleur3, couleur4, couleur5;
		
		if( (row>tailleTab-5) || (col>tailleTab-5) )
		{
			return false;
		}
		
		couleur1 = matrix[row][col];
		couleur2 = matrix[row+1][col];
		couleur3 = matrix[row+2][col];
		couleur4 = matrix[row+3][col];
		couleur5 = matrix[row+4][col];
		
		if(couleur2==couleur1 && couleur3==couleur1 && couleur4==couleur1 && couleur5==couleur1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
}
