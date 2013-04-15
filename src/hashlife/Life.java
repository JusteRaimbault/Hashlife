package hashlife;

import java.awt.Color;
import java.util.LinkedList;



public class Life {
	
	/******************************************************
	 * MAIN CLASS - RUN DIFFERENTS LIFE MODES
	 * 
	 * -- main class with static parameters and methods --
	 * 
	 ******************************************************/
	
	//Running Parameters
	static String currentFile;//fichier courant de configuration initiale
	static int[][] rule;
	static boolean inHashMode = true;//utilisation de l'algorithme hashlife - par défault
	static String mode = "HashLife";
	static int targetTime = 0;//temps visé lors du run
	static int intervalle = 0;//vitesse d'iteration
	static long intTime = 100;//waiting time entre 2 configs en ms
	static boolean launch = false;//launch runLife when true
	static boolean exit = false;//exit when true
	
	static boolean[][] currentState;//etat de la grille sous forme de booleans
	static MacroCell currentCell;//etat en MacroCell
	
	//Drawing Parameters
	static Color background = Color.BLACK;
	static Color foreground = Color.GREEN;
	static float cellSize=2;//taille d'une cellule en pixels
	
	
	static void updateInitConfig(boolean needToClearMap){
		currentState = RleReader.readFile("rlefiles/"+currentFile);
		rule=RleReader.rule("rlefiles/"+currentFile);
		currentCell = MacroCell.fromBoolean(currentState);
		Console.printConsole("Configuration represented by a size "+currentCell.n+" MacroCell");
		if(needToClearMap) {MacroCell.reinitMap();Console.printConsole("HashMap of Cells reinitialized...");}
		if(!checkDrawingSize()) cellSize = 1;
		Affichage.MAINWINDOW.repaint();
	}
	
	static void chgeMode(){
		inHashMode = !inHashMode;
		if(inHashMode) mode="HashLife"; else mode = "Standard";
		Affichage.MAINWINDOW.repaint();
	}
	
	static void runNaive() throws InterruptedException{
		for(int t=0;t<targetTime;t=t+intervalle){
			double time = System.currentTimeMillis();
			currentState = NaiveLife.naiveLife(currentState, rule, intervalle);
			Image.IMAGE.repaint();
			Console.printConsole("saut de "+intervalle+" pas de temps");
			Console.printConsole("Temps de calcul : "+(System.currentTimeMillis()-time));
			Thread.sleep(intTime);
		}
		launch = false;
	}
	
	static boolean checkDrawingSize(){
		if(currentState.length>Image.IMAGE.getHeight()||currentState[0].length>Image.IMAGE.getWidth()){
			cellSize=Math.min((float)Image.IMAGE.getHeight()/(float)currentState.length, (float)Image.IMAGE.getWidth()/(float)currentState[0].length);
			Console.printConsole("Image sortait de l'écran--taille des cellules diminuées automatiquement\n"); return true;}
		return false;
	}
	
	static int[] puissancesDeux(int t){
		LinkedList<Integer> l = new LinkedList<Integer>();
		int val=t;
		while(val>0){
			int p = (int) (Math.log((double)val)/Math.log(2));
			val=val- (int) Math.pow(2.0, (double)p);
			l.addLast(p);
		}
		int[] res = new int[l.size()];
		for(int i=0;i<res.length;i++){res[i] = l.removeFirst().intValue();}
		return res;
	}
	
	static boolean[][] hashLife(MacroCell start, int[][] rule,int t){
		int[] p = puissancesDeux(t);
		for(int k:p){
			MacroCell big = currentCell.doubler().doubler();//rien ne peut en sortir
			while(k>big.n-2) big = big.doubler();//réglage de la taille pour le bon saut temporel
			Console.printConsole("Saut de 2^"+k+" in hashLife");
			double calctime = System.currentTimeMillis();
			Console.printConsole("Calcul du résultat d'une MacroCell de taille "+big.n);
			MacroCell result = big.resultat(k, rule);
			Console.printConsole("Resultat de taille "+result.n);
			currentCell = result.simplifier(); Console.printConsole("MacroCell renvoyée de taille "+currentCell.n);
			Console.printConsole("Result computed in a time of "+(System.currentTimeMillis()-calctime)+" ms...");
			Console.printConsole(MacroCell.cellules.size()+" Cells in Map\n");
		}
		return currentCell.toBoolean();
	}
	
	static void runHashLife() throws InterruptedException{
		try{
		for(int t=0;t<targetTime;t=t+intervalle){
			Console.printConsole("saut de "+intervalle+" pas de temps in hashLife Mode");
			currentState = hashLife(currentCell, rule, intervalle);
			Image.IMAGE.repaint();
			Thread.sleep(intTime);
		}
		}catch(OutOfMemoryError e){Console.printConsole("MEMORY ERROR!! Java heap Space");Console.printConsole("Augmenter la taille de la mémoire");
		Console.printConsole("(Java VM Arguments -Xmx+Memory)");}
		finally{launch = false;}
	}
	
	
	static void runLife() throws InterruptedException{
		if(!inHashMode) runNaive();
		else runHashLife();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Affichage.initWindow();
		while(!exit){
			if(launch) runLife();
		}
		System.exit(0);
	}

}
