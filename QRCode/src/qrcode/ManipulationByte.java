package qrcode;

public class ManipulationByte {
	
	public static void main(String[] args)
	{
		ManipulationByte.conversionByte(97);
		//ManipulationByte.conversionEntier(76, 95);
	}
	
	//transforme un int en un tableau de 8 int contenant la valeur bianire du nbr entré (max 255)
	
	public static int[] conversionByte(int nbr)
	{
		int tabInt[]= {0,0,0,0,0,0,0,0};
		int reste=0;
		
		for(int i=7;i>=0;i--)
		{
			reste=nbr%2;
			tabInt[i]=reste;
			nbr=(nbr-reste)/2;
			
			if(nbr==0)
				break;
		}

		/*
		for(int i=0; i<8; i++)
		{
			System.out.println("tabInt["+i+"] = "+tabInt[i]);
		}
		*/
		
		return tabInt;
	}
	
	//spécifique pour la partie 2 et 5
	
	public static int conversionEntier(int a, int b)
	{
		int resultat=0;
		
		int tab1[] = conversionByte(a);
		int tab2[] = conversionByte(b);
		
		int tab3[] = new int[8];
		
		for(int i=0; i<8 ; i++)
		{
			if(i<4)
				tab3[i]=tab2[3-i];
			else
				tab3[i]=tab1[11-i];	
			
			resultat+=tab3[i]*Math.pow(2, i);
		}
		
		//System.out.println("resultat : "+resultat);
		
		return resultat;
	}
	
}
