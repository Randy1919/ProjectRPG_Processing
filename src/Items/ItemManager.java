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
	public Item[] items;
	
	private LinkedList<Item> lw = new LinkedList<Item>();
	private LinkedList<Item> la = new LinkedList<Item>();
	private LinkedList<Item> li = new LinkedList<Item>();
	
	//****************************************************************
	//***	Konstruktor											   ***
	//****************************************************************
	
	public ItemManager() {
		
		//DebugEquipment wird generiert um zu verhindern das die Liste je leer ist, 
		//um NullPointerExceptions zu verhindern
		CreateDebugEquip();
		
		//Listen mit Items füllen lassen
		parseWeapons();
		parseArmors();
		parseItems();
		
		//Gefüllte Listen werden als Arrays finalisiert
		waffen = lw.toArray(new Item[lw.size()]);
		armors = la.toArray(new Item[la.size()]);
		items = li.toArray(new Item[li.size()]);
		
		//Listen werden gecleared
		lw.clear();
		la.clear();
		li.clear();
	}
	
	//****************************************************************
	//***	Hilfsmethoden										   ***
	//****************************************************************	
	
	
	//Sucht durch das Array der Waffen nach einem Namen. Wenn gefunden gibt es die entsprechende Waffe zurück. Sonst die DebugWaffe
	public Item getWeaponByName(String s) {
		for(int i =0;i<waffen.length;i++) {
			if(waffen[i].name.toLowerCase().equals(s.toLowerCase())) {
				return waffen[i];
			}
		}
		return getWeaponByName("Unbewaffnet");
	}

	//Sucht durch das Array der Rüstungen nach einem Namen. Wenn gefunden gibt es die entsprechende Rüstung zurück. Sonst die DebugArmor
	public Item getArmorByName(String s) {
		for(int i =0;i<armors.length;i++) {
			if(armors[i].name.toLowerCase().equals(s.toLowerCase())) {
				return armors[i];
			}
		}
		return getArmorByName("Nackt");		
	}
	
	//Sucht durch das Array der Items nach einem Namen. Wenn gefunden gibt es das entsprechende Item zurück.
	public Item getItemByName(String s) {
		for(int i =0;i<items.length;i++) {
			if(items[i].name.toLowerCase().equals(s.toLowerCase())) {
				return items[i];
			}
		}
		return null;		
	}
	
	//Gibt die Debugwaffe zurück. Eigentlich nur damit ich aus anderen Klassen aufrufen kann ohne mir den Namen merken zu müssen. Ich bin faul...
	public Item getDebugWeapon() {
		return getWeaponByName("Unbewaffnet");
	}
	//Gibt die DebugArmor zurück. Eigentlich nur damit ich aus anderen Klassen aufrufen kann ohne mir den Namen merken zu müssen. Ich bin faul...
	public Item getDebugArmor() {
		return getArmorByName("Nackt");	
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
	
	//Alle Items parsen
	private void parseItems() {
		File verzeichnis = new File("Items/Consumables"); // Verzeichnis wird aufgerufen
		File[] files = verzeichnis.listFiles(); // Dateien werden gelistet
		for (File file : files) {// Alle Dateien Werden gelesen
			if (file.isFile()) { 
				parseItem(file);//und geparsed
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
				if (sa[0].toLowerCase().equals("schaden")) { //Stärke
					template.schaden = Integer.parseInt(sa[1]);
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
			if(template.name.endsWith(".txt")) {
				String[] name = f.getName().split(".txt");
				String name1 = name[0];
				template.name=name1;
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
			System.out.println(template.name + " zu Rüstungen hinzugefügt" );
			
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

	private void parseItem(File f) {
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
				if(sa.length==2) {
				if (sa[0].toLowerCase().equals("name")) { //Name(Falls angegeben kann hier der Dateiname überschrieben werde)
					template.name = sa[1];
				}
				if (sa[0].toLowerCase().equals("kategorie")) { //Kategorie
					template.kategorie = sa[1];
				}
				
				if (sa[0].toLowerCase().equals("typ")) {//Schwäche
					if(sa[1].toLowerCase().equals("power")||sa[1].toLowerCase().equals("stun")||sa[1].toLowerCase().equals("heal")) {
						template.typ = sa[1];
					}else {
					template=null;
					return;
					}
				}
				
				if (sa[0].toLowerCase().equals("stärke")) {//Stärke
					template.starkGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("schwäche")) { //Schwäche
					template.schwachGegen = sa[1];
				}
				if (sa[0].toLowerCase().equals("beschreibung")) { //Beschreibung
					template.beschreibung = sa[1];
				}
				if (sa[0].toLowerCase().equals("anzahl")) { //Anzahl
					template.anzahl = Integer.parseInt(sa[1]);
				}
				
					template.slot = 2;
				}
				//Wird ein Attribut nicht gefunden gilt der Standard von Item. 
			}
			
			//Fertiges Template wird zum einsortieren in die Liste übergeben
			li.add(template);
			System.out.println(template.name + " zu Items hinzugefügt" );
			
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
	
	
	//Erstellt die Debug- Waffe und Rüstung um zu verhindern dass das Array je leer ist, um NullPointers zu verhindern.
	private void CreateDebugEquip() {
		Item template = new Item("Unbewaffnet");
		template.kategorie = "Debug";
		template.schaden=1;
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
		template.slot = 1;
		la.add(template);
		template=null;
		
		
		//Kleines Easter-Egg wenn Held.txt im Hauptverzeichnis existiert
		File f = new File("Held.txt");
		if(f.exists()) {
			template = new Item("Master-Schwert");
			template.kategorie = "Legendär";
			template.schaden=15;
			template.starkGegen = "Alles";
			template.schwachGegen = "Nichts";
			template.beschreibung = "Das legendäre Master-Schwert, Zerstörer des Bösen. Mit dieser Klinge in der Hand bist du unaufhaltsam.";
			template.slot = 0;
			lw.add(template);
			System.out.println(template.name + " zu Waffen hinzugefügt" );
			template=null;
			template = new Item("Heldenkleidung");
			template.kategorie = "Legendär";
			template.starkGegen = "Alles";
			template.schwachGegen = "Nichts";
			template.beschreibung = "Eine grüne Kleidung mit Mütze, wie sie ein gewisser Held seit Jahrzehnten trägt. Schützt den Träger selbst vor heftigsten Angriffen";
			template.slot = 1;
			la.add(template);
			System.out.println(template.name + " zu Rüstungen hinzugefügt" );
			template=null;			
		}
}

}
