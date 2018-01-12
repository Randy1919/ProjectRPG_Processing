package Actors;
import Items.Item;

public class Boss {
	public String name = "John Doe";
	public Item waffe = null;
	public Item armor = null;

	// Boss Trivia. Jeder Index ist ein Trivia. Reihenfolge beachten!
	// 0 = Alter
	// 1 =
	// 2 =
	// 3 =
	String[] trivia;

	boolean[] triviaUnlocked;

	Boss(String n) {
		name = n;

		trivia = new String[10];
		triviaUnlocked = new boolean[trivia.length];
	}
	
	Boss(String n, Item w, Item a) {
		name = n;
		waffe = w;
		armor = a;

		trivia = new String[3];
		triviaUnlocked = new boolean[trivia.length];
	}

	public String getWaffeCat() {
		if(waffe!=null) {
		return waffe.kategorie;
		}else {return "";}
	}

	public String getArmorCat() {
		if(armor!=null) {
		return armor.kategorie;
		}else {return "";}
	}

	public void unlockRandomTrivia() {
		// Zufallszahl zwischen Null und wie viele Trivias es gibt
		int zufall = 0 + (int) (Math.random() * triviaUnlocked.length);
		int durchgang = 0;
		boolean Ende = false;

		// Solange Ende nicht gegeben
		while (!Ende) {

			// Schauen ob Trivia an Stelle Zufallszahl schon frei ist.
			if (triviaUnlocked[zufall] == true) {
				// Wenn schon freigeschaltet dann Zufallszahl um einen runter
				// Sonst auf die höchste Zahl
				if (zufall > 0) {
					zufall--;
				} else {
					zufall = triviaUnlocked.length;
				}
			} else {
				//Wenn nicht schon freigeschaltet, freischalten und Ende setzen
				triviaUnlocked[zufall] = true;
				Ende = true;
			}
			//durchgang hochzählen
			durchgang++;
			//falls durchgang=länge der Trivias heißt dass das bereits alle
			//Trivias freigeschaltet wurden. Dann Ende setzen um eine Endlosschleife
			//zu verhindern
			if (durchgang == triviaUnlocked.length) {
				Ende = true;
			}
		}

	}

}
