package hashlife;

import java.util.HashMap;

class MacroCell {
	
	//Map contenant toutes les cellules construites
	static final HashMap<Integer,MacroCell> cellules = new HashMap<Integer,MacroCell>(10000000,(float)0.1);	
	
	//Remise à zero de cette map
	static void reinitMap(){ cellules.clear();}
	
	
	
	//override du hashcode et de equals pour obtenir les égalités structurelles lors de la recherche dans la Map
	@Override
	public boolean equals(Object o){//O(1)
		if(n==2) {
			boolean res = (o instanceof MacroCell)&&((MacroCell) o).n==2;
			if(res){
				MacroCell m = (MacroCell) o;
				for(int i=0;i<4;i++){for(int j=0;j<4;j++){res = res&&(valeurs[i][j]==m.valeurs[i][j]);}}
				return res;}
		}
		return (o instanceof MacroCell)&&((MacroCell) o).n==n&&((MacroCell) o).hautGauche.adress()==hautGauche.adress()
				&&((MacroCell) o).hautDroit.adress()==hautDroit.adress()
				&&((MacroCell) o).basGauche.adress()==basGauche.adress()
				&&((MacroCell) o).basDroit.adress()==basDroit.adress();//egalite exacte des objets sous-cellule
	}
	
	@Override
	public int hashCode(){
		int res=0;
		if(n==2){
			for(int i=0;i<valeurs.length;i++){for(int j=0;j<valeurs[0].length;j++){if(valeurs[i][j]){res = (2*res)+1;} else {res = 2*res ;}}}
			return res;//max 65536
		}
		else{//plus que 0(1)?
			res = (hautGauche.adress() + hautDroit.adress()+basDroit.adress()+basGauche.adress());			
			if(res>=0&&res<65536)res = res+65536;
			while(cellules.containsKey(res)&&!this.equals(cellules.get(res))){res++;}//necessaire pour l'injectivité
			return res;
		}
	}
	
	private int adress(){
		return System.identityHashCode(this);
	}
	
	
	/*******************************************
	 * Structure de la MacroCell en elle-meme
	 *******************************************/
	
	int n;//MacroCell de taille 2^n*2^n
	
	//Les champs suivants ne sont utilisés que pour une cellule de taille 4x4 (n=2, cas le plus petit)
	//si n>2 ce champ ne représente rien
	boolean[][] valeurs;//tableau de taille 4x4 nécessairement
	
	//Champs pour cellule plus grande
	
	//4 sous-cellules au temps time
	MacroCell hautGauche;
	MacroCell hautDroit;
	MacroCell basGauche;
	MacroCell basDroit;
	
	//Ensemble des résultats aux temps 2^s, avec s<=n-2 - HashTable de taille n-1 au pire	
	HashMap<Integer,MacroCell> results;
	
	
	
	
	
	//Constructeurs
	
	//n=2
	MacroCell(boolean[][] vals){
		n=2;
		valeurs=vals;		
		hautGauche=null;hautDroit=null;basGauche=null;basDroit=null;
		results=new HashMap<Integer,MacroCell>(n);		
	}
	
	//n>2
	MacroCell(MacroCell hg, MacroCell hd,MacroCell bg, MacroCell bd){
		n=hg.n+1;
		valeurs=null;
		hautGauche=hg;hautDroit=hd;basGauche=bg;basDroit=bd;
		results=new HashMap<Integer,MacroCell>(n);
	}
	
	static MacroCell construct(boolean[][] vals){
		MacroCell m = new MacroCell(vals);
		int n = m.hashCode();
		if(cellules.containsKey(n)) {return cellules.get(n);}
		else{cellules.put(n, m);return m;}
	}
	
	static MacroCell construct(MacroCell hg, MacroCell hd,MacroCell bg, MacroCell bd){
		MacroCell m = new MacroCell(hg,hd,bg,bd);
		int n = m.hashCode();
		if(cellules.containsKey(n)) {return cellules.get(n);}
		else{cellules.put(n, m);return m;}
	}
	
	
	
	//fonctions principales
	
	//Conversion depuis une matrice de booleens(lecture)
	
	static MacroCell fromBoolean(boolean[][] tab){
		if(tab==null) return null;
		//copie de tab vers matrice carre 2^n*2^n
		int m = Math.max(tab.length, tab[0].length);
		int n = (int) (Math.log(m)/Math.log(2));
		if(Math.log(m)/Math.log(2)-n>0) n = n+1;
		int p=(int)Math.pow(2,n);
		//DEBUGSystem.out.println("creation of size "+p+" MacroCell");
		
		boolean[][] carre = new boolean[p][p];
		for(int i=0;i<p;i++){for(int j=0;j<p;j++){if(i<tab.length&&j<tab[0].length){carre[i][j]=tab[i][j];}else{carre[i][j]=false;}}}
		//DEBUGSystem.out.println("initOK");
		
		//result
		return fromBoolSub(carre,0,0,p);
	}
	
	private static MacroCell fromBoolSub(boolean[][] tab,int i,int j,int l){
		if(l==4){
			boolean[][] t = new boolean[4][4];
			for(int x =0;x<4;x++){for(int y =0;y<4;y++){t[x][y]=tab[i+x][j+y];}}
			return construct(t);
		}
		else{
			return construct(fromBoolSub(tab,i,j,l/2),fromBoolSub(tab,i,j+l/2,l/2),fromBoolSub(tab,i+l/2,j,l/2),fromBoolSub(tab,i+l/2,j+l/2,l/2));
		}
	}
	
	
	
	
	//Conversion vers une matrice de booleens (affichage)
	
	boolean[][] toBoolean(){
		int p = (int) Math.pow(2,n);
		boolean[][] res = new boolean[p][p];
		//DEBUGSystem.out.println("export of size "+p+" MacroCell");
		this.toBoolSub(res, 0, 0, p);
		return res;
	}
	
	private void toBoolSub(boolean[][] tab,int i,int j,int l){
		if(l==4){
			//DEBUGSystem.out.println("ecriture cell de base - n="+this.n);
			for(int x =0;x<4;x++){for(int y =0;y<4;y++){tab[i+x][j+y]=this.valeurs[x][y];}}
		}
		else{
			this.hautGauche.toBoolSub(tab,i,j,l/2);this.hautDroit.toBoolSub(tab,i,j+l/2,l/2);this.basGauche.toBoolSub(tab,i+l/2,j,l/2);this.basDroit.toBoolSub(tab,i+l/2,j+l/2,l/2);
		}	
	}
	
	
	//Doublement d'une MacroCell (rajout pourtour de cellules mortes)
	
	MacroCell doubler(){
		if(this.n==2){ return this.doublerSimpl();}
		else{
			MacroCell e = empty((this.n)-1);
			return construct(construct(e,e,e,this.hautGauche),construct(e,e,this.hautDroit,e),construct(e,this.basGauche,e,e),construct(this.basDroit,e,e,e));
		}
	}
	
	//not private because used in tests
	static MacroCell empty(int n){//construit les cellules vides si elles ne sont pas encore dans la table, en O(n)
		if(n==2){
			boolean[][] vide = new boolean[4][4];
			return construct(vide);
		}
		else{
			MacroCell m = empty(n-1);		
			return construct(m,m,m,m);
		}
	}
	
	private MacroCell doublerSimpl(){
		boolean[][] hg = new boolean[4][4];hg[2][2]=valeurs[0][0];hg[2][3]=valeurs[0][1];hg[3][2]=valeurs[1][0];hg[3][3]=valeurs[1][1];MacroCell hgc=construct(hg);
		boolean[][] hd = new boolean[4][4];hd[2][0]=valeurs[0][2];hd[2][1]=valeurs[0][3];hd[3][0]=valeurs[1][2];hd[3][1]=valeurs[1][3];MacroCell hdc=construct(hd);
		boolean[][] bg = new boolean[4][4];bg[0][2]=valeurs[2][0];bg[0][3]=valeurs[2][1];bg[1][2]=valeurs[3][0];bg[1][3]=valeurs[3][1];MacroCell bgc=construct(bg);
		boolean[][] bd = new boolean[4][4];bd[0][0]=valeurs[2][2];bd[0][1]=valeurs[2][3];bd[1][0]=valeurs[3][2];bd[1][1]=valeurs[3][3];MacroCell bdc=construct(bd);
		return construct(hgc,hdc,bgc,bdc);
	}
	
	
	//Simplification d'une MacroCell (si son pourtour est vide)
	
	
	
	
	MacroCell simplifier(){
		if(n==2) return this;
		if(n==3){//cas le plus petit ou la simplification est possible
			MacroCell e2 = empty(n-1);
			if(this.hautGauche.equals(e2)&&this.basGauche.equals(e2)&&this.basDroit.equals(e2)) {return this.hautDroit.simplifier();}
			if(this.hautGauche.equals(e2)&&this.basGauche.equals(e2)&&this.hautDroit.equals(e2)) {return this.basDroit.simplifier();}
			if(this.hautGauche.equals(e2)&&this.hautDroit.equals(e2)&&this.basDroit.equals(e2)) {return this.basGauche.simplifier();}
			if(this.hautDroit.equals(e2)&&this.basGauche.equals(e2)&&this.basDroit.equals(e2)) {return this.hautGauche.simplifier();}
			return this.simplSmall();	
		}
		MacroCell e2 = empty(n-1);
		if(this.hautGauche.equals(e2)&&this.basGauche.equals(e2)&&this.basDroit.equals(e2)) {return this.hautDroit.simplifier();}
		if(this.hautGauche.equals(e2)&&this.basGauche.equals(e2)&&this.hautDroit.equals(e2)) {return this.basDroit.simplifier();}
		if(this.hautGauche.equals(e2)&&this.hautDroit.equals(e2)&&this.basDroit.equals(e2)) {return this.basGauche.simplifier();}
		if(this.hautDroit.equals(e2)&&this.basGauche.equals(e2)&&this.basDroit.equals(e2)) {return this.hautGauche.simplifier();}
		MacroCell e = empty(n-2);
		if(this.hautGauche.hautGauche.equals(e)&&this.hautGauche.hautDroit.equals(e)&&this.hautDroit.hautGauche.equals(e)
					&&this.hautDroit.hautDroit.equals(e)&&this.hautDroit.basDroit.equals(e)&&this.basDroit.hautDroit.equals(e)
					&&this.basDroit.basDroit.equals(e)&&this.basDroit.basGauche.equals(e)&&this.basGauche.basDroit.equals(e)
					&&this.basGauche.basGauche.equals(e)&&this.basGauche.hautGauche.equals(e)&&this.hautGauche.basGauche.equals(e)){
			return this.centre().simplifier();}	
		return this;//il ne se passe rien
	}

	
	private MacroCell simplSmall(){
		boolean res=true;//Degueu mais pas trop le choix :(
		for(int i=0;i<4;i++){for(int j=0;j<2;j++){res = res&&!hautGauche.valeurs[i][j];}}
		for(int i=0;i<2;i++){for(int j=2;j<4;j++){res = res&&!hautGauche.valeurs[i][j];}}
		for(int i=0;i<2;i++){for(int j=0;j<4;j++){res = res&&!hautDroit.valeurs[i][j];}}
		for(int i=2;i<4;i++){for(int j=2;j<4;j++){res = res&&!hautDroit.valeurs[i][j];}}
		for(int i=2;i<4;i++){for(int j=0;j<4;j++){res = res&&!basDroit.valeurs[i][j];}}
		for(int i=0;i<2;i++){for(int j=2;j<4;j++){res = res&&!basDroit.valeurs[i][j];}}
		for(int i=2;i<4;i++){for(int j=0;j<4;j++){res = res&&!basGauche.valeurs[i][j];}}
		for(int i=0;i<2;i++){for(int j=0;j<2;j++){res = res&&!basGauche.valeurs[i][j];}}
		if(res){return this.centre();}
		else{return this;}
	}
	
	
	private MacroCell centre(){
		if(n==3){
			boolean[][] t = new boolean[4][4];
			t[0][0]=hautGauche.valeurs[2][2];t[0][1]=hautGauche.valeurs[2][3];t[1][0]=hautGauche.valeurs[3][2];t[1][1]=hautGauche.valeurs[3][3];
			t[0][2]=hautDroit.valeurs[2][0];t[0][3]=hautDroit.valeurs[2][1];t[1][2]=hautDroit.valeurs[3][0];t[1][3]=hautDroit.valeurs[3][1];
			t[2][0]=basGauche.valeurs[0][2];t[2][1]=basGauche.valeurs[0][3];t[3][0]=basGauche.valeurs[1][2];t[3][1]=basGauche.valeurs[1][3];
			t[2][2]=basDroit.valeurs[0][0];t[2][3]=basDroit.valeurs[0][1];t[3][2]=basDroit.valeurs[1][0];t[3][3]=basDroit.valeurs[1][1];
			return construct(t);
		}
		else{
			return construct(hautGauche.basDroit,hautDroit.basGauche,basGauche.hautDroit,basDroit.hautGauche);
		}		
	}
	
	
	
	
	//CALCUL DU RESULTAT
	
	MacroCell resultat(int s,int[][] rule){//calcul du résultat au laps de temps 2^s, avec s<=(n-2)
		if(this.results.containsKey(s)) return this.results.get(s);//le resultat voulu était déjà calculé
		
		//le cas de base est n=3, car le resultat doit etre une MacroCell
		//si s=0 le traitement est différent (résulat dans un pas de temps!)
		if(s==0){
			if(this.n==3){
				boolean[][] actuel = new boolean[6][6];
				for(int i=1;i<7;i++){
					for(int j=1;j<7;j++){
						if(i<4&&j<4) actuel[i-1][j-1]=this.hautGauche.valeurs[i][j];
						if(i<4&&j>=4) actuel[i-1][j-1]=this.hautDroit.valeurs[i][j-4];
						if(i>=4&&j<4) actuel[i-1][j-1]=this.basGauche.valeurs[i-4][j];
						if(i>=4&&j>=4) actuel[i-1][j-1]=this.basDroit.valeurs[i-4][j-4];
					}
				}
				boolean[][] suivant = NaiveLife.etatSuivant(actuel, rule);
				boolean[][] res = new boolean[4][4];
				for(int i=0;i<4;i++){for(int j=0;j<4;j++){res[i][j]=suivant[i+2][j+2];}}
				MacroCell resultat =construct(res);
				this.results.put(0, resultat);//on memorise le resultat pour ne pas le recalculer
				return resultat;
			}			
			else{
				MacroCell rhg = hautGauche.resultat(0, rule);
				MacroCell rhd = hautDroit.resultat(0, rule);
				MacroCell rbg = basGauche.resultat(0, rule);
				MacroCell rbd = basDroit.resultat(0, rule);
				MacroCell hCross = construct(hautGauche.hautDroit,hautDroit.hautGauche,hautGauche.basDroit,hautDroit.basGauche).resultat(0, rule);
				MacroCell gCross = construct(hautGauche.basGauche,hautGauche.basDroit,basGauche.hautGauche,basGauche.hautDroit).resultat(0, rule);
				MacroCell dCross = construct(hautDroit.basGauche,hautDroit.basDroit,basDroit.hautGauche,basDroit.hautDroit).resultat(0, rule);
				MacroCell bCross = construct(basGauche.hautDroit,basDroit.hautGauche,basGauche.basDroit,basDroit.basGauche).resultat(0, rule);
				MacroCell mCross = construct(hautGauche.basDroit,hautDroit.basGauche,basGauche.hautDroit,basDroit.hautGauche).resultat(0, rule);
				MacroCell resultat = construct(construct(rhg,hCross,gCross,mCross).centre(),construct(hCross,rhd,mCross,dCross).centre(),
						construct(gCross,mCross,rbg,bCross).centre(),construct(mCross,dCross,bCross,rbd).centre()
						);
				this.results.put(0, resultat);
				return resultat;
			}
		}
		else{//tjs cas de base n==3
			if(n==3){//forcément s=1 car s>0 et s<=n-2==1
				boolean[][] actuel = new boolean[8][8];
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++){
						if(i<4&&j<4) actuel[i][j]=this.hautGauche.valeurs[i][j];
						if(i<4&&j>=4) actuel[i][j]=this.hautDroit.valeurs[i][j-4];
						if(i>=4&&j<4) actuel[i][j]=this.basGauche.valeurs[i-4][j];
						if(i>=4&&j>=4) actuel[i][j]=this.basDroit.valeurs[i-4][j-4];
					}
				}
				boolean[][] suivant = NaiveLife.etatSuivant(NaiveLife.etatSuivant(actuel, rule),rule);
				boolean[][] res = new boolean[4][4];
				for(int i=0;i<4;i++){for(int j=0;j<4;j++){res[i][j]=suivant[i+4][j+4];}}
				MacroCell resultat =construct(res);
				this.results.put(1, resultat);
				return resultat;
			}
			else{
				MacroCell rhg = hautGauche.resultat(s-1, rule);
				MacroCell rhd = hautDroit.resultat(s-1, rule);
				MacroCell rbg = basGauche.resultat(s-1, rule);
				MacroCell rbd = basDroit.resultat(s-1, rule);
				MacroCell hCross = construct(hautGauche.hautDroit,hautDroit.hautGauche,hautGauche.basDroit,hautDroit.basGauche).resultat(s-1, rule);
				MacroCell gCross = construct(hautGauche.basGauche,hautGauche.basDroit,basGauche.hautGauche,basGauche.hautDroit).resultat(s-1, rule);
				MacroCell dCross = construct(hautDroit.basGauche,hautDroit.basDroit,basDroit.hautGauche,basDroit.hautDroit).resultat(s-1, rule);
				MacroCell bCross = construct(basGauche.hautDroit,basDroit.hautGauche,basGauche.basDroit,basDroit.basGauche).resultat(s-1, rule);
				MacroCell mCross = construct(hautGauche.basDroit,hautDroit.basGauche,basGauche.hautDroit,basDroit.hautGauche).resultat(s-1, rule);
				//on a sauté de 2^(s-1)
				MacroCell resultat = construct(construct(rhg,hCross,gCross,mCross).resultat(s-1,rule),construct(hCross,rhd,mCross,dCross).resultat(s-1,rule),
						construct(gCross,mCross,rbg,bCross).resultat(s-1,rule),construct(mCross,dCross,bCross,rbd).resultat(s-1,rule)
						);
				//et encore de 2^(s-1), donc de 2^s
				this.results.put(s, resultat);
				return resultat;
			}						
		}
	}
	
	
	
	
	
	
}
