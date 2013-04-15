package hashlife;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;

class Affichage extends JFrame {//instanceof Affichage is the main window
	
	private static final long serialVersionUID = 1L;
	
	private static final Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
	
	static final Affichage MAINWINDOW = new Affichage("Le Jeu de la Vie");
	
	@SuppressWarnings("deprecation")
	static void initWindow(){MAINWINDOW.show(); }

	Affichage(String title){
		super(title);
		setSize(screenSize.width,screenSize.height);
		setLocation(0,0);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    System.out.println(System.setProperty("swing.aatext", "true"));    
	    setJMenuBar(createMenu());
	    this.add("North",createChoicePanel());
		this.add(Image.IMAGE);//le composant est dessiné à travers l'UI quand il est ajouté
	    validateTree();
		setVisible(true);
	}
	
	private static JMenuBar createMenu(){
		JMenuBar menuBar = new JMenuBar();
	    JMenu mFichier = new JMenu("Fichier");
	    menuBar.add(mFichier);
	    JMenu mOptions = new JMenu("Options");
	    menuBar.add(mOptions);
	    JMenuItem itemFile = new JMenuItem("Set new initial configuration");
	    mFichier.add(itemFile);
	    itemFile.addActionListener(new ActionListener() {      
	      public void actionPerformed(ActionEvent e) {
	    	  choixFichier();
	      }
	    });    
	    
	    JMenuItem itemQuit = new JMenuItem("Quit");
	    mFichier.add(itemQuit);
	    itemQuit.addActionListener(new ActionListener() {      
	      public void actionPerformed(ActionEvent e) {
	        Life.exit = true;
	      }
	    });
	    
	    JMenuItem itemMode = new JMenuItem("Change Life algorithm");
	    mOptions.add(itemMode);
	    itemMode.addActionListener(new ActionListener() {      
	      public void actionPerformed(ActionEvent e) {
	    	  Life.chgeMode();
	      }
	    });    
	    
	    JMenuItem itemConsole = new JMenuItem("Open/Close console");
	    mOptions.add(itemConsole);
	    itemConsole.addActionListener(new ActionListener() {      
	      public void actionPerformed(ActionEvent e) {
	    	  //adapter taille écran selon hardware
	    	  if(Console.CONSOLE.isVisible()){
	    	  MAINWINDOW.setSize(screenSize.width,screenSize.height);
	    	  Console.CONSOLE.setVisible(false);}
	    	  else{MAINWINDOW.setSize(3*screenSize.width/4,screenSize.height);
	    	  		Console.CONSOLE.setLocation(3*screenSize.width/4,0);
	    	  		Console.CONSOLE.setVisible(true);}
	      }
	    });
	    
	    JMenuItem itemDraw = new JMenuItem("Drawing options...");
	    mOptions.add(itemDraw);
	    itemDraw.addActionListener(new ActionListener() {      
	      public void actionPerformed(ActionEvent e) {
	    	  drawingOptions();
	      }
	    });  
	    
		return menuBar;
	}
	
	private static void drawingOptions(){
		final JFrame fenetrechoix = new JFrame("Options de dessin");
  	  	fenetrechoix.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  	  	fenetrechoix.setVisible(true);
  	  	fenetrechoix.setSize(370, 250);
  	  	fenetrechoix.setLocation(100, 100);
  	  	
  	  	JPanel panel = new JPanel();
  	  	panel.setBackground(Color.white);
		fenetrechoix.getContentPane().add(panel);
		Color[] couleurs = {Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.PINK,Color.ORANGE,Color.RED,Color.WHITE,Color.YELLOW};
		final JComboBox box = new JComboBox(couleurs);
		JTextArea back = new JTextArea("Couleur Background");
		back.setVisible(true);panel.add(back);
		panel.add(box);
		box.setSelectedItem(Life.background);
		panel.add(new JTextArea("             "));
		panel.setVisible(true);
		panel.setLocation(0, 0);
		
		JTextArea fore = new JTextArea("Couleur Foreground");
		fore.setVisible(true);panel.add(fore);
		final JComboBox box2 = new JComboBox(couleurs);
		panel.add(box2);
		box2.setSelectedItem(Life.foreground);
		panel.setVisible(true); 
		
		
		JTextArea size = new JTextArea("Taille d'une cellule en pixels: ");
		panel.add(size);
		size.setVisible(true);
		final JTextField s = new JTextField(((Float)Life.cellSize).toString(),5);
		panel.add(s);
		s.setVisible(true);  
		
		JTextArea sleep = new JTextArea("Temps d'attente entre deux configs sucessives (ms): ");
		panel.add(sleep);
		sleep.setVisible(true);
		final JTextField sleepchp = new JTextField(((Long)Life.intTime).toString(),5);
		panel.add(sleepchp);
		sleepchp.setVisible(true);  
		
		
		
	    JButton valide = new JButton("OK");
	    valide.addActionListener(new ActionListener() {        
	        public void actionPerformed(ActionEvent e) {
	        	try{
	        		float taille = Float.parseFloat(s.getText());
	        		long sleeptime = Long.parseLong(sleepchp.getText());
	        		Life.cellSize = taille;
	        		Life.intTime = sleeptime;
	        	} catch(Exception ex){Life.cellSize = 1;Life.intTime= 100;}
	        	finally{
	        		Life.background = (Color)box.getSelectedItem();
	        		Life.foreground = (Color)box2.getSelectedItem();
	        		Console.printConsole("Background set to "+Life.background);
	        		Console.printConsole("Foreground set to "+Life.foreground);
	        		Console.printConsole("CellSize set to "+Life.cellSize);
	        		Console.printConsole("Sleeptime set to "+Life.intTime+"\n");
	        		Image.IMAGE.setBackground(Life.background);
	        		MAINWINDOW.repaint();
	        		fenetrechoix.dispose();}
	        }
	    });
	    fenetrechoix.add("South",valide); 	  	
	    valide.setVisible(true);	
	    
	}
	
	
	private static class TextLifeMode extends JTextArea{
		private static final long serialVersionUID = 1L;

		TextLifeMode(){super("The Conway's Game of Life in "+Life.mode+"mode...");setVisible(true);}
		
		@Override
		public void paint(Graphics g){
			this.setText("The Conway's Game of Life in "+Life.mode+" mode...");
			super.paint(g);
		}
	}
	
	
	private static JPanel createChoicePanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		
		TextLifeMode lbl = new TextLifeMode();
		lbl.setVisible(true);
		panel.add("Left",lbl);
		
		JTextArea tps = new JTextArea("Calculer la configuration dans ");
		panel.add(tps);
		tps.setVisible(true);
		
		final JTextField time = new JTextField("temps",10);
		panel.add(time);
		time.setVisible(true);
		
		JTextArea inter = new JTextArea(" par intervalles de ");
		panel.add(inter);
		inter.setVisible(true);
		
		final JTextField vitesse = new JTextField("intervalle",10);
		panel.add(vitesse);
		vitesse.setVisible(true);
		JButton valide = new JButton("Run");
	    valide.addActionListener(new ActionListener() {        
	        public void actionPerformed(ActionEvent e) {
	        	try{
	        		Life.targetTime = Integer.parseInt(time.getText());
	        		Life.intervalle = Integer.parseInt(vitesse.getText());
	        		Console.printConsole("Run to time "+Life.targetTime+" with intervals of "+Life.intervalle);
	        		Life.launch = true;
	        	}
	        	catch(Exception ex){
	        		Life.targetTime = 0; 
	        		Life.intervalle = 0;
	        		JOptionPane.showConfirmDialog(null,"Entrez des valeurs entières SVP","Erreur",JOptionPane.WARNING_MESSAGE);}
	        }
	    });
	    panel.add(valide); 	  	
	    valide.setVisible(true);	
	    JButton raz = new JButton("Reinit");
	    raz.addActionListener(new ActionListener() {        
	        public void actionPerformed(ActionEvent e) {Life.updateInitConfig(false);}
	    });
	    panel.add(raz);
	    raz.setVisible(true);
		panel.setVisible(true);
		return panel;
	}
	
	private static void choixFichier(){
		final JFrame fenetrechoix = new JFrame("Choix du fichier");
  	  	fenetrechoix.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  	  	fenetrechoix.setVisible(true);
  	  	fenetrechoix.setSize(1000, 700);
  	  	fenetrechoix.setLocation(100, 100);
  	  	
  	  	JPanel panel = new JPanel();
		fenetrechoix.add("North",panel);
		panel.setBackground(Color.white);
		panel.setMinimumSize(new Dimension(1000,700));
		
		JTextArea txtlist = new JTextArea("Choisir la configuration dans le bestiaire de la Vie:");
		panel.add(txtlist);
		txtlist.setVisible(true);
		
		File rlefiles = new File("rlefiles");
		String[] files = rlefiles.list();		
		final JComboBox box = new JComboBox(files);
		panel.add(box);
		panel.setVisible(true);
		
		JButton valide = new JButton("OK");
	    valide.addActionListener(new ActionListener() {        
	        public void actionPerformed(ActionEvent e) {
	        	Life.currentFile = (String) box.getSelectedItem();
	        	Life.updateInitConfig(true);
	        	fenetrechoix.dispose();
	        }
	    });
	    panel.add(valide); 	  	
	    valide.setVisible(true);
	    
	    JTextArea txtperso = new JTextArea("Choisir un fichier personnel de configuration:");
		panel.add(txtperso);
		txtperso.setVisible(true);
	    
		
		final JFileChooser choix = new JFileChooser();
	    choix.addActionListener(new ActionListener() {        
	        public void actionPerformed(ActionEvent e) {  
	        	try{
	        		String com = e.getActionCommand();
	        		if(com.equals(JFileChooser.APPROVE_SELECTION)){
	        			Life.currentFile = choix.getSelectedFile().getName();
	        			Life.updateInitConfig(true);
	        			fenetrechoix.dispose();
	        		}
	        		if (com.equals(JFileChooser.CANCEL_SELECTION)) {fenetrechoix.dispose();}  
	        	}catch (Exception ex){fenetrechoix.dispose();}
	        }
	    });
	    panel.add(choix); 	  	
	    choix.setVisible(true);
		
	}
	
	
	
}

class Image extends JComponent {	

	private static final long serialVersionUID = 1L;
	
	private static class ImageUI extends ComponentUI  {

	    static final ImageUI UI = new ImageUI();
	    
	    @Override
	    public void paint(Graphics g, JComponent c) {//g créé par l'appel à l'UI et c est le composant
	      Graphics2D gg = (Graphics2D) g.create();
	      gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	      gg.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
	      try {
	    	  Life.checkDrawingSize();
	    	  float l = Life.cellSize;
	    	  gg.setColor(Life.foreground);
	    	  boolean[][] draw = Life.currentState;
	    		  for(int i=0;i<draw.length;i++){
	    			  for(int j=0;j<draw[0].length;j++){
	    				  if(draw[i][j]) gg.fillRect((int)(l*j),(int)(l*i), ((int)l)+1, ((int)l)+1);
	    			  }
	    		  }  
	    		  /*
	    	  int n = (int) (l*Math.pow(2, Life.currentCell.n));
	    	  gg.drawLine(0,0, 0,n);
	    	  gg.drawLine(0,n, n, n);
	    	  gg.drawLine(n,0, n, n);
	    	  gg.drawLine(0,0, n, 0);
	    	  gg.drawLine(0,n/2, n,n/2);
	    	  gg.drawLine(n/2,0, n/2,n);
	    	  gg.drawLine(0,n/4, n,n/4);
	    	  gg.drawLine(n/4,0, n/4,n);
	    	  gg.drawLine(0,3*n/4, n,3*n/4);
	    	  gg.drawLine(3*n/4,0, 3*n/4,n);*/
	    	  
	      } catch(Exception e){
	    	  gg.drawChars("Explorez le Jeu de la Vie...".toCharArray(), 0, 28, 200, 200);
	      } finally {      
	        gg.dispose();
	      }
	    }

		
	  }

	Image() {
	    setUI(ImageUI.UI);
		setOpaque(true);
		setBackground(Color.BLACK);//fonctionne pas sans l'UI
	}
	
	static final Image IMAGE = new Image();
	

}


class Console extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private static class TextConsole extends JTextArea{	
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			String txt="";
			for(String s: texte){txt=s+"\n"+txt;}
			setText(txt);
			super.paint(g);
		}
	}
	
	private static final Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
	
	Console(){
		super("Console");
		setVisible(false);
		setSize(screenSize.width/4,screenSize.height);
		setLocation(3*screenSize.width/4,0);
		add(new TextConsole());
	}
	
	static final Console CONSOLE = new Console();
	
	private static final LinkedList<String> texte = new LinkedList<String>();
	
	static void printConsole(String s){
		texte.addFirst(s);
		if(texte.size()>40) texte.removeLast();
		CONSOLE.repaint();
	}
	
	static void clearConsole(){texte.clear();}
	
}





