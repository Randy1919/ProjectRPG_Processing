package Game;

import java.io.File;

import Actors.BossManager;
import Actors.Spieler;
import Items.ItemManager;
import Kampf.Button;
import Kampf.Kampf;
import processing.core.PApplet;
import processing.core.PFont;

public class Hauptmenu extends PApplet {
	PFont f;

	public static void main(String[] args) {
		String[] argu = { "--location=200,200", "Game.Hauptmenu" };
		Hauptmenu h = new Hauptmenu();
		PApplet.runSketch(argu, h);
	}

	ItemManager im = new ItemManager();
	BossManager bo = new BossManager(im);
	Spieler hero = new Spieler("Spieler");
	
	public Hauptmenu() {
		hero.setWaffe(im.getWeaponByName("Unbewaffnet"));
		hero.setArmor(im.getArmorByName("Nackt"));
		hero.item[1] = null;
		hero.item[2] = null;
		hero.item[3] = null;

		File f = new File("Held.txt");
		if (f.exists()) {
			hero.setWaffe(im.getWeaponByName("Master-Schwert"));
			hero.setArmor(im.getArmorByName("Heldenkleidung"));
		}
	}

	public void settings() {
		size(1000, 1000);
	}

	public void setup() {

	}

	public void draw() {
		background(0);
		f = createFont("Arial", 18, true);
		textFont(f, 40);
		frameRate(30);

		drawBackground();
		drawButtons();
	}

	// Button Spalten(X) Positionen
	float bts1 = 400;

	// Button Reihen(Y) Positionen
	float btr1 = 300;
	float btr2 = 351;
	float btr3 = 402;
	float btr4 = 453;
	float btr5 = 504;

	// Button Weite
	float btmw = 200;
	// Button Höhe
	float bth = 50;

	// Menü Buttons
	Button btlab = new Button(bts1, btr1, btmw, bth);
	Button btknow = new Button(bts1, btr2, btmw, bth);
	Button btequip = new Button(bts1, btr3, btmw, bth);
	Button btbattle = new Button(bts1, btr4, btmw, bth);
	Button btend = new Button(bts1, btr5, btmw, bth);

	public void drawButtons() {
		textFont(f, 35);

		// Button 1 (Labyrinth)
		if (overButton(btlab)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Labyrinth", btlab.positionX + 10, btlab.positionY + 40);

		// Button 2 (Knowledge)
		if (overButton(btknow)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Archiv", btknow.positionX + 10, btknow.positionY + 40);

		// Button 3 (Battle)
		if (overButton(btequip)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Ausrüstung", btequip.positionX + 10, btequip.positionY + 40);

		// Button 4 (Items)
		if (overButton(btbattle)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Kampf", btbattle.positionX + 10, btbattle.positionY + 40);

		// Button 5 (Fliehen)
		if (overButton(btend)) {
			fill(150, 150, 150);
		} else {
			fill(255, 255, 255);
		}
		text("Beenden", btend.positionX + 10, btend.positionY + 40);
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

	boolean overButton(Button h) {
		if (mouseX >= h.positionX && mouseX <= h.positionX + h.weite && mouseY >= h.positionY
				&& mouseY <= h.positionY + h.hoehe) {
			return true;
		} else {
			return false;
		}
	}

	public void mousePressed() {
		
		if (overButton(btlab)) {
			
		}
		if (overButton(btknow)) {

		}
		if (overButton(btequip)) {

		}
		if (overButton(btbattle)) {
			String[] argu = { "--location=200,200", "Kampf.Kampf" };
			Kampf k = new Kampf(hero, bo);
			PApplet.runSketch(argu, k);
		}
		if (overButton(btend)) {
			System.exit(0);
		}
	}
}
