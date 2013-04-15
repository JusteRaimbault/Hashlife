package hashlife;

class NaiveLife {

	
	//algorithme naif du jeu de la vie pour tester la correction du Hashlife sur des petites valeurs temporelles
		//complexité en O(t*n^2)
		
		private static int vivants(boolean[][] actuel,int i,int j){
			//requires i \in [|1;h-1|], j \in [|1;w-1|] (actuel "elargi")
			int count=0;
			for(int k=0;k<3;k++){if (actuel[i-1][j-1+k]) count++;}
			if (actuel[i][j-1]) count++; if (actuel[i][j+1]) count++;
			for(int k=0;k<3;k++){if (actuel[i+1][j-1+k]) count++;}
			return count;
		}
		
		static boolean[][] etatSuivant(boolean[][] actuel,int[][] rule){
			int largeur = actuel[0].length, hauteur = actuel.length;
			boolean[][] actuelElargi = new boolean[hauteur+4][largeur+4];//afin d'eviter effets de bord
			boolean[][] res = new boolean[hauteur+2][largeur+2];//on n'avance que d'un pas
			for(int i =2;i<actuelElargi.length-2;i++){for(int j =2;j<actuelElargi[0].length-2;j++){actuelElargi[i][j]=actuel[i-2][j-2];}}//copie
			
			for(int i=0;i<res.length;i++){
				for(int j=0;j<res[0].length;j++){
					int viv = vivants(actuelElargi,i+1,j+1);
					//DEBUG System.out.print("i="+i+" j="+j+" vivants="+viv);
					boolean mq=false;
					for(int s=0;s<rule[1].length;s++){if(viv==rule[1][s]) {res[i][j]=actuelElargi[i+1][j+1];mq=true;}}//stay
					for(int b=0;b<rule[0].length;b++){if(viv==rule[0][b]) {res[i][j]=true;mq=true;}}//born
					if(!mq) res[i][j]=false;//die
				}
			}
			return res;
		}
		
		static boolean[][] naiveLife(boolean[][] start,int[][] rule,int time){
			boolean[][] current = start.clone();
			for(int t=0;t<time;t++){
				current = etatSuivant(current,rule);
			}
			return current;
		}
	
	
	
	
	
}
