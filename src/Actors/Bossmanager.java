package Actors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import Items.ItemManager;

public class Bossmanager {
	
	public Boss[] Bosse;
	private LinkedList<Boss> bo = new LinkedList<Boss>();
	private	ItemManager items;
	
	//****************************************************************
	//***	Konstruktor											   ***
	//****************************************************************
	
	
	public Bossmanager(ItemManager im) {
		
		items=im;
		//Listen mit Items füllen lassen
		parseBosses();
		
		
		//Listen werden gelcleared
		bo.clear();

	}
	
	//****************************************************************
	//***	Gruppenparse										   ***
	//****************************************************************
	
	
	//Alle Rüstungen parsen
	private void parseBosses() {
		File verzeichnis = new File("Bosse"); // Verzeichnis wird aufgerufen
		File[] files = verzeichnis.listFiles(); // Dateien werden gelistet
		for (File file : files) {// Alle Dateien Werden gelesen
			if (file.isFile()) { 
				parseBoss(file);//und geparsed
			}
		}
	}

	
	//****************************************************************
	//***	Einzelparse											   ***
	//****************************************************************	
	
	// Einzelne Waffe Parsen
	private void parseBoss(File f) {
		try {
			// Template wird erstellt, zuerst mit Namen der Textdatei
			Boss template = new Boss(f.getName());

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
				if (sa[0].toLowerCase().equals("alter")) { //Alter
					template.trivia[0] = sa[1];
				}
				if (sa[0].toLowerCase().equals("waffe")) { //Waffe
					template.waffe = items.getWeaponByName(sa[1]);
				}
				if (sa[0].toLowerCase().equals("rüstung")) { //Rüstung
					template.armor = items.getArmorByName(sa[1]);
				}
				if (sa[0].toLowerCase().equals("sport")) { //Trivia1
					template.trivia[2] = sa[1];
				}
				
				//Wird ein Attribut nicht gefunden gilt der Standard von Item. 
			}
			
			//Fertiges Template wird zum einsortieren in die Liste übergeben
			bo.add(template);
			System.out.println(template.name + " als Boss hinzugefügt" );
			
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
