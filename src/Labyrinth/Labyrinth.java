package Labyrinth;

import java.io.*;

import processing.core.PApplet;

/**
 * 
 * @author Xalnaji
 *
 */
public class Labyrinth extends PApplet {

	File LabLayout;

	int[][] nLabMatrix = new int[20][20];

	int nTilePositionX = 30;
	int nTilePositionY = 30;
	int nTileWidth = 30;
	int nTileHeight = 30;

	// ----------------------Dev---------------------------
	/**
	 * Dev Main Methode
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Labyrinth lab = new Labyrinth("layout_static");
		lab.loadLabyrinth();

		PApplet.main("Labyrinth.Labyrinth");

	}
	
	public void settings() {
		size(700,700);
	}

	public void setup() {
		
	}

	public void draw() {
		
		background(255);
		
		nTilePositionY = 50;

		for (int i = 0; i < this.nLabMatrix.length; i++) {
			nTilePositionX = 50;

			for (int j = 0; j < this.nLabMatrix[i].length; j++) {
				
				if (this.nLabMatrix[i][j] == 1) {
					fill(0,0,0);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
				} else if (this.nLabMatrix[i][j] == 2) {
					fill(0, 200, 0);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
				} else {
					fill(255,255,255);
					rect(nTilePositionX, nTilePositionY, nTileWidth, nTileHeight);
				}

				nTilePositionX += nTileWidth;
			}

			nTilePositionY += nTileHeight;
		}
		

	}

	private String getLayoutAsString() {
		String strArray = "";

		for (int i = 0; i < this.nLabMatrix.length; i++) {
			for (int j = 0; j < this.nLabMatrix[i].length; j++) {
				strArray += "" + this.nLabMatrix[i][j];
			}
			strArray += "\n";
		}

		return strArray;
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
		}catch (NullPointerException eNull) {
			System.out.print(eNull.getMessage());			
		} finally {
			
		}
	}
}
