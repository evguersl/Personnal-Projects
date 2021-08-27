package qrcode;

import java.nio.charset.StandardCharsets;

import reedsolomon.ErrorCorrectionEncoding;


//L'exemple de l'annexe B n'est pas très clair :(pour -128) 
//1111 1111 1111 1111 1111 1111 1000 0000 ??? par défaut les bytes de poid fort se mettent à 1, car le nbr est négatif

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
		
		int maxLength = QRCodeInfos.getMaxInputLength(version);
		int finalLength = QRCodeInfos.getCodeWordsLength(version);
		int eccLength= QRCodeInfos.getECCLength(version);
		
		int chaine1[];
		int chaine2[];
		int chaine3[];
		int chaine4[];
		
		
		chaine1 = encodeString(input, maxLength);
		System.out.println();
		chaine2 = addInformations(chaine1);
		System.out.println();
		chaine3 = fillSequence(chaine2, finalLength);
		System.out.println();
		chaine4 = addErrorCorrection(chaine3, eccLength);
		System.out.println();
		
		return bytesToBinaryArray(chaine4);
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	
	
	
	//==========================================================================================================================
	//		Conversion d'une String en int[] avec le code ISO_8859_1 pour chaque caractères (attention conversion byte/int !!!)
	//==========================================================================================================================
	
	
	
	public static int[] encodeString(String input, int maxLength) {
		int tailleTab = 0;
		
		if(input.length()<=maxLength)
		{
			tailleTab = input.length();
		}
		else
		{
			System.out.println("The length is of the encoding is not correct");
			System.exit(1);
		}
		
		byte tabByte[] = new byte[tailleTab];
		int tabInt[] = new int[tailleTab];
		
		
		tabByte=input.getBytes(StandardCharsets.ISO_8859_1);
		
		
		
		for(int i=0; i<tailleTab;i++) {
			tabInt[i]=tabByte[i] & 0xFF;
			//System.out.println(tabInt[i]); //test pour code
		}
		
		return tabInt;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	
	
	//==============================================================================================
	//		En manipulant les opérateurs binaires, on construit le int[] demandé (décalage et | )
	//==============================================================================================
	
	
	
	public static int[] addInformations(int[] inputBytes) {
		int taille = inputBytes.length;
		
		int tabInt[] = new int[taille+2];
		
		if(taille==0)
		{
			tabInt[0]=64;
			tabInt[1]=80;
			
			return tabInt;
		}
		
		for(int i=0; i<taille+2;i++)
		{			
			if(i==0)
			{
				tabInt[i]=ManipulationByte.conversionEntier(4,taille);
			}
			else if(i==1)
			{
				tabInt[i]=ManipulationByte.conversionEntier(taille,inputBytes[i-1]);
			}
			else if(i==taille+1)
			{
				tabInt[i]=ManipulationByte.conversionEntier(inputBytes[taille-1],0);
			}
			else
			{
				tabInt[i]=ManipulationByte.conversionEntier(inputBytes[i-2],inputBytes[i-1]);
			}
			//System.out.println("tabInt["+i+"] = "+tabInt[i]); //test pour code
		}
		
		
		
		return tabInt;
	}

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	
	
	//==============================================================================================
	//						Rajoute 236 ou 17 à la fin de la chaîne 
	//==============================================================================================
	
	
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		
		if(finalLength<=encodedData.length)
		{
			return encodedData;
		}
		else
		{
			int newTab[] = new int[finalLength];
			
			for(int i=0;i<finalLength;i++)
			{
				if(i<encodedData.length)
				{
					newTab[i]=encodedData[i];
				}
				else if(newTab[i-1]!=236)
				{
					newTab[i]=236;
				}
				else
				{
					newTab[i]=17;
				}
			//	System.out.println("newTab["+i+"] : "+newTab[i]); //test pour code
			}
			
			return newTab;
			
		}
	}

	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	
	//===================================================================================================
	//		Méthode pour concaténer nos données précédentes avec les octets de correction
	//===================================================================================================
	
	
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		
		int tailleTab = encodedData.length+eccLength;
		int tabFinal[] = new int[tailleTab];
		int tabCorrection[] = new int[eccLength];
		int j=0;
		
		tabCorrection=ErrorCorrectionEncoding.encode(encodedData,eccLength);
		
		for(int i=0;i<tailleTab;i++)
		{
			if(i<encodedData.length)
			{
				tabFinal[i]=encodedData[i];
			}
			else
			{
				tabFinal[i]=tabCorrection[j];
				j++;
			}
			
			//System.out.println("tabFinal["+i+"] : "+tabFinal[i]); //test pour code
		}
		
		return tabFinal;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	
	//==============================================================================================
	//			Conversion int en boolean 
	//==============================================================================================
	
	
	public static boolean[] bytesToBinaryArray(int[] data) {
		int taille = data.length*8;
		boolean tabBol[] = new boolean[taille];
		int tabInter[] = new int[8];
		
		
		for(int i=0; i<data.length; i++)
		{

			tabInter=ManipulationByte.conversionByte(data[i]);
			for(int j=0; j<8; j++)
			{
				int compteur=8*i+j;
				
				if(tabInter[j]==1)
				{
					tabBol[compteur]=true;
				}
				else
				{
					tabBol[compteur]=false;
				}
				
				
				
				//System.out.println("tabBol["+compteur+"] = "+tabBol[compteur]);
			}	
		}
			 
		return tabBol;
	}

}
