package Kampf;

import java.io.File;
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

public class Kampf {

	// Manager
	BossManager bm;
	PApplet hauptmenu;

	// Spieler
	Spieler held;
	boolean spezialangriffEingesetzt;
	int heldSchild;
	int heldStun;
	boolean playerturn;

	// Boss
	Boss gegner;
	boolean spezialangriffVorbereitetCOM;
	int bossspezialUsed;
	int gegnerSchild;
	int gegnerStun;
	boolean bossturn;

	// ****************************************************************
	// *** Konstruktor ***
	// ****************************************************************
	public Kampf(Spieler s, BossManager b, PApplet p) {
		held = s;
		bm = b;
		hauptmenu =p;
		gegner = bm.getCurrentBoss();
	}

	// ****************************************************************
	// *** Ablauf ***
	// ****************************************************************

	//Button Betätigung
	public void mousePressed() {
		// Item Eingaben
		if (playerturn) {
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
				if (overButton(itemback)) {
					itempressed = false;
				}
				// Kampfmenü
			} else {
				if (overButton(btang)) {
					angriff(held, gegner);
				}
				if (overButton(btspez)) {
					if (!spezialangriffEingesetzt) {
						spezialangriff(held, gegner);
						spezialangriffaktiv = true;
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
	}

	// Prüft ob jemand gewonnen hat
	private int checkWin() {
		// Wenn Gegner Leben 0 dann gewonnen!
		if (gegner.leben <= 0) {
			gegner.leben = 0;
			playerturn = false;
			onPlayerWin();
			return 1;
		}

		// Wenn Spieler Leben 0 dann verloren!
		if (held.leben <= 0) {
			held.leben = 0;
			playerturn = false;
			return 2;
		}
		return 0;
	}

	//Wenn Spieler gewinnt
	private void onPlayerWin() {
		held.siege++;
		bm.setNextBoss();
	}

	// ****************************************************************
	// *** Allgemeine Aktionen ***
	// ****************************************************************

	// Ein Angriff
	public void angriff(Actor angreifer, Actor verteidiger) {
		actor = angreifer;
		acted = verteidiger;
		action = angreifer.getWaffeName();
		damagestep = 1;
		avoid = "";

		double damage;
		if (verteidiger.def) {// Wenn Gegner verteidigt kein Schaden
			avoid = "dodge";
			verteidiger.def = false;
			damage = 0;
		} else {// Sonst Berechnung
			if (ThreadLocalRandom.current().nextInt(0, 100 + 1) >= 1) {// Wenn Angriff nicht zufällig daneben geht
				damage = angreifer.getWaffe().schaden; // Grundschaden

				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
					damage = damage * 2;
					effektiv = ": Effektiv";
				}
				if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv
																								// halbieren
					damage = damage / 2;
					effektiv = ": Ineffektiv";
				}
				if (verteidiger.getArmorName().equals("Heldenkleidung")) {
					damage = damage / 2;
					effektiv = ": Geschützt";
				}
			} else {// Wenn der Angriff zufällig daneben geht
				avoid = "miss";
				damage = 0;
			}
		}
		schaden = damage;
		formerLeben = verteidiger.leben;
		lifechanged = true;
		verteidiger.leben = verteidiger.leben - damage;// Schaden zufügen
	}

	// Verteidigen
	private void verteidigung(Actor verteidiger) {
		verteidiger.def = true;// Verteidigung wird gesetzt

		actor = verteidiger;
		acted = verteidiger;
		action = "Verteidigt";
		avoid = "block";
		schaden = 0;
		damagestep = 1;

	}

	// Aufgeben
	private void fliehen() {
		held.leben = 0;
		checkWin();
	}

	// ****************************************************************
	// *** Spieler Aktionen ***
	// ****************************************************************

	// Der Spezialangriff des Spielers
	private void spezialangriff(Actor angreifer, Actor verteidiger) {
		double damage;

		actor = angreifer;
		acted = verteidiger;
		damagestep = 1;
		action = "Spezialangriff";
		avoid = "";

		if (verteidiger.def) {// Wenn Gegner verteidigt kein Schaden
			avoid = "dodge";
			damage = 0;
		} else {// Sonst Berechnung

			damage = angreifer.getWaffe().schaden * 2; // Grundschaden

			if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
				damage = damage + 5;
				effektiv = ": Effektiv";
			}
			if (verteidiger.starkOderSchwach(angreifer.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv halbieren
				damage = damage - 5;
				effektiv = ": Ineffektiv";
			}
			if (verteidiger.getArmorName().equals("Heldenkleidung")) {
				damage = damage / 2;
			}
			if (spezialangriffVorbereitetCOM) {// Wenn Gegner seinen Spezialangriff vorbereitet
				avoid = "spezialcounter";
				spezialangriffVorbereitetCOM = false; // Spezialangriff unterbrechen
				gegnerStun++;
				damage = damage + 5;
			}
		}

		schaden = damage;
		formerLeben = verteidiger.leben;
		lifechanged = true;
		verteidiger.leben = verteidiger.leben - damage;// Schaden zufügen
		spezialangriffEingesetzt = true; // Spezialangriff verwendet
	}

	// Item einsetzen
	private void itemEinsatz(int index) {
		Item item = null;
		actor = held;

		// Der übergebene Index(1-3) wird als Item gesetzt, die Anzahl der übrigen Items
		// um einen verringert und wenn keine mehr übrig sind das Item gelöscht
		if (index >= 1 && index <= 3) {
			item = held.item[index];

			// Powerball und noch Spezialangriff übrig
			if (item.typ.toLowerCase().equals("power") && !spezialangriffEingesetzt) {
				acted = held;
				action = item.name;
				avoid = "powerfail";
				schaden = 0;
				damagestep = 1;
				return;
			}

			held.item[index].anzahl--;
			if (held.item[index].anzahl == 0) {
				held.item[index] = null;
			}

		}

		action = item.name;

		// Das gesetzte Item bestimmt hier die Wirkung

		// Als Stun Item
		if (item.typ.toLowerCase().equals("stun")) {
			acted = gegner;
			schaden = 0;
			damagestep = 1;
			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				gegnerStun = gegnerStun + 2;
				avoid = "stuncrit";
				effektiv = ": Effektiv";

			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				gegnerStun = 0;
				avoid = "stunno";
				effektiv = ": Ineffektiv";

			} else {
				gegnerStun++;
				avoid = "stunyes";
			}
		}

		// Als Heal Item
		if (item.typ.toLowerCase().equals("heal")) {

			acted = held;
			schaden = 15;
			damagestep = 1;

			if (gegner.starkOderSchwach(item.kategorie).equals("stark")) {
				formerLeben = held.leben;
				held.leben = held.leben + 15;
				lifechanged = true;
				gegnerStun++;
				avoid = "stunheal";
				effektiv = ": Effektiv";

			} else if (gegner.starkOderSchwach(item.kategorie).equals("schwach")) {
				formerLeben =  gegner.leben;
				gegner.leben = gegner.leben + 15;
				lifechanged = true;
				acted = gegner;
				avoid = "healsteal";
				effektiv = ": Ineffektiv";

			} else {
				formerLeben =  held.leben;
				held.leben = held.leben + 15;
				lifechanged = true;
				avoid = "heal";
			}
		}

		// Als Poweritem!
		if (item.typ.toLowerCase().equals("power")) {
			acted = held;
			avoid = "power";
			schaden = 0;
			damagestep = 1;

			spezialangriffEingesetzt = false;
		}
	}

	// ****************************************************************
	// *** Gegner Aktionen ***
	// ****************************************************************
	// Eine Gegner Runde
	private void gegnerTurn() {

		if (gegnerStun > 0) {
			actor = gegner;
			acted = gegner;
			action = "Gelähmt";
			avoid = "stun";
			schaden = 0;
			spezialangriffVorbereitetCOM = false;
			damagestep = 1;
		} else {

			if (spezialangriffVorbereitetCOM) {
				gegnerSpezialangriff();
			} else {
				int zufall = ThreadLocalRandom.current().nextInt(0, 100 + 1);
				if (zufall <= 60) {
					angriff(gegner, held);
				}

					if (zufall >= 61 && zufall <= 70) {
						if(bossspezialUsed<3) {
						gegnerSpezialangriffVorbereitung();
						}else {
							angriff(gegner, held);						
						}
					}
					if (zufall >= 71 && zufall <= 80) {
						verteidigung(gegner);
					}

				if (gegner.leben >= 40) {
					if (zufall >= 81 && zufall <= 85) {
						if (gegner.healItem > 0) {
							gegnerItemHeal();
						} else {
							angriff(gegner, held);
						}
					}
					if (zufall >= 86 && zufall <= 100) {

						if (gegner.stunItem > 0) {
							gegnerItemStun();
						} else {
							angriff(gegner, held);
						}
					}
				} else {
					if (zufall >= 81 && zufall <= 85) {
						if (gegner.stunItem > 0) {
							gegnerItemStun();
						} else {
							angriff(gegner, held);
						}
					}
					if (zufall >= 86 && zufall <= 100) {
						if (gegner.healItem > 0) {
							gegnerItemHeal();
						} else {
							angriff(gegner, held);
						}
					}
				}
			}
		}

	}
	
	// Der Spezialangriff des Spielers
	private void gegnerSpezialangriffVorbereitung() {
		spezialangriffVorbereitetCOM = true;
		actor = gegner;
		acted = gegner;
		action = "Vorbereitung";
		avoid = "vor";
		schaden = 0;
		damagestep = 1;
	}

	// Der Spezialangriff des Spielers
	private void gegnerSpezialangriff() {
		double damage;
		actor = gegner;
		acted = held;
		action = "Spezialangriff";
		avoid = "gegnerspezial";

		damage = gegner.getWaffe().schaden * 2; // Grundschaden
		if (held.starkOderSchwach(gegner.getWaffeCat()).equals("stark")) {// Wenn Effektiv verdoppeln
			damage = damage * 2;
			effektiv = ": Effektiv";
		}
		if (gegner.starkOderSchwach(gegner.getWaffeCat()).equals("schwach")) {// Wenn Ineffektiv halbieren
			damage = damage / 2;
			effektiv = ": Ineffektiv";
		}

		if (held.def) {// Wenn Gegner verteidigt halber Schaden
			held.def = false;
			damage = damage / 2;
			avoid = "gegnerspezialbreak";
		}

		if (held.getArmorName().equals("Heldenkleidung")) {
			damage = damage / 2;
			effektiv = ": Geschützt";
		}

		schaden = damage;
		damagestep = 1;
		
		formerLeben =  held.leben;
		lifechanged = true;
		held.leben = held.leben - damage;// Schaden zufügen
		spezialangriffVorbereitetCOM = false;
		gegnerspezialangriffaktiv=true;
		damagestep = 1;
		bossspezialUsed++;
	}

	private void gegnerItemStun() {
		actor = gegner;
		acted = held;
		action = "Lähmt";
		avoid = "stun";
		schaden = 0;
		heldStun++;
		gegner.stunItem--;
		damagestep = 1;
	}

	private void gegnerItemHeal() {
		actor = gegner;
		acted = gegner;
		action = "Heilt";
		avoid = "heal";
		schaden = 15;
		gegner.healItem--;
		lifechanged = true;
		formerLeben =  gegner.leben;
		gegner.leben = gegner.leben + schaden;
		damagestep = 1;
	}
	
	// ****************************************************************
	// *** Grafik ***
	// ****************************************************************
	// Grafik

	public PFont f;

	public boolean wait;
	public boolean itempressed;
	public boolean spezialangriffaktiv = false;
	public boolean gegnerspezialangriffaktiv = false;

	public PImage imgbackground; // Declare a variable of type PImage
	public PImage imgplayer; // Declare a variable of type PImage
	public PImage imgplayerspezial; // Declare a variable of type PImage
	public PImage imgboss; // Declare a variable of type PImage
	public PImage imgbossspezial; // Declare a variable of type PImage
	public PImage imgegg; // Declare a variable of type PImage

	public PImage imgslashplayer; // Declare a variable of type PImage
	public PImage imgslashgegner; // Declare a variable of type PImage
	public PImage imghit1; // Declare a variable of type PImage
	public PImage imghit2; // Declare a variable of type PImage
	public PImage imgheal; // Declare a variable of type PImage
	public PImage imgshieldplayer; // Declare a variable of type PImage
	public PImage imgshieldgegner; // Declare a variable of type PImage

	public Actor actor; // Der Aktive
	public Actor acted; // Der Passive
	public String action;// Das genutze Aktion(oder Item)
	public String avoid;// Wird für drawDamage benötigt
	public String effektiv="";// Wird für drawDamage benötigt
	public double schaden;// Schaden
	public int damagestep;// Indicator für den Draw. 0=Normal, 1= Actor, 2 = action, 3= Acted, 4 =
					// Schaden, 5 = Rundenwechsel
	public double formerLeben;
	public boolean lifechanged = false;
	public boolean end = false;
	public boolean gegnerspezialaktiv=false;
	
	// Button Spalten(X) Positionen
	public float bts1 = 525;
	public float bts2 = 726;

	// Button Reihen(Y) Positionen
	public float btr1 = 760;
	public float btr2 = 830;
	public float btr3 = 900;

	// Button Weite
	public float btmw = 200;
	// Button Höhe
	public float bth = 50;

	// Item Buttons
	public Button item1 = new Button(bts1, btr1, btmw, bth);
	public Button item2 = new Button(bts1, btr2, btmw, bth);
	public Button item3 = new Button(bts1, btr3, btmw, bth);
	public Button itemback = new Button(bts2, btr2, btmw, bth);

	// Menü Buttons
	public Button btang = new Button(bts1, btr1, btmw, bth);
	public Button btspez = new Button(bts2, btr1, btmw, bth);
	public Button btdef = new Button(bts1, btr2, btmw, bth);
	public Button bti = new Button(bts2, btr2, btmw, bth);
	public Button btflee = new Button(bts1, btr3, btmw, bth);
	public Button item6 = new Button(bts2, btr3, btmw, bth);

	public void settings() {
		hauptmenu.size(1000, 1000);
	}

	public void setup() {
		hauptmenu.background(0);
		f = hauptmenu.createFont("Arial", 18, true);
		hauptmenu.textFont(f, 40);
		hauptmenu.frameRate(30);
		wait = false;
		itempressed = false;

		bossspezialUsed = 0;

		held.leben = 100;
		heldSchild = 0;
		gegner.leben = 100;
		gegnerSchild = 0;
		spezialangriffEingesetzt = false;
		heldStun = 0;
		gegnerStun = 0;
		gegner.parseCats();
		end = false;
		playerturn = true;

		if (new File("Images/player/spielerkampf.png").isFile()) {
			imgplayer = hauptmenu.loadImage("Images/player/spielerkampf.png");
		}
		if (new File("Images/player/spielerspezial.jpg").isFile()) {
			imgplayerspezial = hauptmenu.loadImage("Images/player/spielerspezial.jpg");
		}
		if (new File("Images/player/spielerspezial.png").isFile()) {
			imgplayerspezial = hauptmenu.loadImage("Images/player/spielerspezial.png");
		}
		if (new File("Bosse/" + gegner.name + ".png").isFile()) {
			imgboss = hauptmenu.loadImage("Bosse/" + gegner.name + ".png");
		}
		if (new File("Bosse/" + gegner.name + "_spezial.jpg").isFile()) {
			imgbossspezial = hauptmenu.loadImage("Bosse/" + gegner.name + "_spezial.jpg");
		}
		if (new File("Bosse/" + gegner.name + "_spezial.png").isFile()) {
			imgbossspezial = hauptmenu.loadImage("Bosse/" + gegner.name + "_spezial.png");
		}
		if (new File("Images/backgrounds/background.jpg").isFile()) {
			imgbackground = hauptmenu.loadImage("Images/backgrounds/background.jpg");
		}
		if (new File("Images/player/easteregg.png").isFile()) {
			imgegg = hauptmenu.loadImage("Images/player/easteregg.png");
		}
		if (new File("Images/effects/slashboss.png").isFile()) {
			imgslashgegner = hauptmenu.loadImage("Images/effects/slashboss.png");
		}
		if (new File("Images/effects/slashplayer.png").isFile()) {
			imgslashplayer = hauptmenu.loadImage("Images/effects/slashplayer.png");
		}
		if (new File("Images/effects/hit1.png").isFile()) {
			imghit1 = hauptmenu.loadImage("Images/effects/hit1.png");
		}
		if (new File("Images/effects/hit2.png").isFile()) {
			imghit2 = hauptmenu.loadImage("Images/effects/hit2.png");
		}
		if (new File("Images/effects/heal.png").isFile()) {
			imgheal = hauptmenu.loadImage("Images/effects/heal.png");
		}
		if (new File("Images/effects/shieldgegner.png").isFile()) {
			imgshieldgegner = hauptmenu.loadImage("Images/effects/shieldgegner.png");
		}
		if (new File("Images/effects/shieldplayer.png").isFile()) {
			imgshieldplayer = hauptmenu.loadImage("Images/effects/shieldplayer.png");
		}
	}

	public void draw() {
		if (!wait) {
			if (checkWin() == 0) {
				if (damagestep == 0) {
					drawStep0();
				} else if (damagestep == 1) {
					drawStep1();
				} else if (damagestep == 2) {
					drawStep2();
				} else if (damagestep == 3) {
					drawStep3();
				} else if (damagestep == 4) {
					drawStep4();
				} else if (damagestep == 5) {
					drawStep5();
				}
			} else if (checkWin() == 1) {
				drawWin();
				wait = true;
				end = true;
			} else {
				drawLose();
				wait = true;
				end = true;
			}
		} else {
			if (end) {
				hauptmenu.delay(3000);
				hauptmenu.noLoop();
				//Hauptmenu sichbar machen
			} else if(!gegnerspezialaktiv){
				hauptmenu.delay(500);
				wait = false;
			}else {
				hauptmenu.delay(1500);
				wait = false;
				gegnerspezialaktiv=false;
			}
		}
	}

	public void drawStep0() {
		hauptmenu.background(0);
		drawBackground();
		drawBackgroundPicture();
		drawPlayer();
		drawBoss();
		drawLeben();
		if (bossturn) {
			gegnerTurn();
		}
		if (!itempressed) {
			drawTurnButtons();
		} else {
			drawItemButtons();
		}
		if (playerturn) {
			if (heldStun > 0) {
				actor = held;
				acted = held;
				action = "Gelähmt";
				avoid = "stun";
				schaden = 0;
				damagestep = 1;
			}
		}
	}

	public void drawStep1() {
		hauptmenu.background(0);
		drawBackground();
		drawBackgroundPicture();
		if (actor.equals(held)) {
			if (heldStun > 0) {
				drawPlayer();
			} else {
				drawAttackPlayer();
			}
			drawBoss();
		} else {
			drawPlayer();
			if (gegnerStun > 0) {
				drawBoss();
			} else {
				drawAttackBoss();
			}

		}
		if (lifechanged) {
			drawFormerLeben();
		} else {
			drawLeben();
		}

		if (!itempressed) {
			drawTurnButtons();
		} else {
			drawItemButtons();
		}

		playerturn = false;
		drawAttacker(actor);
		wait = true;
		damagestep = 2;
	}

	public void drawStep2() {
		if (spezialangriffaktiv) {
			drawSpezial();
		}
		if (gegnerspezialangriffaktiv) {
			drawGegnerSpezial();
		}
		drawWeapon(action);
		if (action.equals("Master-Schwert") && actor.equals(held)) {
			drawEasterEggPlayer();
		}
		if (action.equals("Master-Schwert") && actor.equals(gegner)) {
			drawEasterEggBoss();
		}
		if (actor.equals(held)) {
			if (acted.equals(held)) {
				if (action.equals("Verteidigt")) {
					if (imgshieldplayer != null) {
						hauptmenu.image(imgshieldplayer, 250, 330, 200, 340);
					}
				} else if (action.equals("Gelähmt")) {
					heldStun--;
				} else {
					if (imgheal != null) {
						hauptmenu.image(imgheal, 130, 350, 200, 340);
					}
				}
			} else {
				if (!avoid.equals("healsteal")) {
					if (!action.equals("Spezialangriff")) {
						if (imgslashplayer != null) {
							hauptmenu.image(imgslashplayer, 230, 350, 200, 340);
						}
					}
				} else {
					if (imgheal != null) {
						hauptmenu.image(imgheal, 690, 350, 200, 340);
					}
				}
			}
		} else {
			if (acted.equals(gegner)) {
				if (action.equals("Verteidigt")) {
					if (imgshieldgegner != null) {
						hauptmenu.image(imgshieldgegner, 600, 330, 200, 340);
					}
				} else if (action.equals("Gelähmt")) {
					gegnerStun--;
				} else if (action.equals("Vorbereitung")) {

				} else {
					if (imgheal != null) {
						hauptmenu.image(imgheal, 690, 350, 200, 340);
					}
				}
			} else {
				if (!action.equals("Spezialangriff")) {
				if (imgslashgegner != null) {
					hauptmenu.image(imgslashgegner, 630, 350, 200, 340);
				}
				}
			}
		}
		wait = true;
		if (action.equals("Verteidigt") || action.equals("Gelähmt") || action.equals("Vorbereitung")) {
			damagestep = 5;
		} else {
			damagestep = 3;
		}
	}

	public void drawStep3() {
		hauptmenu.background(0);
		drawBackground();
		drawBackgroundPicture();
		if (actor.equals(held)) {
			drawAttackPlayer();
			drawBoss();
		} else {
			drawPlayer();
			drawAttackBoss();
		}
		if (lifechanged) {
			drawFormerLeben();
		} else {
			drawLeben();
		}
		if (!itempressed) {
			drawTurnButtons();
		} else {
			drawItemButtons();
		}
		drawAttacker(actor);
		drawWeapon(action);
		drawAttacked(acted);

		if (actor.equals(held)) {
			if (acted.equals(held)) {
				if (imgheal != null) {
					hauptmenu.image(imgheal, 70, 350, 200, 340);
				}
			} else {
				if (!avoid.equals("healsteal")) {
						if (!avoid.equals("dodge")&&!avoid.equals("gegnerspezialbreak")) {
							if (imghit1 != null) {
								hauptmenu.image(imghit1, 710, 350, 200, 340);
							}
						} else {
							hauptmenu.image(imgshieldgegner, 650, 330, 200, 340);
						}
				} else {
					if (imgheal != null) {
						hauptmenu.image(imgheal, 690, 350, 200, 340);
					}
				}
			}
		} else {
			if (acted.equals(gegner)) {
				if (imgheal != null) {
					hauptmenu.image(imgheal, 690, 350, 200, 340);
				}

			} else {
				if (!avoid.equals("dodge")&&!avoid.equals("gegnerspezialbreak")) {
					if (imghit1 != null) {
						hauptmenu.image(imghit1, 150, 350, 200, 340);
					}
				} else {
					hauptmenu.image(imgshieldplayer, 190, 330, 200, 340);
				}
			}
		}

		wait = true;
		damagestep = 4;
	}

	public void drawStep4() {
		hauptmenu.background(0);
		drawBackground();
		drawBackgroundPicture();
		if (actor.equals(held)) {
			drawAttackPlayer();
			drawBoss();
		} else {
			drawPlayer();
			drawAttackBoss();
		}
		if (lifechanged) {
			drawFormerLeben();
		} else {
			drawLeben();
		}
		if (!itempressed) {
			drawTurnButtons();
		} else {
			drawItemButtons();
		}
		drawAttacker(actor);
		drawWeapon(action);
		if (spezialangriffaktiv) {
			spezialangriffaktiv = false;
		}
		if (gegnerspezialangriffaktiv) {
			gegnerspezialangriffaktiv = false;
		}
		drawAttacked(acted);

		if (actor.equals(held)) {
			if (acted.equals(held)) {

			} else {
				if (!avoid.equals("healsteal")) {
						if (!avoid.equals("dodge")) {
							if (imghit2 != null) {
								hauptmenu.image(imghit2, 710, 350, 200, 340);
							}
						} else {
							hauptmenu.image(imgshieldgegner, 650, 330, 200, 340);
						}
				}
			}
		} else {
			if (acted.equals(gegner)) {

			} else {
				if (!avoid.equals("dodge")) {
					if (imghit2 != null) {
						hauptmenu.image(imghit2, 150, 350, 200, 340);
					}
				} else {
					hauptmenu.image(imgshieldplayer, 190, 330, 200, 340);
				}
			}
		}
		drawDamage(schaden, avoid);
		wait = true;
		damagestep = 5;

		if (action.equals("Heldenkleidung") && actor.equals(held)) {
			drawEasterEggPlayer();
		}
		if (action.equals("Heldenkleidung") && actor.equals(gegner)) {
			drawEasterEggBoss();
		}
	}

	public void drawStep5() {
		if (bossturn) {
			playerturn = true;
			held.def=false;
			bossturn = false;
		} else {
			gegner.def=false;
			bossturn = true;
		}
		if (lifechanged) {
			lifechanged = false;
		}
		effektiv="";
		damagestep = 0;
	}

	public void drawBackground() {
		hauptmenu.background(0);
		hauptmenu.textFont(f, 40);
		hauptmenu.fill(230, 138, 0);
		//
		// Umrandungen
		//

		// Lebensbalken
		hauptmenu.rect(10, 10, 485, 150);
		hauptmenu.rect(505, 10, 485, 150);

		// Hauptfenster
		hauptmenu.rect(10, 170, 980, 550);

		// Ergebnisse und Menü
		hauptmenu.rect(10, 730, 485, 260);
		hauptmenu.rect(505, 730, 485, 260);

		hauptmenu.fill(0, 0, 0);

		//
		// Innenflächen
		//

		// Lebensbalken
		hauptmenu.rect(20, 20, 465, 130);
		hauptmenu.rect(515, 20, 465, 130);

		// Hauptfenster
		hauptmenu.rect(20, 180, 960, 530);

		// Ergebnisse und Menü
		hauptmenu.rect(20, 740, 465, 240);
		hauptmenu.rect(515, 740, 465, 240);
	}

	public void drawBackgroundPicture() {
		if (imgbackground != null) {
			hauptmenu.image(imgbackground, 20, 180, 961, 531);
		}
	}

	public void drawSpezial() {
		if (imgplayerspezial != null) {
			hauptmenu.image(imgplayerspezial, 20, 180, 961, 531);
		}
	}

	public void drawGegnerSpezial() {
		if (imgbossspezial != null) {
			hauptmenu.image(imgbossspezial, 20, 180, 961, 531);
			gegnerspezialaktiv=true;
		}
	}

	public void drawPlayer() {
		if (imgplayer != null) {
			hauptmenu.image(imgplayer, 80, 400);
		}
	}

	public void drawAttackPlayer() {
		if (imgplayer != null) {
			hauptmenu.image(imgplayer, 130, 400);
		}
	}

	public void drawEasterEggPlayer() {
		if (imgegg != null) {
			hauptmenu.image(imgegg, 105, 185, 150, 150);
		}
	}

	public void drawEasterEggBoss() {
		if (imgegg != null) {
			hauptmenu.image(imgegg, 745, 185, 150, 150);
		}
	}

	public void drawBoss() {
		if (imgboss != null) {
			hauptmenu.image(imgboss, 720, 350);
		}
	}

	public void drawAttackBoss() {
		if (imgboss != null) {
			hauptmenu.image(imgboss, 670, 350);
		}
	}

	public void drawLeben() {
		//
		// Text
		//
		hauptmenu.textFont(f, 40);
		hauptmenu.fill(255, 255, 255);

		hauptmenu.text(held.name.split("\\s+")[0] + ": ", 50, 100);
		hauptmenu.text(held.leben + "", 355, 100);
		hauptmenu.text(gegner.name.split("\\s+")[0] + ": ", 555, 100);
		hauptmenu.text(gegner.leben + "", 855, 100);
	}

	public void drawFormerLeben() {
		//
		// Text
		//
		hauptmenu.fill(255, 255, 255);

		hauptmenu.text(held.name.split("\\s+")[0] + ": ", 50, 100);
		if (acted.equals(held)) {
			hauptmenu.text(formerLeben + "", 355, 100);
		} else {
			hauptmenu.text(held.leben + "", 355, 100);
		}

		hauptmenu.text(gegner.name.split("\\s+")[0] + ": ", 555, 100);
		if (acted.equals(gegner)) {
			hauptmenu.text( formerLeben + "", 855, 100);
		} else {
			hauptmenu.text( gegner.leben + "", 855, 100);
		}
	}

	public void drawWin() {
		hauptmenu.fill(0, 255, 0, 100);
		hauptmenu.rect(0, 0, 1000, 1000);
		hauptmenu.fill(255, 255, 255, 255);
		hauptmenu.textFont(f, 95);
		hauptmenu.text("Du hast gewonnen!", 100, 350);
	}

	public void drawLose() {
		hauptmenu.fill(255, 0, 0, 100);
		hauptmenu.rect(0, 0, 1000, 1000);
		hauptmenu.tint(255, 255); // Display at half opacity
		hauptmenu.fill(255, 255, 255, 255);
		hauptmenu.textFont(f, 95);
		hauptmenu.text("Du hast verloren!", 150, 350);
	}

	public void drawTurnButtons() {
		hauptmenu.textFont(f, 30);

		// Button 1 (Angriff)
		if (overButton(btang)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("Angriff", btang.positionX + 10, btang.positionY + 40);

		// Button 2 (Spezialangriff)
		if (overButton(btspez)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		if (spezialangriffEingesetzt) {
			hauptmenu.fill(255, 0, 0);
		}
		hauptmenu.text("Spezialangriff", btspez.positionX + 10, btspez.positionY + 40);

		// Button 3 (Verteidigung)
		if (overButton(btdef)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("Verteidigen", btdef.positionX + 10, btdef.positionY + 40);

		// Button 4 (Items)
		if (overButton(bti)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("Items", bti.positionX + 10, bti.positionY + 40);

		// Button 5 (Fliehen)
		if (overButton(btflee)) {
			hauptmenu.	fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("Fliehen", btflee.positionX + 10, btflee.positionY + 40);

		// Button 6
		if (overButton(item6)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("6", item6.positionX + 10, item6.positionY + 40);
	}

	public void drawItemButtons() {
		hauptmenu.textFont(f, 30);

		// Button 1
		if (overButton(item1)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		if (held.item[1] != null) {
			hauptmenu.text(held.item[1].name + ": " + held.item[1].anzahl, item1.positionX + 10, item1.positionY + 40);
		} else {
			hauptmenu.text("Leer", item1.positionX + 10, item1.positionY + 40);
		}

		// Button 2
		if (overButton(item2)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		if (held.item[2] != null) {
			hauptmenu.text(held.item[2].name + ": " + held.item[2].anzahl, item2.positionX + 10, item2.positionY + 40);
		} else {
			hauptmenu.text("Leer", item2.positionX + 10, item2.positionY + 40);
		}

		// Button 3
		if (overButton(item3)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		if (held.item[3] != null) {
			hauptmenu.text(held.item[3].name + ": " + held.item[3].anzahl, item3.positionX + 10, item3.positionY + 40);
		} else {
			hauptmenu.text("Leer", item3.positionX + 10, item3.positionY + 40);
		}

		// Button 4
		if (overButton(itemback)) {
			hauptmenu.fill(150, 150, 150);
		} else {
			hauptmenu.fill(255, 255, 255);
		}
		hauptmenu.text("Zurück", itemback.positionX + 60, itemback.positionY + 40);
	}

	boolean overButton(Button h) {
		if (hauptmenu.mouseX >= h.positionX && hauptmenu.mouseX <= h.positionX + h.weite && hauptmenu.mouseY >= h.positionY
				&& hauptmenu.mouseY <= h.positionY + h.hoehe) {
			return true;
		} else {
			return false;
		}
	}

	public void drawAttacker(Actor attacker) {
		wait = true;
		hauptmenu.textFont(f, 30);
		hauptmenu.text(attacker.name.split("\\s+")[0], 50, 800);
	}

	public void drawWeapon(Item usedItem) {
		wait = true;
		hauptmenu.textFont(f, 30);
		hauptmenu.text(usedItem.name+" "+acted.starkOderSchwach(usedItem.kategorie), 50, 850);
	}

	public void drawWeapon(String usedItem) {
		wait = true;
		hauptmenu.textFont(f, 30);
		hauptmenu.text(usedItem+effektiv, 50, 850);
	}

	public void drawAttacked(Actor defender) {
		wait = true;
		hauptmenu.textFont(f, 30);
		hauptmenu.text(defender.name.split("\\s+")[0], 50, 900);
	}

	public void drawDamage(double damage, String avoid) {
		wait = true;
		hauptmenu.textFont(f, 30);
		if (avoid.equals("")) {
			hauptmenu.text(damage + " Schaden", 50, 950);
		}
		if (avoid.equals("spezialcounter")) {
			hauptmenu.text(damage + " Schaden, Spez. gekontert", 50, 950);
		}
		if (avoid.equals("gegnerspezialbreak")) {
			hauptmenu.text(damage + " Schaden, Durchbrochen", 50, 950);
		}
		if (avoid.equals("dodge")) {
			hauptmenu.text("Abgewehrt", 50, 950);
		}
		if (avoid.equals("miss")) {
			hauptmenu.text("Verfehlt", 50, 950);
		}
		if (avoid.equals("heal")) {
			hauptmenu.text(damage + " geheilt", 50, 950);
		}
		if (avoid.equals("healsteal")) {
			hauptmenu.text(damage + " Gegner geheilt", 50, 950);
		}
		if (avoid.equals("stunheal")) {
			hauptmenu.text(damage + " geheilt, Gegner gelähmt", 50, 950);
		}
		if (avoid.equals("stunyes")) {
			hauptmenu.text("Gelähmt", 50, 950);
		}
		if (avoid.equals("stuncrit")) {
			hauptmenu.text("Gelähmt für 2 Runden", 50, 950);
		}
		if (avoid.equals("stunno")) {
			hauptmenu.text("Wiederstanden", 50, 950);
		}
		if (avoid.equals("power")) {
			hauptmenu.text("Spezialangriff regeneriert", 50, 950);
		}
		if (avoid.equals("powersteal")) {
			hauptmenu.text("Gekontert!", 50, 950);
		}
		if (avoid.equals("powerfail")) {
			hauptmenu.text("Nicht nötig", 50, 950);
		}
	}

}
