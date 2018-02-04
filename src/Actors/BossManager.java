package Actors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import Items.ItemManager;

public class BossManager {

	public Boss[] bosse;
	public int currentBoss;
	private LinkedList<Boss> bo = new LinkedList<Boss>();
	private ItemManager items;

	// ****************************************************************
	// *** Konstruktor ***
	// ****************************************************************

	public BossManager(ItemManager im) {
		// ItemManager wird registriert
		items = im;

		// Debugboss erstellen lassen, um sicherzugehen das immer mindestens ein Boss
		// existiert
		createDebugBoss();

		// Liste mit Bossen füllen lassen
		parseBosses();

		// Liste wird als Array finalisiert
		bosse = bo.toArray(new Boss[bo.size()]);

		// Listen werden gecleared
		bo.clear();

		//Erster Boss wird gesetzt. Falls keine vorhanden wird auf den DebugBoss gesetzt
		if (bosse.length > 1) {
			currentBoss = 1;
		} else {
			currentBoss = 0;
		}

	}

	// ****************************************************************
	// *** Hilfsmethoden ***
	// ****************************************************************

	//Zählt den momentanen Boss um einen hoch. Wird das Array gesprengt setzt es auf den DebugBoss
	public void setNextBoss() {
		if (currentBoss != 0) {
			if (currentBoss > (bosse.length - 1)) {
				currentBoss++;
			} else {
				currentBoss = 0;
			}
		}
	}
	
	//Zählt den momentanen Boss um einen hoch. Wird das Array gesprengt setzt es auf den DebugBoss
	public void setBossByInt(int index) {
		currentBoss = index;
	}
	
	//Setzt Boss auf den angegebenen. Sonst Debugboss.
	public void setBossByName(String s) {
		for (int i = 0; i < bosse.length; i++) {
			if (bosse[i].name.toLowerCase().equals(s.toLowerCase())) {
				currentBoss = i;
			}
		}
	}

	//Gibt den momentanen Boss zurück
	public Boss getCurrentBoss() {
		return bosse[currentBoss];
	}

	//Suche im Array nach einem bestimmten Namen und gib diesen Boss zurück. Sonst den DebugBoss
	public Boss getBossByName(String s) {
		for (int i = 0; i < bosse.length; i++) {
			if (bosse[i].name.toLowerCase().equals(s.toLowerCase())) {
				return bosse[i];
			}
		}
		return getBossByName("Entwickler");
	}

	// ****************************************************************
	// *** Gruppenparse ***
	// ****************************************************************

	// Alle Bosse parsen
	private void parseBosses() {
		File verzeichnis = new File("Bosse"); // Verzeichnis wird aufgerufen
		File[] files = verzeichnis.listFiles(); // Dateien werden gelistet
		for (File file : files) {// Alle Dateien Werden gelesen
			if (file.isFile()&&file.getName().endsWith(".txt")) {
				parseBoss(file);// und geparsed
			}
		}
	}

	// ****************************************************************
	// *** Einzelparse ***
	// ****************************************************************

	// Einzelnen Boss Parsen
	private void parseBoss(File f) {
		try {
			// Template wird erstellt, zuerst mit Namen der Textdatei
			Boss template = new Boss(f.getName());

			// File lesen
			FileReader input;

			// Hier wird die Datei gelesen und die Informationen extrahiert
			input = new FileReader(f);
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
			while ((myLine = bufRead.readLine()) != null) {
				String[] sa = myLine.split("="); // Split beim =, da Syntax "Variable=Value"
				if (sa[0].toLowerCase().equals("name")) { // Name(Falls angegeben kann hier der Dateiname überschrieben
															// werde)
					template.name = sa[1];
				}
				if (sa[0].toLowerCase().equals("waffe")) { // Waffe
					template.setWaffe(items.getWeaponByName(sa[1]));
				}
				if (sa[0].toLowerCase().equals("rüstung")) { // Rüstung
					template.setArmor(items.getArmorByName(sa[1]));
				}
				
				
				if (sa[0].toLowerCase().equals("alter")) { 
					template.trivia[0] = sa[1];
				}
				if (sa[0].toLowerCase().equals("geburtsort")) { 
					template.trivia[1] = sa[1];
				}
				if (sa[0].toLowerCase().equals("verkehrsmittel")) {
					template.trivia[2] = sa[1];
				}
				if (sa[0].toLowerCase().equals("videospiel")) { 
					template.trivia[3] = sa[1];
				}
				if (sa[0].toLowerCase().equals("konsole")) { 
					template.trivia[4] = sa[1];
				}
				if (sa[0].toLowerCase().equals("hobby")) { 
					template.trivia[5] = sa[1];
				}
				if (sa[0].toLowerCase().equals("sport")) { 
					template.trivia[6] = sa[1];
				}
				if (sa[0].toLowerCase().equals("lieblingsfarbe")) { 
					template.trivia[7] = sa[1];
				}
				if (sa[0].toLowerCase().equals("lieblingspizza")) {
					template.trivia[8] = sa[1];
				}
				if (sa[0].toLowerCase().equals("hasspizza")) { 
					template.trivia[9] = sa[1];
				}
				if (sa[0].toLowerCase().equals("lieblingstemperatur")) { 
					template.trivia[10] = sa[1];
				}
				if (sa[0].toLowerCase().equals("hasstemperatur")) {
					template.trivia[11] = sa[1];
				}

				// Wird keine Waffe oder Rüstung gefunden, setze Debugequip
				if (template.getWaffe() == null) {
					template.setWaffe(items.getDebugWeapon());
				}
				if (template.getArmor() == null) {
					template.setArmor(items.getDebugArmor());
				}

				// Wird ein Attribut nicht gefunden gilt der Standard von Boss.
			}

			// Fertiges Template wird zum einsortieren in die Liste übergeben
			bo.add(template);
			System.out.println(template.name + " als Boss hinzugefügt");

			// Dann wird das Template geleert
			template = null;

			// Sinnloses Blabla
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Debug boss wird generiert, um eine leere Liste zu verhindern
	private void createDebugBoss() {
		Boss template = new Boss("Entwickler");
		template.setWaffe(items.getDebugWeapon());
		template.setArmor(items.getDebugArmor());
		bo.add(template);
		template = null;
	}

}
