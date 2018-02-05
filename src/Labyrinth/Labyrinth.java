package Labyrinth;

import java.io.*;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import Actors.Boss;
import Actors.BossManager;

/**
 * 
 * @author Xalnaji
 *
 */
public class Labyrinth {

	File LabLayout;
	PApplet hauptmenu;
	Boss boss;
	
	public PFont f;
	public PImage imgWall; // Declare a variable of type PImage
	public PImage imgFloor; // Declare a variable of type PImage
	public PImage imgChest; // Declare a variable of type PImage

	int[][] nLabMatrix = new int[20][20];

	int nTileWidth = 30;
	int nTileHeight = 30;

	int nPlayerHeight = 120;
	int nPlayerWidth = 90;	
	
	String sInfo = "";
	
	boolean bShowInfo = false;
	boolean bExit = false;
	
	public void settings() {
		hauptmenu.size(1000, 1000);
	}

	public void setup() {
		hauptmenu.textFont(f, 40);
		hauptmenu.frameRate(30);
		
		if (new File("Images/labyrinth/wall.jpg").isFile()) {
			imgWall = hauptmenu.loadImage("Images/labyrinth/wall.jpg");
		}
		if (new File("Images/player/floor.jpg").isFile()) {
			imgFloor = hauptmenu.loadImage("Images/labyrinth/floor.jpg");
		}
		if (new File("Images/player/chest.png").isFile()) {
			imgChest = hauptmenu.loadImage("Images/labyrinth/chest.png");
		}
	}

	public boolean draw() {

		hauptmenu.background(255);
		
		int nTranslateToPlayerX = 0;
		int nTranslateToPlayerY = 0;
		
		int nTranslateY = 50;	
		for (int i = 0; i < this.nLabMatrix.length; i++) {
			int nTranslateX = 50;
			for (int j = 0; j < this.nLabMatrix[i].length; j++) {
				if(this.nLabMatrix[i][j] == 3)
				{
					nTranslateToPlayerX = nTranslateX + 650;
					nTranslateToPlayerY = nTranslateY + 850;
				}
				nTranslateX += nTileWidth;
			}
			nTranslateY += nTileHeight;
		}
		
		hauptmenu.pushMatrix();

		hauptmenu.translate(-(nTranslateToPlayerX / 2),-(nTranslateToPlayerY / 2));
		int nTilePositionY = 50;

		for (int i = 0; i < this.nLabMatrix.length; i++) {
			int nTilePositionX = 50;

			for (int j = 0; j < this.nLabMatrix[i].length; j++) {

				if (this.nLabMatrix[i][j] == 1 || this.nLabMatrix[i][j] == 3) {
					if (imgFloor != null) {
						hauptmenu.image(imgFloor, nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
					}

					if (this.nLabMatrix[i][j] == 3) {					
						hauptmenu.fill(230, 138, 0);
						hauptmenu.stroke(230,138,0);
						hauptmenu.rect(nTilePositionX + 5, nTilePositionY - 40, nPlayerWidth, nPlayerHeight);
					}
				} else if (this.nLabMatrix[i][j] == 2) {
					if (imgChest != null) {
						hauptmenu.image(imgChest, nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
					}
				} else {
					if (imgWall != null) {
						hauptmenu.image(imgWall, nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
					}
				}

				nTilePositionX += nTileWidth;
			}

			nTilePositionY += nTileHeight;
		}
		
		hauptmenu.popMatrix();
		
		if(bShowInfo == true)
		{
			hauptmenu.fill(230, 138, 0);
			hauptmenu.rect(257, 10, 485, 150);
			
			hauptmenu.fill(0,0,0);
			hauptmenu.rect(267, 20, 465, 130);
			
			hauptmenu.fill(255, 255, 255);
			hauptmenu.text(sInfo, 300, 100);
			hauptmenu.text("Weiter mit LEERTASTE", 300, 150);
		}
		
		
		return bExit;
	}

	/**
	 * Konstruktor der aufgerufen wird, falls ein Labyrinth aus einer Datei
	 * eingelesen werden soll.
	 * 
	 * @param uFilename
	 */
	public Labyrinth(PApplet p, BossManager bs) {
		hauptmenu = p;
		f = hauptmenu.createFont("Arial", 18, true);
		this.boss = bs.getCurrentBoss();
		this.loadRandomLabyrinth();
		setup();		
	}

	private void loadRandomLabyrinth() {
		
		File[] files = new File("LabLayouts").listFiles();
		Random rand = new Random();
		this.LabLayout = files[rand.nextInt(files.length)];
		
		try {
			this.processFile();
		} catch (IOException eIO) {
			eIO.getMessage();
		}
	}

	/**
	 * 
	 * Funktion um das Layout des Labyrinths in ein Array zu laden.
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void processFile() throws IOException {
		// Lädt die Datei ein.
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.LabLayout.getAbsolutePath()));

			String strLine = "";
			int nLine = 0;
			int nPosition = 0;

			/**
			 * Zeile wird aus der Textdatei eingelesen
			 */
			while ((strLine = br.readLine()) != null) {

				/**
				 * Es wird jedes Zeichen einzeln geladen.
				 */
				char[] cZeichen = strLine.toCharArray();

				for (char c : cZeichen) {
					if (Character.isDigit(c)) {
						int tile = Character.getNumericValue(c);
						this.nLabMatrix[nLine][nPosition] = tile;
						nPosition++;
					}
				}

				nPosition = 0;
				nLine++;
			}

			br.close();
		} catch (NullPointerException eNull) {
			System.out.print(eNull.getMessage());
		} finally {

		}
	}
	
	public void keyPressed() {
		
		if (hauptmenu.keyCode == 27) {
		    // leave labyrinth
		    // transition to menu
			hauptmenu.key = 0;
		    this.bExit = true;
		}

		int x=0;
		int y=0;
		
		for (int i = 0; i < this.nLabMatrix.length; i++) {
			for (int j = 0; j < this.nLabMatrix[i].length; j++) {
				if(this.nLabMatrix[i][j] == 3)
				{
					y = i;
					x = j;
				}
			}
		}
		
		switch (hauptmenu.key) {
		case 'a':
			if(this.nLabMatrix[y][x-1] != 0)
			{
				if(this.nLabMatrix[y][x-1] == 2)
				{
					this.nLabMatrix[y][x-1] = 3;
					this.nLabMatrix[y][x] = 1;
					
					this.sInfo = this.boss.unlockRandomTrivia();
					this.bShowInfo = true;
				}
				else
				{
					this.nLabMatrix[y][x-1] = 3;
					this.nLabMatrix[y][x] = 1;
				}
			}
			break;
		case 'w':
			if(this.nLabMatrix[y-1][x] != 0)
			{
				if(this.nLabMatrix[y-1][x] == 2)
				{
					this.nLabMatrix[y-1][x] = 3;
					this.nLabMatrix[y][x] = 1;

					this.sInfo = this.boss.unlockRandomTrivia();
					this.bShowInfo = true;
				}
				else
				{
					this.nLabMatrix[y-1][x] = 3;
					this.nLabMatrix[y][x] = 1;
				}
			}
			break;
		case 's':
			if(this.nLabMatrix[y+1][x] != 0)
			{
				if(this.nLabMatrix[y+1][x] == 2)
				{
					this.nLabMatrix[y+1][x] = 3;
					this.nLabMatrix[y][x] = 1;

					this.sInfo = this.boss.unlockRandomTrivia();
					this.bShowInfo = true;
				}
				else
				{
					this.nLabMatrix[y+1][x] = 3;
					this.nLabMatrix[y][x] = 1;
				}
			}
			break;
		case 'd':
			if(this.nLabMatrix[y][x+1] != 0)
			{
				if(this.nLabMatrix[y][x+1] == 2)
				{
					this.nLabMatrix[y][x+1] = 3;
					this.nLabMatrix[y][x] = 1;

					this.sInfo = this.boss.unlockRandomTrivia();
					this.bShowInfo = true;
				}
				else
				{
					this.nLabMatrix[y][x+1] = 3;
					this.nLabMatrix[y][x] = 1;
				}
				

			}
			break;
		case ' ':
			this.bShowInfo = false;
			break;
		}
	}

	public void keyReleased() {
	}
	
}
