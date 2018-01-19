package Kampf;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import Actors.Boss;
import Actors.BossManager;
import Actors.Spieler;
import Items.Item;
import Items.ItemManager;

public class Kampf {

	public static void main(String[] args) {
		ItemManager im = new ItemManager();
		BossManager bo = new BossManager(im);
		Spieler hero = new Spieler("Name");
		hero.waffe = im.getWeaponByName("Kendoschwert");
		hero.armor = im.getArmorByName("Kendorüstung");
		hero.item1 = im.getItemByName("Schneefestung");
		hero.item2 = im.getItemByName("Schneeball");
		hero.item3 = im.getItemByName("Pizza Hawaii");

		Kampf k = new Kampf(hero, bo);
		k.start();
	}

	BossManager bm;
	Scanner s;

	// Spieler
	Spieler held;
	boolean spezialangriffEingesetzt;

	// Boss
	Boss gegner;
	boolean spezialangriffVorbereitetCOM;

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

	// Prüft ob i1 effektiv gegen i2 ist und gibt das Ergebnis als Boolean zurück
	private boolean istEffektiv(Item i1, Item i2) {
		int h =0;
		if (i1.starkGegen.equals(i2.kategorie)) {
			h++;
		}
		if (i2.schwachGegen.equals(i1.kategorie)) {
			h++;
		}
		if (i1.schwachGegen.equals(i2.kategorie)) {
			h--;
		}
		if (i2.starkGegen.equals(i1.kategorie)) {
			h--;
		}
		if(h>=1) {
		return true;	
		}else {
		return false;
		}
	}

	// Prüft ob i1 schwach gegen i2 ist und gibt das Ergebnis als Boolean zurück
	private boolean istIneffektiv(Item i1, Item i2) {
		int h =0;
		if (i2.starkGegen.equals(i1.kategorie)) {
			h++;
		}
		if (i1.schwachGegen.equals(i2.kategorie)) {
			h++;
		}
		if (i2.schwachGegen.equals(i1.kategorie)) {
			h--;
		}
		if (i1.starkGegen.equals(i2.kategorie)) {
			h--;
		}
		if(h>=1) {
		return true;	
		}else {
		return false;
		}
	}

	// ****************************************************************
	// *** Ablauf ***
	// ****************************************************************
	public void start() {
		System.out.println(held.name + " vs " + gegner.name);
		System.out.println(held.waffe.name + " vs " + gegner.waffe.name);
		held.leben = 100;
		gegner.leben = 100;
		spezialangriffEingesetzt = false;

		s = new Scanner(System.in);
		while (true) {
			nextTurn();
		}
	}

	// Eine Spieler Runde
	private void nextTurn() {

		held.def = false; // Defensive läuft bei nächster Runde ab
		checkWin(); // Wurde durch einen Effekt gewonnen?

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
		System.out.println("4 Item");
		System.out.println("5 Fliehen");
		System.out.println("");

		int befehl = s.nextInt(); // Kommando auswählen
		if (befehl == 1) {
			angriff(held, gegner);
		}
		if (!spezialangriffEingesetzt) {
			if (befehl == 2) {
				spezialangriff(held, gegner);
			}
		}
		if (befehl == 4) {
			itemBefehl();
		}
		if (befehl == 3) {
			verteidigung(held);
		}
		if (befehl == 5) {
			fliehen();
		}

		gegnerTurn(); // Gegner ist dran
	}

	// Eine Gegner Runde
	private void gegnerTurn() {
		gegner.def = false; // Verteidigung läuft mit neuer Runde ab
		if (spezialangriffVorbereitetCOM) {
			gegnerSpezialangriff();
		} else {

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
		System.out.println(angreifer.name + " greift mit " + angreifer.waffe.name + " an!");
		if (verteidiger.def) {// Wenn Gegner verteidigt kein Schaden
			System.out.println("Aber " + verteidiger.name + " hat den Angriff geblockt!");
			verteidiger.def = false;
			schaden = 0;
		} else {// Sonst Berechnung
			if (ThreadLocalRandom.current().nextInt(0, 100 + 1) >= 1) {// Wenn Angriff nicht zufällig daneben geht
				schaden = 5; // Grundschaden
				if (istEffektiv(angreifer.waffe, verteidiger.armor)) {// Wenn Effektiv verdoppeln
					System.out.println("Und es ist sehr effektiv!");
					schaden = schaden * 2;
				}
				if (istIneffektiv(angreifer.waffe, verteidiger.armor)) {// Wenn Ineffektiv halbieren
					System.out.println("Aber es ist nicht sehr effektiv!");
					schaden = schaden / 2;
				}
			} else {// Wenn der Angriff zufällig daneben geht
				System.out.println("Aber er hat verfehlt!");
				schaden = 0;
			}
		}
		verteidiger.leben = verteidiger.leben - schaden;// Schaden zufügen
		checkWin();// Hat Schaden den Sieg gebracht?
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
			schaden = 10; // Grundschaden
			if (istEffektiv(angreifer.waffe, verteidiger.armor)) {// Wenn Effektiv verdoppeln
				System.out.println("Und es ist sehr effektiv!");
				schaden = schaden + 5;
			}
			if (istIneffektiv(angreifer.waffe, verteidiger.armor)) {// Wenn Ineffektiv halbieren
				System.out.println("Aber es ist nicht sehr effektiv!");
				schaden = schaden - 5;
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
		checkWin();// Hat Schaden den Sieg gebracht?
	}

	// Item einsetzen
	private void itemBefehl() {
		if (held.item1!=null) {
			System.out.println("1: "+held.item1.name+": "+held.item1Anzahl);
		} else {
			System.out.println("1: Leer");
		}
		if (held.item2!=null) {
			System.out.println("2: "+held.item2.name+": "+held.item2Anzahl);
		} else {
			System.out.println("2: Leer");
		}
		if (held.item3!=null) {
			System.out.println("3: "+held.item3.name+": "+held.item3Anzahl);
		} else {
			System.out.println("3: Leer");
		}
		
		int befehl = s.nextInt(); // Kommando auswählen
		if (held.item1!=null) {
			if (befehl == 1) {
				itemEinsatz(1);
			}
		}
		if (held.item2!=null) {
			if (befehl == 2) {
				itemEinsatz(2);
			}
		}
		if (held.item3!=null) {
			if (befehl == 3) {
				itemEinsatz(3);
			}
		}
	}
	
	private void itemEinsatz(int index) {
		Item item=null;
		if(index==1) {
			held.item1Anzahl--;
			item = held.item1;	
			if(held.item1Anzahl==0) {
				held.item1=null;
			}
		}
		if(index==1) {
			held.item2Anzahl--;	
			item = held.item2;
			if(held.item2Anzahl==0) {
				held.item2=null;
			}
		}
		if(index==1) {
			held.item3Anzahl--;	
			item = held.item3;
			if(held.item3Anzahl==0) {
				held.item3=null;
			}
		}
		
		if(item.typ.toLowerCase().equals("stun")) {
			
		}
		if(item.typ.toLowerCase().equals("heal")) {
			
		}
		if(item.typ.toLowerCase().equals("shield")) {
			
		}
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
		if (istEffektiv(gegner.waffe, held.armor)) {// Wenn Effektiv verdoppeln
			System.out.println("Und es ist sehr effektiv!");
			schaden = schaden * 2;
		}
		if (istIneffektiv(gegner.waffe, held.armor)) {// Wenn Ineffektiv halbieren
			System.out.println("Aber es ist nicht sehr effektiv!");
			schaden = schaden / 2;
		}

		if (held.def) {// Wenn Gegner verteidigt halber Schaden
			System.out.println(held.name
					+ " hat versucht den Angriff zu blockieren! Der Angriff war zu stark, wurde aber abgeschwächt!");
			held.def = false;
			schaden = schaden / 2;
		}

		held.leben = held.leben - schaden;// Schaden zufügen
		spezialangriffVorbereitetCOM=false;
		checkWin();// Hat Schaden den Sieg gebracht?
	}
}
