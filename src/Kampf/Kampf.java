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
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Kampf extends PApplet {

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
		if (f.exists()) {
			hero.setWaffe(im.getWeaponByName("Master-Schwert"));
			hero.setArmor(im.getArmorByName("Heldenkleidung"));
		}

		String[] argu = { "--location=0,0", "Kampf.Kampf" };
		Kampf k = new Kampf(hero, bo);
		PApplet.runSketch(argu, k);

		k.startKampf();

	}

	PFont f;
	BossManager bm;
	Scanner s;

	boolean wait;
	boolean itempressed;
	
	PImage imgbackground;	// Declare a variable of type PImage
	PImage imgplayer;	// Declare a variable of type PImage
	PImage imgplayerspezial;	// Declare a variable of type PImage
	PImage imgboss;	// Declare a variable of type PImage

	// Spieler
	Spieler held;
	boolean spezialangriffEingesetzt;
	boolean spezialangriffaktiv=false;
	int heldSchild;
	int heldStun;

	// Boss
	Boss gegner;
	boolean spezialangriffVorbereitetCOM;
	int gegnerSchild;
	int gegnerStun;

	// ****************************************************************
	// *** Grafik ***
	// ****************************************************************

	public void settings() {
		size(1000, 1000);
	}

	public void setup() {
		background(0);
		f = createFont("Arial", 18, true);
		textFont(f, 40);
		frameRate(30);
		wait = false;
		itempressed = false;

		if(new File("Images/spielerkampf.png").isFile()) {
			  imgplayer = loadImage("Images/spielerkampf.png");
		}
		if(new File("Images/spielerspezial.jpg").isFile()) {
			  imgplayerspezial = loadImage("Images/spielerspezial.jpg");
		}
		if(new File("Bosse/"+gegner.name+".png").isFile()) {
			  imgboss = loadImage("Bosse/"+gegner.name+".png");
		}	
		if(new File("Images/background.jpg").isFile()) {
			  imgbackground = loadImage("Images/background.jpg");		
		}

	}

	public void draw() {
		if (!wait) {
			if(!spezialangriffaktiv) {
			drawBackground();
			drawBackgroundPicture();
			drawPlayer();
			drawBoss();



			
			drawLeben();
			if (!itempressed) {
				drawTurnButtons();
			} else {
				drawItemButtons();
			}
		}else {
			drawSpezial();	
			spezialangriffaktiv=false;
			wait=true;
		}
		} else {
			delay(1000);
			wait = false;
		}
	}

	public void drawBackground() {
		background(0);
		textFont(f, 40);
		fill(230, 138, 0);

		//
		// Umrandungen
		//

		// Lebensbalken
		rect(10, 10, 485, 150);
		rect(505, 10, 485, 150);

		// Hauptfenster
		rect(10, 170, 980, 550);

		// Ergebnisse und Menü
		rect(10, 730, 485, 260);
		rect(505, 730, 485, 260);

		fill(0, 0, 0);

		//
		// Innenflächen
		//

		// Lebensbalken
		rect(20, 20, 465, 130);
		rect(515, 20, 465, 130);

		// Hauptfenster
		rect(20, 180, 960, 530);

		// Ergebnisse und Menü
		rect(20, 740, 465, 240);
		rect(515, 740, 465, 240);
	}


	public void drawBackgroundPicture() {
		if(imgbackground!=null) {
		  image(imgbackground,20,180,961,531);	
		}
	}
	
	public void drawSpezial() {
		if(imgplayerspezial!=null) {
		  image(imgplayerspezial,20,180,961,531);	
		}
	}
	
	public void drawPlayer() {
		if(imgplayer!=null) {
		  image(imgplayer,80,350,200,340);	
		}
	}

	public void drawBoss() {
		if(imgboss!=null) {
			  image(imgboss,720,350,200,340);	
			}		
	}
	
	public void drawLeben() {
		//
		// Text
		//
		fill(255, 255, 255);

		text(held.name.split("\\s+")[0] + ": ", 50, 100);
		text((int) held.leben + "", 355, 100);
		text(gegner.name.split("\\s+")[0] + ": ", 555, 100);
		text((int) gegner.leben + "", 855, 100);
	}

	// Button Spalten(X) Positionen
	float bts1 = 525;
	float bts2 = 726;

	// Button Reihen(Y) Positionen
	float btr1 = 760;
	float btr2 = 830;
	float btr3 = 900;

	// Button Weite
	float btiw = 400;
	float btmw = 200;
	// Button Höhe
	float bth = 50;

	// Item Buttons
	Button item1 = new Button(bts1, btr1, btiw, bth);
	Button item2 = new Button(bts1, btr2, btiw, bth);
	Button item3 = new Button(bts1, btr3, btiw, bth);

	// Menü Buttons
	Button btang = new Button(bts1, btr1, btmw, bth);
	Button btspez = new Button(bts2, btr1, btmw, bth);
	Button btdef = new Button(bts1, btr2, btmw, bth);
	Button bti = new Button(bts2, btr2, btmw, bth);
	Button btflee = new Button(bts1, btr3, btmw, bth);
	Button item6 = new Button(bts2, btr3, btmw, bth);

	public void drawTurnButtons() {
		textFont(f, 30);
		
		// Button 1 (Angriff)
		if (overButton(btang)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Angriff", btang.positionX + 10, btang.positionY + 40);

		// Button 2 (Spezialangriff)
		if (overButton(btspez)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		if (spezialangriffEingesetzt) {
			fill(255, 0, 0);
		}
		text("Spezialangriff", btspez.positionX + 10, btspez.positionY + 40);

		// Button 3 (Verteidigung)
		if (overButton(btdef)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Verteidigen", btdef.positionX + 10, btdef.positionY + 40);

		// Button 4 (Items)
		if (overButton(bti)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Items", bti.positionX + 10, bti.positionY + 40);

		// Button 5 (Fliehen)
		if (overButton(btflee)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Fliehen", btflee.positionX + 10, btflee.positionY + 40);

		// Button 6
		if (overButton(item6)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("6", item6.positionX + 10, item6.positionY + 40);
	}

	public void drawItemButtons() {
		textFont(f, 30);
		
		// Button 1
		if (overButton(item1)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		if (held.item[1] != null) {
			text(held.item[1].name + ": " + held.item[1].anzahl, item1.positionX + 10, item1.positionY + 40);
		} else {
			text("Leer", item1.positionX + 10, item1.positionY + 40);
		}

		// Button 2
		if (overButton(item2)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		if (held.item[2] != null) {
			text(held.item[2].name + ": " + held.item[2].anzahl, item2.positionX + 10, item2.positionY + 40);
		} else {
			text("Leer", item2.positionX + 10, item2.positionY + 40);
		}

		// Button 3
		if (overButton(item3)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		if (held.item[3] != null) {
			text(held.item[3].name + ": " + held.item[3].anzahl, item3.positionX + 10, item3.positionY + 40);
		} else {
			text("Leer", item3.positionX + 10, item3.positionY + 40);
		}
	}

	boolean overButton(Button h) {
		if (mouseX >= h.positionX && mouseX <= h.positionX + h.weite && mouseY >= h.positionY
				&& mouseY <= h.positionY + h.hoehe) {
			return true;
		} else {
			return false;
		}
	}

	public void drawAttacker(Actor attacker) {
		wait = true;
		textFont(f, 30);
		text(attacker.name.split("\\s+")[0], 50, 800);
	}

	public void drawWeapon(Item usedItem) {
		wait = true;
		textFont(f, 30);
		text(usedItem.name, 50, 850);
	}

	public void drawWeapon(String usedItem) {
		wait = true;
		textFont(f, 30);
		text(usedItem, 50, 850);
	}

	public void drawAttacked(Actor defender) {
		wait = true;
		textFont(f, 30);
		text(defender.name.split("\\s+")[0], 50, 900);
	}

	public void drawDamage(int damage, String avoid) {
		wait = true;
		textFont(f, 30);
		if (avoid.equals("")) {
			text(damage + " Schaden", 50, 950);
		}
		if (avoid.equals("dodge")) {
			text("Abgewehrt", 50, 950);
		}
		if (avoid.equals("miss")) {
			text("Verfehlt", 50, 950);
		}
		if (avoid.equals("heal")) {
			text(damage + " geheilt", 50, 950);
		}
		if (avoid.equals("heal")) {
			text(damage + " Gegner geheilt", 50, 950);
		}
		if (avoid.equals("stunheal")) {
			text(damage + " geheilt und Gegner gelähmt", 50, 950);
		}
		if (avoid.equals("stunyes")) {
			text("Gelähmt", 50, 950);
		}
		if (avoid.equals("stuncrit")) {
			text("Gelähmt für 2 Runden", 50, 950);
		}
		if (avoid.equals("stunno")) {
			text("Wiederstanden", 50, 950);
		}
		if (avoid.equals("power")) {
			text("Spezialangriff regeneriert", 50, 950);
		}
		if (avoid.equals("powersteal")) {
			text("Gekontert!", 50, 950);
		}
		if (avoid.equals("powerfail")) {
			text("Noch verfügbar", 50, 950);
		}
	}

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
		System.out.println(gegner.name + " geht zu Boden! Du hast gewonnen!");
		held.siege++;
		bm.setNextBoss();
		System.exit(0);
	}

	private void onPlayerLose() {
		System.out.println("");
		System.out.println(held.name + " geht zu Boden! Du hast verloren!");
		System.exit(0);
	}

	// ****************************************************************
	// *** Ablauf ***
	// ****************************************************************
	public void startKampf() {
		System.out.println(held.name + " vs " + gegner.name);
		System.out.println(held.getWaffeName() + " vs " + gegner.getWaffeName());
		held.leben = 100;
		heldSchild = 0;
		gegner.leben = 100;
		gegnerSchild = 0;
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

	public void mousePressed() {
		// Item Eingaben
		if (itempressed) {
			if (overButton(item1)) {
				if (held.item[1] != null) {
					itemEinsatz(1);
				}
				itempressed = false;
			}
			if (overButton(item2)) {
				if (held.item[2] != null) {
					itemEinsatz(2);
				}
				itempressed = false;
			}
			if (overButton(item3)) {
				if (held.item[3] != null) {
					itemEinsatz(3);
				}
				itempressed = false;
			}
			// Kampfmenü
		} else {
			if (overButton(btang)) {
				angriff(held, gegner);
				checkWin();
			}
			if (overButton(btspez)) {
				if (!spezialangriffEingesetzt) {
					spezialangriff(held, gegner);
					spezialangriffaktiv=true;
				}
			}
			if (overButton(btdef)) {
				verteidigung(held);
			}
			if (overButton(bti)) {
				itempressed = true;
			}
			if (overButton(btflee)) {
				fliehen();
			}

		}

	}

	// Eine Runde
	private void turn() {
		boolean legitTurn = false;
		if (heldStun == 0) {

		} else {
			System.out.println("");
			System.out.println(held.name + " ist benommen und kann sich nicht bewegen!");
			System.out.println("");
			heldStun--;
		}


		if (gegnerStun == 0) {
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
		redraw();
		checkWin(); // Wurde gewonnen?
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
				if (angreifer.getWaffeName().equals("Master-Schwert")) {
					System.out.println("Das legendäre Master-Schwert blitzt in seiner Hand!");
					schaden = 15;
				} else {
					schaden = 5; // Grundschaden
				}
				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
					System.out.println("Und es ist sehr effektiv!");
					schaden = schaden * 2;
				}
				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv
																								// halbieren
					System.out.println("Aber es ist nicht sehr effektiv!");
					schaden = schaden / 2;
				}
				if (verteidiger.getArmorName().equals("Heldenkleidung")) {
					System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
					schaden = schaden / 2;
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
			if (angreifer.getWaffeName().equals("Master-Schwert")) {
				schaden = 20;
				System.out.println("Das legendäre Master-Schwert blitzt in seiner Hand!");
			} else {
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
			if (verteidiger.getArmorName().equals("Heldenkleidung")) {
				System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
				schaden = schaden / 2;
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
	private boolean itemEinsatz(int index) {
		Item item = null;
		drawAttacker(held);

		// Der übergebene Index(1-3) wird als Item gesetzt, die Anzahl der übrigen Items
		// um einen verringert und wenn keine mehr übrig sind das Item gelöscht
		if (index >= 1 && index <= 3) {
			item = held.item[index];

			if (item.typ.toLowerCase().equals("power") && !spezialangriffEingesetzt) {
				System.out.println("Dein Spezialangriff ist noch verfügbar!");
				drawWeapon(item);
				drawAttacked(held);
				drawDamage(0,"powerfail");
				return false;
			}

			held.item[index].anzahl--;
			if (held.item[index].anzahl == 0) {
				held.item[index] = null;
			}

		}

		drawWeapon(item);
		// Das gesetzte Item bestimmt hier die Wirkung

		// Als Stun Item
		if (item.typ.toLowerCase().equals("stun")) {
			System.out.println(held.name + " benutzt " + item.name + " um den Gegner zu betäuben!");
			drawAttacked(gegner);

			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				gegnerStun = gegnerStun + 2;
				System.out.println("Es ist sehr effektiv und betäubt den Gegner für 2 Runden!");
				drawDamage(0, "stuncrit");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				gegnerStun = 0;
				System.out.println("Doch es macht " + gegner.name + " nichts aus!");
				drawDamage(0, "stunno");
			} else {
				gegnerStun++;
				System.out.println("Der Gegner ist für eine Runde betäubt!");
				drawDamage(0, "stunyes");
			}
			return true;
		}

		// Als Heal Item
		if (item.typ.toLowerCase().equals("heal")) {
			System.out.println(held.name + " isst " + item.name + " um sich zu heilen!");
			drawAttacked(held);
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				held.leben = held.leben + 15;
				gegnerStun++;
				drawDamage(15, "stunheal");
				System.out.println("Und heilt sich damit um 15 Punkte!");
				System.out.println(gegner.name + " wird schlecht als er das sieht! Er ist für eine Runde betäubt!");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				gegner.leben = gegner.leben + 15;
				redraw();
				drawAttacker(held);
				drawAttacked(gegner);
				drawWeapon(item);
				drawDamage(15, "healsteal");
				System.out.println("Doch " + gegner.name + " mag es und stiehlt es daher um es selbst zu essen!");
			} else {
				held.leben = held.leben + 15;
				drawDamage(15, "heal");
				System.out.println("Und heilt sich damit um 15 Punkte!");
			}
			return true;
		}

		// Als Poweritem!
		if (item.typ.toLowerCase().equals("power")) {
			drawAttacked(held);
			System.out.println(held.name + " nutzt " + item.name + " um seinen Spezialangriff zu erholen!");
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				spezialangriffEingesetzt = false;
				gegnerStun++;
				drawDamage(15, "power");
				System.out.println("Der Gegner ist von seinem plötzlichen Kraftanstieg wie gelähmt!");
			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				spezialangriffVorbereitetCOM = true;
				gegnerStun = 0;
				drawDamage(15, "powersteal");
				System.out.println("Doch der Gegner durchschaut dies und kontert mit seinem eigenen Spezialangriff!");
			} else {
				drawDamage(15, "power");
				spezialangriffEingesetzt = false;
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

		if (held.getArmorName().equals("Heldenkleidung")) {
			System.out.println("Die Kleidung des legendären Helden schützt seinen Träger!");
			schaden = schaden / 2;
		}

		held.leben = held.leben - schaden;// Schaden zufügen
		spezialangriffVorbereitetCOM = false;
		checkWin();// Hat Schaden den Sieg gebracht?
	}
}
