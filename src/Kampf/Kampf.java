package Kampf;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import Actors.Actor;
import Actors.Boss;
import Actors.BossManager;
import Actors.Spieler;
import Items.Item;
import Items.ItemManager;

public class Kampf {

	// To-Do: Grafik
	// To-Do: Gegner KI
	
	public static void main(String[] args) {
		ItemManager im = new ItemManager();
		BossManager bo = new BossManager(im);
		Spieler hero = new Spieler("Name");
		hero.setWaffe(im.getWeaponByName("Kendoschwert"));
		hero.setArmor(im.getArmorByName("Kendorüstung"));
		hero.item[1] = im.getItemByName("Powerkugel");
		hero.item[2] = im.getItemByName("Schneeball");
		hero.item[3] = im.getItemByName("Pizza Hawaii");
		
		File f = new File("Held.txt");
		if(f.exists()) {
			hero.setWaffe(im.getWeaponByName("Master-Schwert"));
			hero.setArmor(im.getArmorByName("Heldenkleidung"));			
		}

		Kampf k = new Kampf(hero, bo);
		k.start();
	}

	BossManager bm;
	Scanner s;

	// Spieler
	Spieler held;
	boolean spezialangriffEingesetzt;
	int heldSchild;
	int heldStun;

	// Boss
	Boss gegner;
	boolean spezialangriffVorbereitetCOM;
	int gegnerSchild;
	int gegnerStun;

	// ****************************************************************
	// *** Konstruktor ***
	// ****************************************************************
	public Kampf(Spieler s, BossManager b) {
		held = s;
		bm = b;
		gegner = bm.getCurrentBoss();
	}

	// ****************************************************************
	// *** Hilfsmethoden ***
	// ****************************************************************
	private void onPlayerWin() {
		System.out.println("");
		System.out.println(gegner.name + " geht zu Boden! Du hast verloren!");
		held.siege++;
		bm.setNextBoss();
		System.exit(0);
	}

	private void onPlayerLose() {
		System.out.println("");
		System.out.println(held.name + " geht zu Boden! Du hast verloren!");
		System.exit(0);
	}

	// Prüft ob der Spieler noch verfügbare Items hat
	private boolean itemHeldVerfuegbar() {
		if (held.item[1] != null) {
			return true;
		}
		if (held.item[2] != null) {
			return true;
		}
		if (held.item[3] != null) {
			return true;
		}
		return false;
	}

	// ****************************************************************
	// *** Ablauf ***
	// ****************************************************************
	public void start() {
		System.out.println(held.name + " vs " + gegner.name);
		System.out.println(held.getWaffeName() + " vs " + gegner.getWaffeName());
		held.leben = 100;
		heldSchild=0;
		gegner.leben = 100;
		gegnerSchild=0;
		spezialangriffEingesetzt = false;
		heldStun = 0;
		gegnerStun = 0;
		gegner.parseCats();
		held.parseCats();

		s = new Scanner(System.in);
		while (true) {
			turn();
		}
	}

	// Eine Runde
	private void turn() {
		boolean legitTurn = false;

		if (heldStun==0) {
			while (!legitTurn) {// Ist Runde gültig? Wird durch Turn bestimmt
				legitTurn = playerTurn(); // Spieler ist dran.
			}
		} else {
			System.out.println("");
			System.out.println(held.name + " ist benommen und kann sich nicht bewegen!");
			System.out.println("");
			heldStun--;
		}
		checkWin(); // Wurde gewonnen?

		if (gegnerStun==0) {
			legitTurn = false;
			while (!legitTurn) {// Ist Runde gültig? Wird durch Turn bestimmt
				legitTurn = gegnerTurn(); // Spieler ist dran
			}
		} else {
			System.out.println("");
			System.out.println(gegner.name + " ist benommen und kann sich nicht bewegen!");
			System.out.println("");
			gegnerStun--;
		}
		checkWin(); // Wurde gewonnen?
	}

	// Eine Spieler Runde
	private boolean playerTurn() {
		held.def = false; // Defensive läuft bei nächster Runde ab

		System.out.println("");
		System.out.println("Spieler: " + held.leben);
		System.out.println("Gegner: " + gegner.leben);
		System.out.println("");

		System.out.println("Befehl Auswählen:");
		System.out.println("1 Angriff");
		if (!spezialangriffEingesetzt) {
			System.out.println("2 Spezialangriff - Verfügbar");
		} else {
			System.out.println("Spezialangriff - Schon genutzt!");
		}
		System.out.println("3 Verteidigung");
		if (itemHeldVerfuegbar()) {
			System.out.println("4 Item");
		} else {
			System.out.println("Keine Items verfügbar!");
		}
		System.out.println("5 Fliehen");
		System.out.println("");

		int befehl = s.nextInt(); // Kommando auswählen
		if (befehl == 1) {// Befehl 1: Ein einfacher Angriff
			angriff(held, gegner);
			return true;
		}
		if (befehl == 2) {// Befehl 2: Spezialangriff. Nur einmal pro Kampf nutzbar
			if (!spezialangriffEingesetzt) {
				spezialangriff(held, gegner);
				return true;
			} else {
				System.out.println("");
				System.out.println(held.name + " hat seinen Spezialangriff schon eingesetzt!");
				System.out.println("");
			}
		}
		if (befehl == 3) {// Befehl 3: Verteidigung. Spieler verteidigt sich diese Runde und kann nicht
							// getroffen werden
			verteidigung(held);
			return true;
		}
		if (befehl == 4) {// Befehl 4: Item einsetzen. Nur verfügbar wenn noch Items vorhanden
			if (itemHeldVerfuegbar()) {
				return itemBefehl();
			} else {
				System.out.println("");
				System.out.println(held.name + " hat keine Items mehr!");
				System.out.println("");
			}
		}
		if (befehl == 5) {// Befehl 5: Fliehen. Spieler verliert automatisch.
			fliehen();
			return true;
		}
		return false;
	}

	// Eine Gegner Runde
	private boolean gegnerTurn() {
		gegner.def = false; // Verteidigung läuft mit neuer Runde ab
		if (spezialangriffVorbereitetCOM) {
			gegnerSpezialangriff();
			return true;
		} else {
			return true;
		}
	}

	// ****************************************************************
	// *** Aktionen ***
	// ****************************************************************

	// ****************************************************************
	// *** Allgemein ***
	// ****************************************************************

	// Ein Angriff
	private void angriff(Actor angreifer, Actor verteidiger) {
		double schaden;
		System.out.println(angreifer.name + " greift mit " + angreifer.getWaffeName() + " an!");
		if (verteidiger.def) {// Wenn Gegner verteidigt kein Schaden
			System.out.println("Aber " + verteidiger.name + " hat den Angriff geblockt!");
			verteidiger.def = false;
			schaden = 0;
		} else {// Sonst Berechnung
			if (ThreadLocalRandom.current().nextInt(0, 100 + 1) >= 1) {// Wenn Angriff nicht zufällig daneben geht
				if(angreifer.getWaffeName().equals("Master-Schwert")) {
					System.out.println("Das legendäre Master-Schwert blitzt in seiner Hand!");	
					schaden=15;
				}else {
				schaden = 5; // Grundschaden
				}
				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
					System.out.println("Und es ist sehr effektiv!");
					schaden = schaden * 2;
				}
				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv halbieren
					System.out.println("Aber es ist nicht sehr effektiv!");
					schaden = schaden / 2;
				}
				if(verteidiger.getArmorName().equals("Heldenkleidung")) {
					System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
					schaden=schaden/2;
				}
			} else {// Wenn der Angriff zufällig daneben geht
				System.out.println("Aber er hat verfehlt!");
				schaden = 0;
			}
		}
		verteidiger.leben = verteidiger.leben - schaden;// Schaden zufügen
	}

	// Verteidigen
	private void verteidigung(Actor verteidiger) {
		System.out.println(verteidiger.name + " macht sich bereit dem nächsten Angriff auszuweichen!");
		verteidiger.def = true;// Verteidigung wird gesetzt
	}

	// Aufgeben
	private void fliehen() {
		onPlayerLose(); // Löst verlieren aus
	}

	// Prüft ob jemand gewonnen hat
	private void checkWin() {
		// Wenn Spieler Leben 0 dann verloren!
		if (held.leben <= 0) {
			held.leben = 0;
			onPlayerLose();
		}

		// Wenn Gegner Leben 0 dann gewonnen!
		if (gegner.leben <= 0) {
			gegner.leben = 0;
			onPlayerWin();
		}
	}

	// ****************************************************************
	// *** Spieler Aktionen ***
	// ****************************************************************

	// Der Spezialangriff des Spielers
	private void spezialangriff(Actor angreifer, Actor verteidiger) {
		double schaden;
		System.out.println(angreifer.name + " greift mit seiner Spezialtechnik an!");
		if (verteidiger.def) {// Wenn Gegner verteidigt kein Schaden
			System.out.println("Aber " + verteidiger.name + " hat den Angriff geblockt!");
			verteidiger.def = false;
			schaden = 0;
		} else {// Sonst Berechnung
			if(angreifer.getWaffeName().equals("Master-Schwert")) {
				schaden=20;
				System.out.println("Das legendäre Master-Schwert blitzt in seiner Hand!");				
			}else {
			schaden = 10; // Grundschaden
			}
			if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
				System.out.println("Und es ist sehr effektiv!");
				schaden = schaden + 5;
			}
			if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv halbieren
				System.out.println("Aber es ist nicht sehr effektiv!");
				schaden = schaden - 5;
			}
			if(verteidiger.getArmorName().equals("Heldenkleidung")) {
				System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
				schaden=schaden/2;
			}
			if (spezialangriffVorbereitetCOM) {// Wenn Gegner seinen Spezialangriff vorbereitet
				System.out.println(
						"Und es hat den Gegner unvorbereitet getroffen! Sein Spezialangriff wurde unterbrochen!");
				spezialangriffVorbereitetCOM = false; // Spezialangriff unterbrechen
				schaden = schaden + 5;
			}
		}
		verteidiger.leben = verteidiger.leben - schaden;// Schaden zufügen
		spezialangriffEingesetzt = true; // Spezialangriff verwendet
	}

	// Item einsetzen
	private boolean itemBefehl() {
		if (held.item[1] != null) {
			System.out.println("1: " + held.item[1].name + ": " + held.item[1].anzahl);
		} else {
			System.out.println("1: Leer");
		}
		if (held.item[2] != null) {
			System.out.println("2: " + held.item[2].name + ": " + held.item[2].anzahl);
		} else {
			System.out.println("2: Leer");
		}
		if (held.item[3] != null) {
			System.out.println("3: " + held.item[3].name + ": " + held.item[3].anzahl);
		} else {
			System.out.println("3: Leer");
		}
		
		int befehl = s.nextInt(); // Item auswählen
		if (held.item[befehl] != null) {
			return itemEinsatz(befehl);
		}
		System.out.println("Kein Item!");
		return false;
	}

	private boolean itemEinsatz(int index) {
		Item item = null;

		// Der übergebene Index(1-3) wird als Item gesetzt, die Anzahl der übrigen Items
		// um einen verringert und wenn keine mehr übrig sind das Item gelöscht
		if (index >= 1 && index <= 3) {
			item = held.item[index];
			
			if(item.typ.toLowerCase().equals("power")&&!spezialangriffEingesetzt) {
				System.out.println("Dein Spezialangriff ist noch verfügbar!");
				return false;
			}
			
			held.item[index].anzahl--;
			if (held.item[index].anzahl == 0) {
				held.item[index] = null;
			}
			
		}


		// Das gesetzte Item bestimmt hier die Wirkung

		//Als Stun Item
		if (item.typ.toLowerCase().equals("stun")) {
			System.out.println(held.name+" benutzt "+item.name+" um den Gegner zu betäuben!");
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				gegnerStun = gegnerStun + 2;
				System.out.println("Es ist sehr effektiv und betäubt den Gegner für 2 Runden!");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				gegnerStun = 0;
				System.out.println("Doch es macht "+gegner.name+" nichts aus!");
			}else {
				gegnerStun++;
				System.out.println("Der Gegner ist für eine Runde betäubt!");
			}
			
			return true;
		}
		
		//Als Heal Item
		if (item.typ.toLowerCase().equals("heal")) {
			System.out.println(held.name+" isst "+item.name+" um sich zu heilen!");
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				held.leben=held.leben+15;
				gegnerStun++;
				System.out.println("Und heilt sich damit um 15 Punkte!");
				System.out.println(gegner.name+" wird schlecht als er das sieht! Er ist für eine Runde betäubt!");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				gegnerStun = 0;
				System.out.println("Doch es macht "+gegner.name+" nichts aus!");
			}else {
				held.leben=held.leben+15;
				System.out.println("Und heilt sich damit um 15 Punkte!");
			}
			return true;
		}
		
		//Als Poweritem!
		if (item.typ.toLowerCase().equals("power")) {
			System.out.println(held.name+" nutzt "+item.name+" um seinen Spezialangriff zu erholen!");
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				spezialangriffEingesetzt=false;
				gegnerStun++;
				System.out.println("Der Gegner ist von seinem plötzlichen Kraftanstieg wie gelähmt!");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				spezialangriffVorbereitetCOM=true;
				gegnerStun=0;
				System.out.println("Doch der Gegner durchschaut dies und kontert mit seinem eigenen Spezialangriff!");
			}else {
				spezialangriffEingesetzt=false;
			}
			return true;
		}

		return false;
	}

	// ****************************************************************
	// *** Gegner Aktionen ***
	// ****************************************************************

	// Der Spezialangriff des Spielers
	private void gegnerSpezialangriffVorbereitung() {
		spezialangriffVorbereitetCOM = true;
		System.out.println("");
		System.out.println(gegner.name + " fängt an seinen Spezialangriff vorzubereiten");
	}

	// Der Spezialangriff des Spielers
	private void gegnerSpezialangriff() {
		double schaden;
		System.out.println(gegner.name + " greift mit seiner Spezialtechnik an!");

		schaden = 10; // Grundschaden
		if (held.starkOderSchwach(gegner.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
			System.out.println("Und es ist sehr effektiv!");
			schaden = schaden * 2;
		}
		if (gegner.starkOderSchwach(gegner.getWaffeCat()).equals("stark")) {// Wenn Ineffektiv halbieren
			System.out.println("Aber es ist nicht sehr effektiv!");
			schaden = schaden / 2;
		}

		if (held.def) {// Wenn Gegner verteidigt halber Schaden
			System.out.println(held.name
					+ " hat versucht den Angriff zu blockieren! Der Angriff war zu stark, wurde aber abgeschwächt!");
			held.def = false;
			schaden = schaden / 2;
		}
		
		if(held.getArmorName().equals("Heldenkleidung")) {
			System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
			schaden=schaden/2;
		}

		held.leben = held.leben - schaden;// Schaden zufügen
		spezialangriffVorbereitetCOM = false;
		checkWin();// Hat Schaden den Sieg gebracht?
	}
}
