package hashlife;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.Math;


class RleReader {
	
	private static int[] decBaseDix(int n){
		int[] res = new int[((int)Math.log10((double)n))+1];
		int val=n;
		for(int i=0;i<res.length;i++){
			res[i]= (int) (val/Math.pow(10.0,((int)Math.log10((double)val))));
			val=val-(res[i]*(int)Math.pow(10.0,((int)Math.log10((double)val))));
		}
		return res;
	}
	
	
	static int[][] rule(String inputFileName){
		try {
			if(!inputFileName.endsWith(".rle")) Console.printConsole("ERREUR - Mauvais format de fichier");			
			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName),"UTF-8"));	
			String currentString = b.readLine();
			while(currentString.startsWith("#")){currentString=b.readLine();}
			String[] def = currentString.split(",");
			int B = Integer.parseInt(def[2].split("B")[1].split("/S")[0]);//born
			int S = Integer.parseInt(def[2].split("B")[1].split("/S")[1]);//stay
			int[][] res = {decBaseDix(B),decBaseDix(S)};
			//regle representŽe par une matrice d'entiers; une ligne pour les naissances, la deuxime pour la survie; chaque ligne prŽsente plusieurs possibilitŽs
			return  res;
		}
		catch(Exception e){Console.printConsole("ERREUR! - fichier illisible; rgle standard appliquŽe\n"); int[][] res = {{3},{2,3}};return res;}
	}
	
	
	static boolean[][] readFile(String inputFileName){		
		try {
			if(!inputFileName.endsWith(".rle")) Console.printConsole("ERREUR - Mauvais format de fichier");			
			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName),"UTF-8"));			
			String currentString = b.readLine();
			while(currentString.startsWith("#")){
				currentString=b.readLine();
				Console.printConsole(currentString);
			}
			String[] def = currentString.split(",");
			int colonnes  = Integer.parseInt((def[0].split("= "))[1]);
			int lignes  = Integer.parseInt((def[1].split("= "))[1]); Console.printConsole("cols:"+colonnes+", lignes:"+lignes);
			
			boolean[][] res = new boolean[lignes][colonnes];
			int ligne=0,colonne=0;
			int currentInt=0;
			
			int currentChar;
			while(ligne<lignes){
				currentChar=b.read();
				if(currentChar <= '9' && currentChar >= '0'){ currentInt = 10 * currentInt+ (currentChar - '0');}
				if(currentChar=='o'||currentChar=='b'){
					if(currentInt==0) {res[ligne][colonne]=(currentChar=='o'); colonne++;}
					else{
						for(int i=colonne;i<colonne+currentInt&&i<colonnes;i++){
							res[ligne][i]=(currentChar=='o');
						}
						colonne=colonne+currentInt;
						currentInt=0;
					}
				}
				if(currentChar=='$'){if (currentInt==0) ligne++; else {ligne=ligne+currentInt;currentInt=0;} colonne=0;}		
				if(currentChar=='!') ligne=lignes;
			}
			Console.printConsole("");
			return res;
		}
		catch(Exception e){Console.printConsole("ERREUR! - fichier illisible\n");return null;}
	}
	
	
}
