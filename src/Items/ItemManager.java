package Items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class ItemManager {
	public Item[] waffen;
	public Item[] armors;
	
	private LinkedList<Item> lw = new LinkedList<Item>();
	private LinkedList<Item> la = new LinkedList<Item>();
	
	//****************************************************************
	//***	Konstruktor											   ***
	//****************************************************************
	
	public ItemManager() {
		
		CreateDebugEquip();
		
		//Listen mit Items füllen lassen
		parseWeapons();
		parseArmors();
		
		//Gefüllte Listen werden als Arrays finalisiert
		waffen = lw.toArray(new Item[lw.size()]);
		armors = la.toArray(new Item[la.size()]);
		
		//Listen werden gelcleared
		lw.clear();
		la.clear();
	}
	
	//****************************************************************
	//***	Hilfsmethoden										   ***
	//****************************************************************	
	
	public Item getWeaponByName(String s) {
		for(int i =0;i<waffen.length;i++) {
			if(waffen[i].name.toLowerCase().equals(s.toLowerCase())) {
				return waffen[i];
			}
		}
		return getWeaponByName("Unbewaffnet");
	}
	
	public Item getArmorByName(String s) {
		for(int i =0;i<armors.length;i++) {
			if(armors[i].name.toLowerCase().equals(s.toLowerCase())) {
				return armors[i];
			}
		}
		return getArmorByName("Nackt");		
	}
	
	private void CreateDebugEquip() {
			Item template = new Item("Unbewaffnet");
			template.kategorie = "Debug";
			template.starkGegen = "";
			template.schwachGegen = "";
			template.beschreibung = "";
			template.slot = 0;
			lw.add(template);
			template=null;
			template = new Item("Nackt");
			template.kategorie = "Debug";
			template.starkGegen = "";
			template.schwachGegen = "";
			template.beschreibung = "";
			template.slot = 0;
			la.add(template);
			template=null;
	}
	//****************************************************************
	//***	Gruppenparse										   ***
	//****************************************************************
	
	//Alle Waffen Parsen
	private void parseWeapons() {
		File verzeichnis = new File("Items/Waffen"); // Verzeichnis wird aufgerufen
		File[] files = verzeichnis.listFiles(); // Dateien werden gelistet
		for (File file : files) {// Alle Dateien Werden gelesen
			if (file.isFile()) { 
				parseWeapon(file);//und geparsed
			}
		}
	}
	
	//Alle Rüstungen parsen
	private void parseArmors() {
		File verzeichnis = new File("Items/Panzerung"); // Verzeichnis wird aufgerufen
		File[] files = verzeichnis.listFiles(); // Dateien werden gelistet
		for (File file : files) {// Alle Dateien Werden gelesen
			if (file.isFile()) { 
				parseArmor(file);//und geparsed
			}
		}
	}

	
	//****************************************************************
	//***	Einzelparse											   ***
	//****************************************************************	
	
	// Einzelne Waffe Parsen
	private void parseWeapon(File f) {
		try {
			// Template wird erstellt, zuerst mit Namen der Textdatei
			Item template = new Item(f.getName());

			// File lesen
			FileReader input;

			//Hier wird die Datei gelesen und die Informationen extrahiert
			input = new FileReader(f);
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
			while ((myLine = bufRead.readLine()) != null) {
				String[] sa = myLine.split("="); // Split beim =, da Syntax "Variable=Value"
				if (sa[0].toLowerCase().equals("name")) { //Name(Falls angegeben kann hier der Dateiname überschrieben werde)
					template.name = sa[1];
				}
				if (sa[0].toLowerCase().equals("kategorie")) { //Kategorie
					template.kategorie = sa[1];
				}
				if (sa[0].toLowerCase().equals("stärke")) { //Stärke
					template.starkGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("schwäche")) { //Schwäche
					template.schwachGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("beschreibung")) { //Beschreibung
					template.beschreibung = sa[1];
				}
					template.slot = 0;
				
				//Wird ein Attribut nicht gefunden gilt der Standard von Item. 
			}
			
			//Fertiges Template wird zum einsortieren in die Liste übergeben
			lw.add(template);
			System.out.println(template.name + " zu Waffen hinzugefügt" );
			
			//Dann wird das Template geleert
			template=null;
			
		//Sinnloses Blabla
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Einzelne Waffe Parsen
	private void parseArmor(File f) {
		try {
			// Template wird erstellt, zuerst mit Namen der Textdatei
			Item template = new Item(f.getName());

			// File lesen
			FileReader input;

			//Hier wird die Datei gelesen und die Informationen extrahiert
			input = new FileReader(f);
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
			while ((myLine = bufRead.readLine()) != null) {
				String[] sa = myLine.split("="); // Split beim =, da Syntax "Variable=Value"
				if (sa[0].toLowerCase().equals("name")) { //Name(Falls angegeben kann hier der Dateiname überschrieben werde)
					template.name = sa[1];
				}
				if (sa[0].toLowerCase().equals("kategorie")) { //Kategorie
					template.kategorie = sa[1];
				}
				if (sa[0].toLowerCase().equals("stärke")) { //Stärke
					template.starkGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("schwäche")) { //Schwäche
					template.schwachGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("beschreibung")) { //Beschreibung
					template.beschreibung = sa[1];
				}
					template.slot = 1;
				
				//Wird ein Attribut nicht gefunden gilt der Standard von Item. 
			}
			
			//Fertiges Template wird zum einsortieren in die Liste übergeben
			la.add(template);
			System.out.println(template.name + " zu Rüstung hinzugefügt" );
			
			//Dann wird das Template geleert
			template=null;
			
		//Sinnloses Blabla
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
