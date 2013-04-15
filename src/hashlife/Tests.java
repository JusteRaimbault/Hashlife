package hashlife;

import org.junit.Test;


public class Tests {
	/*
	@Test
	public void testempty(){
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/rlefiles/puffer.rle");
		int[][] rule = {{3},{2,3}};
		MacroCell inter = MacroCell.fromBoolean(img);
		MacroCell m =inter;
		MacroCell e = MacroCell.empty(8);
		MacroCell m2 = MacroCell.construct(m,e,e,e).doubler().doubler();
		MacroCell r = m2.resultat(10, rule);
		System.out.println(r.n);
		System.out.println(r.simplifier().n);
	}
	
	
	
	/*
	@Test
	public void testsimpl(){
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/rlefiles/glider.rle");
		MacroCell inter = MacroCell.fromBoolean(img);
		MacroCell e1 = inter.doubler().doubler().doubler();
		System.out.println(e1.n+" "+inter.n);
		//MacroCell e2 = MacroCell.empty(e1.n);
		System.out.println(e1.equals(inter));
	}
	
	
	
	
	
	//champs pour calcul du temps dans les fonctions (verif complexité)
	static int countCells =0;
	static double constructTime=0.0;
	static double constructTimeBis=0.0;

	/*
	//Test clé: comparaison du naive life et HashLife
	@Test
	public void testHashLife() throws InterruptedException{
		double t=System.currentTimeMillis();
		MacroCell.reinitMap();
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/puffer.rle");
		int[][] rule = RleReader.rule("/Users/Juste/Documents/workspace/Hashlife/puffer.rle");
		MacroCell inter = MacroCell.fromBoolean(img).doubler().doubler().doubler().doubler();
		System.out.println(inter.n);
		Affichage fenetre1 = new Affichage("temps initial",0,0,500,500,img);
		boolean[][] hlife = inter.resultat(10, rule).simplifier().toBoolean();//ce que Puffer est dans 64
		Affichage fenetre2 = new Affichage("result with Hashlife",500,0,500,500,hlife);
		//Affichage fenetre3 = new Affichage("resultat recalculé",0,500,500,500,inter.resultat(9, rule).simplifier().toBoolean());
		System.out.println(countCells+" Cells required, "+MacroCell.cellules.size()+" Cells in Map");
		System.out.println((System.currentTimeMillis()-t)+" ms ellapsed...");
		System.out.println("Construct time:"+constructTime+" ms");
		System.out.println("time dans hashcode:"+constructTimeBis+" ms");
		Thread.sleep(10000);
	}*/
	
	
	/*
	//Test doublage et simplification d'une MacroCell
	@Test
	public void testDoubl() throws InterruptedException{
		double t = System.currentTimeMillis();
		MacroCell.reinitMap();
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/puffer.rle");
		Affichage fenetre1 = new Affichage("original",0,0,200,200,img);
		MacroCell inter = MacroCell.fromBoolean(img);		
		Affichage fenetre2 = new Affichage("converti",0,300,200,200,inter.toBoolean());
		MacroCell d = inter.doubler().doubler();
		System.out.println("cellSize="+inter.n+" dSize="+d.n);
		Affichage fenetre3 = new Affichage("doublé",300,0,500,500,d.toBoolean());
		Affichage fenetre4 = new Affichage("simplifié",300,300,300,300,d.simplifier().toBoolean());
		System.out.println(countCells+" Cells required, "+MacroCell.cellules.size()+" Cells in Map");
		System.out.println((System.currentTimeMillis()-t)+" ms ellapsed...");
		System.out.println("Construct time:"+constructTime+" ms");
		System.out.println("time dans hashcode:"+constructTimeBis+" ms");
		Thread.sleep(30000);	
	}*/
	
	/*
	//Test conversion from et to boolean
	@Test
	public void testBoolConv() throws InterruptedException{
		MacroCell.reinitMap();
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/puffer.rle");
		Affichage fenetre1 = new Affichage("original",0,0,200,200,img);
		MacroCell inter = MacroCell.fromBoolean(img);
		System.out.println("import reussi, "+MacroCell.cellules.size()+" cells in Map");
		boolean[][] res = inter.toBoolean();
		Affichage fenetre2 = new Affichage("converti",0,300,200,200,res);
		Thread.sleep(5000);	
	}
	*/
	
	/*
	@Test
	public void testObjAdress(){
		/*int[] t1 = new int[4];
		boolean[] t2 = new boolean[3];
		int[] t3 = t1.clone();
		int n=2;
		System.out.println(t1.toString());		
		System.out.println(t2.toString());
		System.out.println(t3.toString());
		System.out.println(((Integer) n).toString());
		//toString renvoie: type@adresse en hexadécimal
		//testhexa
		System.out.println(Integer.parseInt("d7725c4", 16));
		System.out.println(Integer.MAX_VALUE);
		int m=Integer.parseInt("fffffff",16);
		System.out.println(1+m+(m*m)+(m*m*m));
		//hashcode??(add1,add2,add3,add4) avec add<fffffff
		m = (int) Math.pow(2, 5)+1;
		System.out.println((int) (Math.log(m)/Math.log(2)));
		System.out.println(Integer.parseInt("ffff3d20", 16));
	}*/
	
	/*
	//test Affichage
	@Test(timeout=5000)
	public void testWindow() throws InterruptedException{
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/glider.rle");
		Affichage fenetre = new Affichage("testaff",0,0,700,1000,img);
		while(true){}
	}*/
	
	//test Naivelife
	
	/*
	@Test(timeout=100000)
	public void testNaive() throws InterruptedException{
	    boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/rlefiles/loadingdock.rle");
		int[][] rule = {{3},{2,3}};
		
		Affichage fenetre = new Affichage("testaff",0,0,2000,1000,img);
		Thread.sleep(10000);
		//Thread.sleep(2000);
		boolean[][] current = img;
		for(int t=0;t<1000;t++){
			fenetre.evolve(current);
			current = NaiveLife.etatSuivant(current,rule);
			Thread.sleep(20);
		}
	}
	
	
	//tests du RleReader
	
	//OK
	/*
	@Test
	public void testNewFiles(){
		RleReader.testFile("/Users/Juste/Documents/workspace/Hashlife/zweiback.rle");
	}*/
	
	/*
	@Test
	public void testFile(){
		boolean[][] img = RleReader.readFile("/Users/Juste/Documents/workspace/Hashlife/puffer.rle");
		for(int i =0;i<img.length;i++){
			for(int j=0;j<img[0].length;j++){
				System.out.print(img[i][j]+" ");
			}
			System.out.println();
		}	
	}*/

	/*
	@Test
	public void testRule(){
		int[][] rule = RleReader.rule("/Users/Juste/Documents/workspace/Hashlife/glider.rle");
		for(int i =0;i<rule.length;i++){
			for(int j=0;j<rule[i].length;j++){
				System.out.print(rule[i][j]+" ");
			}
			System.out.println();
		}	
	}*/
	
}
