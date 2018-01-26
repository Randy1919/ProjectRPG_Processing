package Labyrinth;

import java.io.*;
import static javax.swing.JOptionPane.*;
import processing.core.PApplet;
import Actors.Boss;

/**
 * 
 * @author Xalnaji
 *
 */
public class Labyrinth extends PApplet {

	File LabLayout;

	int[][] nLabMatrix = new int[20][20];

	int nTileWidth = 30;
	int nTileHeight = 30;

	int nPlayerHeight = 40;
	int nPlayerWidth = 20;
	
	Boss boss = new Boss("Test");

	// ----------------------Dev---------------------------
	/**
	 * Dev Main Methode
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Labyrinth lab = new Labyrinth("layout_static");
		lab.loadLabyrinth();

		String[] argu = { "--location=100,200", "Kampf.Kampf" };
		PApplet.runSketch(argu, lab);
	}

	public void settings() {
		size(700, 700);
	}

	public void setup() {
		noLoop();
	}

	public void draw() {

		background(255);

		int nTilePositionY = 50;

		for (int i = 0; i < this.nLabMatrix.length; i++) {
			int nTilePositionX = 50;

			for (int j = 0; j < this.nLabMatrix[i].length; j++) {

				if (this.nLabMatrix[i][j] == 1 || this.nLabMatrix[i][j] == 3) {
					fill(255, 255, 255);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);

					if (this.nLabMatrix[i][j] == 3) {
						fill(230, 138, 0);
						rect(nTilePositionX + 5, nTilePositionY - 15, nPlayerWidth, nPlayerHeight);
					}
				} else if (this.nLabMatrix[i][j] == 2) {
					fill(0, 200, 0);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
				} else {
					fill(0, 0, 0);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
				}

				nTilePositionX += nTileWidth;
			}

			nTilePositionY += nTileHeight;
		}

	}

	// ----------------------Dev---------------------------
	/**
	 * Konstruktor der aufgerufen wird, falls ein Labyrinth dynamisch generiert
	 * werden soll. (Noch nicht implementiert)
	 */
	public Labyrinth() {
	}

	/**
	 * Konstruktor der aufgerufen wird, falls ein Labyrinth aus einer Datei
	 * eingelesen werden soll.
	 * 
	 * @param uFilename
	 */
	public Labyrinth(String uFilename) {
		this.LabLayout = new File("LabLayouts/" + uFilename);
		
	}

	/**
	 * 
	 */
	private void genLabyrinth() {

	}

	/**
	 * 
	 */
	private void loadLabyrinth() {
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
		
		switch (key) {
		case 'a':
			if(this.nLabMatrix[y][x-1] != 0)
			{
				if(this.nLabMatrix[y][x-1] == 2)
				{
					this.nLabMatrix[y][x-1] = 3;
					this.nLabMatrix[y][x] = 1;
					showMessageDialog(null, this.boss.unlockRandomTrivia(),"Info", INFORMATION_MESSAGE);
					//System.out.println(this.boss.unlockRandomTrivia());
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
					showMessageDialog(null, this.boss.unlockRandomTrivia(),"Info", INFORMATION_MESSAGE);
					//System.out.println(this.boss.unlockRandomTrivia());
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
					showMessageDialog(null, this.boss.unlockRandomTrivia(),"Info", INFORMATION_MESSAGE);
					//System.out.println(this.boss.unlockRandomTrivia());
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
					showMessageDialog(null, this.boss.unlockRandomTrivia(),"Info", INFORMATION_MESSAGE);
					//System.out.println(this.boss.unlockRandomTrivia());
				}
				else
				{
					this.nLabMatrix[y][x+1] = 3;
					this.nLabMatrix[y][x] = 1;
				}
				

			}
			break;
		}
	}

	public void keyReleased() {

		redraw();
	}
}
