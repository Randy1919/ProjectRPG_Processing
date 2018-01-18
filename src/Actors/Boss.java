package Actors;
import Kampf.Actor;

public class Boss extends Actor{

	// Boss Trivia. Jeder Index ist ein Trivia. Reihenfolge beachten!
	// 0 = Alter, 1 = , 2=
	// 3 = , 4 = ,5 = Hobby,  
	// 6 = Sport, 7 = Lieblingsfarbe, 8 = Lieblingsessen
	// 9 = 
	String[] triviaCategory = {"Alter","","","","","Hobby","Sport","Lieblingsfarbe","Lieblingsessen",""};	
	String[] trivia;
	boolean[] triviaUnlocked;

	
	//Konstruktor
	Boss(String n) {
		super(n);

		trivia = new String[10];
		triviaUnlocked = new boolean[trivia.length];
	}



	//Zufällige Trivia wird freigeschaltet und eine passende Meldung wird zurückgegeben
	public String unlockRandomTrivia() {
		// Zufallszahl zwischen Null und wie viele Trivias es gibt
		int zufall = 0 + (int) (Math.random() * triviaUnlocked.length);
		int durchgang = 0;

		// Solange Ende nicht gegeben
		while (true) {

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
				String s = "Du hast folgendes über den Gegner herausgefunden: "+triviaCategory[zufall];
				return s;
			}
			//durchgang hochzählen
			durchgang++;
			//falls durchgang=länge der Trivias heißt dass das bereits alle
			//Trivias freigeschaltet wurden. Dann Ende setzen um eine Endlosschleife
			//zu verhindern
			if (durchgang == triviaUnlocked.length) {
				return "Es sind bereits alle Informationen freigeschaltet!";
			}
		}

	}

}
