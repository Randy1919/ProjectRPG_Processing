package Actors;

public class Boss extends Actor{

	// Boss Trivia. Jeder Index ist ein Trivia. Reihenfolge beachten!
	// 0 = Alter, 1 = Geburtsort, 2= Verkehrsmittel der Wahl
	// 3 = Lieblingsvideospielserie, 4 =Lieblingskonsole ,5 = Hobby,  
	// 6 = Sport, 7 = Lieblingsfarbe, 8 = Lieblingsessen
	// 9 = Hassessen, 10 = Lieblingstemperatur, 11 = Meistgehasste Temperatur
	public String[] triviaCategory = {"Alter","Geburtsort","Verkehrsmittel","Videospielreihe","Konsole","Hobby","Sport","Lieblingsfarbe","Lieblingspizza","Hasspizza","Lieblingstemperatur","Hasstemperatur"};	
	public String[] trivia;
	public boolean[] triviaUnlocked; 
	private String[] schwaechen;
	private String[] staerken;
	
	public int stunItem =3;
	public int healItem=3;

	
	//Konstruktor
	public Boss(String n) {
		super(n);

		trivia = new String[12];
		triviaUnlocked = new boolean[trivia.length];
		for(int i=0;i<trivia.length;i++) {
			triviaUnlocked[i]=false;
			trivia[i]="";
		}
		
		schwaechen = new String[4];
		staerken= new String[6];
		for(int i=0;i<schwaechen.length;i++) {
			schwaechen[i]="";
		}
		for(int i=0;i<staerken.length;i++) {
			staerken[i]="";
		}
	}
	
	//Gibt "schwach" zurück wenn Item gegen den Gegner schwach ist, und "stark" wenn stark. Sonst "normal"
	public String starkOderSchwach(String s) {
		
		for(int i=0;i<schwaechen.length;i++) {
			if(s.toLowerCase().equals(schwaechen[i].toLowerCase())) {
				return "stark";
			}
		}
		
		for(int i=0;i<staerken.length;i++) {
			if(s.toLowerCase().equals(staerken[i].toLowerCase())) {
				return "schwach";
			}
		}
		
		return "normal";
	}
	
	//Parsed Schwächen aus Trivia
	//0=Rüstungsschwäche,1=Hasstemperatur,2=Hasspizza,3=Lieblingsfarbe
	
	//Parsed Stärken aus Trivia
	//0=Rüstungsstärke,1=Lieblingstemperatur,2=Pizza,3=Hobby,4=Sport,
	public void parseCats() {

		
		schwaechen[0]=getArmor().schwachGegen;
		schwaechen[1]=trivia[11].toLowerCase();
		schwaechen[2]=trivia[9].toLowerCase();
		schwaechen[3]=trivia[7].toLowerCase();
		

		
		staerken[0]=getArmor().starkGegen;
		staerken[1]=trivia[10].toLowerCase();
		staerken[2]=trivia[8].toLowerCase();
		staerken[3]=trivia[5].toLowerCase();
		staerken[4]=trivia[6].toLowerCase();
	}
	
	//Zufällige Trivia wird freigeschaltet und eine passende Meldung wird zurückgegeben
	public String unlockRandomTrivia() {
		// Zufallszahl zwischen Null und wie viele Trivias es gibt
		int zufall = 0 + (int) (Math.random() * triviaUnlocked.length-1);
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
					zufall = triviaUnlocked.length-1;
				}
			} else {
				//Wenn nicht schon freigeschaltet, freischalten und Ende setzen
				triviaUnlocked[zufall] = true;
				String s = "Freigeschaltet: "+triviaCategory[zufall];
				
				if(zufall==10) {
					triviaUnlocked[11] = true;
					s = "Freigeschaltet: "+triviaCategory[zufall]+", "+triviaCategory[11];
				}else
				if(zufall==11) {
					triviaUnlocked[10] = true;
					s = "Freigeschaltet: "+triviaCategory[zufall]+", "+triviaCategory[10];
				}
				if(zufall==8) {
					triviaUnlocked[9] = true;
					s = "Freigeschaltet: "+triviaCategory[zufall]+", "+triviaCategory[9];
				}
				if(zufall==9) {
					triviaUnlocked[8] = true;
					s = "Freigeschaltet: "+triviaCategory[zufall]+", "+triviaCategory[8];
				}
				
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
