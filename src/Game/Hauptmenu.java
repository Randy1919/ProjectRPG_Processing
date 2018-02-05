package Game;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import Actors.Actor;
import Actors.Boss;
import Actors.BossManager;
import Actors.Spieler;
import Items.Item;
import Items.ItemManager;
import Kampf.Kampf;
import Labyrinth.Labyrinth;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Hauptmenu extends PApplet
{
	ItemManager itemManager;
	BossManager bossManager;
	Spieler spieler;
	
	PImage imgPlayer;
	PImage[] bossImgs;
	
	Boolean setUp = false;
	int drawMode = 0;
	int activeBoss = 0;
	int activeWeapon = 0;
	int activeArmor = 0;
	int[] activeItems = {-1,-1,-1};
	
	Kampf currentKampf;
	Labyrinth currentLabyrinth;
	
	String[] buttons = {"Labyrinth", "Kampf", "Ausrüstung"};
	
	
	public static void main(String[] args) 
	{		
		PApplet.main("Game.Hauptmenu");		
	}
	
	public void setUpMenu()
	{
		itemManager = new ItemManager();
		bossManager = new BossManager(itemManager);
		spieler = new Spieler("Awesomeness");
		
		File f = new File("Held.txt");
		if(f.exists()) 
		{
			spieler.setWaffe(itemManager.getWeaponByName("Master-Schwert"));
			spieler.setArmor(itemManager.getArmorByName("Heldenkleidung"));			
		}
		
		if (new File("Images/player/spielerkampf.png").isFile()) 
		{
			imgPlayer = loadImage("Images/player/spielerkampf.png");
		}
		
		bossImgs = new PImage[bossManager.bosse.length];	
		
		for(int i = 0; i < bossManager.bosse.length; i++)
		{
			if(new File("Bosse/"+bossManager.bosse[i].name+".png").isFile()) 
			{
				bossImgs[i] = loadImage("Bosse/"+bossManager.bosse[i].name+".png");
			}	
		}
			
		setUp = true;
	}
	
	public void chooseBoss(int nr)
	{
		if(nr != 0)
		{
			drawMode = 1;
			activeBoss = nr;
		}	
		else
		{
			drawMode = 0;
		}
	}
	
	public void onButton(int nr)
	{
		if(nr == 0)
		{
			startLabyrinth();
		}
		else if(nr == 1)
		{
			startFight();
		}
		else
		{
			drawMode = 2;
		}
	}
	
	public void startLabyrinth()
	{
		currentLabyrinth = new Labyrinth(this, bossManager);
		drawMode = 4;
	}
	
	public void endLabyrinth()
	{
		currentLabyrinth = null;
		drawMode = 2;
	}
	
	public void startFight()
	{
		spieler.setWaffe(itemManager.waffen[activeWeapon]);
		spieler.setArmor(itemManager.armors[activeArmor]);
		
		//Item[] items = {null, itemManager.items[activeItems[0]],itemManager.items[activeItems[1]],itemManager.items[activeItems[2]]};
		Item[] items = new Item[4];
		for(int i = 0; i < 3; i++)
		{
			if(activeItems[i] < 0)
			{
				items[i+1] = null;
			}
			else
			{
				items[i+1] = itemManager.items[activeItems[i]];
			}
		}
		spieler.item = items;
		bossManager.setBossByInt(activeBoss);
		currentKampf = new Kampf(spieler, bossManager, this);
		drawMode = 3;
	}
	
	public void endFight()
	{
		textFont(createFont("Arial", 18, true), 40);
		currentKampf = null;
		drawMode = 1;
	}
	
	public void nextItem(int slot, boolean previous)
	{
		int length;
		int current;
		
		switch(slot)
		{
		case 0: 
			length = itemManager.waffen.length;
			current = activeWeapon;
			break;
		case 1: 
			length = itemManager.armors.length;
			current = activeArmor;
			break;
		default: 
			length = itemManager.items.length;
			current = activeItems[slot-2];
			break;
		}
		
		if(previous)
		{
			current--;	
		}
		else
		{
			current++;
		}
		
		if(current < 0)
		{
			current = length - 1;
		}
		if(current >= length)
		{
			current = 0;
		}
		
		switch(slot)
		{
		case 0: 
			activeWeapon = current;
			break;
		case 1: 
			activeArmor = current;
			break;
		default: 
			activeItems[slot-2] = current;
			break;
		}
	}
	
	public boolean hovering(int posX, int posY, int width, int height)
	{
		if (mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height)
		{
			return true;
		}
		return false;
	}	
	
	public void keyPressed()
	{
		if(currentLabyrinth != null)
		{
			currentLabyrinth.keyPressed();
		}
	}
	
	public void mousePressed()
	{
		if(drawMode == 0) //Choose Boss Menu
		{
			//BossBoxen	
			int boxDistance = 20;
			int boxWidth = 900 / bossManager.bosse.length;
			int boxInnerWidth = boxWidth - 20;				
			for(int i = 1; i < bossManager.bosse.length; i++)
			{
				//BossBox0
				int posX = boxWidth + boxDistance;
						
				if(i % 2 == 0)
				{
					posX *= i / 2;
				}
				else
				{
					posX *= -i / 2;
				}
				
				if(bossManager.bosse.length % 2 != 0) 
				{
					posX -= (boxWidth + boxDistance)/2;
				}
				
				//box	
				//check if mouse is hovering the box
				if(hovering((500 - (boxWidth / 2)) + posX, (500 - (boxWidth / 2)), boxWidth, boxWidth))
				{
					chooseBoss(i);
				}
			}
		}
		else if(drawMode == 1) //Boss übersicht
		{
			if(hovering(840, 10, 150, 150))
			{
				chooseBoss(0);
			}
			for (int i = 0; i < buttons.length; i++)
			{
				int posY = 60 * i;
				//float fill = 1f;
				fill(120, 160, 230);
				if(hovering(690, 605 + posY, 280, 50))
				{
					onButton(i);
				}
			}
		}
		else if(drawMode == 2) //ausrüstung
		{
			if(hovering(335 + 145, 300 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(0, false);
				}
			}
			if(hovering(335 - 195, 300 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(0, true);
				}
			}
			
			if(hovering(335 + 145, 400 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(1, false);
				}
			}
			if(hovering(335 - 195, 400 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(1, true);
				}
			}
			
			if(hovering(335 + 145, 700 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(2, false);
				}
			}
			if(hovering(335 - 195, 700 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(2, true);
				}
			}
			
			if(hovering(335 + 145, 800 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(3, false);
				}
			}
			if(hovering(335 - 195, 800 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(3, true);
				}
			}
			
			if(hovering(335 + 145, 900 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(4, false);
				}
			}
			if(hovering(335 - 195, 900 - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					nextItem(4, true);
				}
			}
			
			if(hovering(840, 10, 150, 150))
			{
				drawMode = 1;
			}
		}
		else if(drawMode == 3) //kampf
		{
			if(currentKampf != null)
			{
				currentKampf.mousePressed();
			}
		}
		else
		{
			if(hovering(840, 10, 150, 150))
			{
				chooseBoss(0);
			}
		}		
	}
	
	public void settings() 
	{
		size(1000,1000);
	}

	public void setup() 
	{
		background(0);
		//noLoop();
		textFont(createFont("Arial", 18, true), 40);
	}

	public void draw() 
	{
		//Konstruktor
		if(!setUp)
		{
			setUpMenu();
		}
		//Konstruktor
		
		//background(0);
		
		//drawMode = 0;
		if(drawMode == 0) //Choose Boss Menu
		{	
			background(0);
			//Header
			fill(230, 138, 0);
			rect(257, 10, 485, 150);
			
			fill(0,0,0);
			rect(267, 20, 465, 130);
			
			fill(255, 255, 255);
			text("Fate | Processing RPG", 300, 100);
			//Header
			
			fill(255, 255, 255);
			text("Choose a Boss", 375, 340);
			text("~Einführung in die Medieninformatik", 175, 870);
			
			//BossBoxen	
			int boxDistance = 20;
			int boxWidth = 900 / bossManager.bosse.length;
			int boxInnerWidth = boxWidth - 20;				
			for(int i = 1; i < bossManager.bosse.length; i++)
			{
				//BossBox0
				int posX = boxWidth + boxDistance;
						
				if(i % 2 == 0)
				{
					posX *= i / 2;
				}
				else
				{
					posX *= -i / 2;
				}
				
				if(bossManager.bosse.length % 2 != 0) 
				{
					posX -= (boxWidth + boxDistance)/2;
				}
				
				//box	
				//check if mouse is hovering the box
				boolean hovering = hovering((500 - (boxWidth / 2)) + posX, (500 - (boxWidth / 2)), boxWidth, boxWidth);
				//check if mouse is hovering the box
				//fill(230, 138, 0);
				fill(120, 160, 230);
				//if mouse is hovering/down change color of outer box + run method
				if(hovering) 
				{
					fill(137, 137, 137);
					if(mousePressed)
					{
						fill(100, 100, 100);
						//chooseBoss(i);
					}
				}
				//if mouse is hovering/down change color of outer box + run method
				rect((500 - (boxWidth / 2)) + posX, (500 - (boxWidth / 2)), boxWidth, boxWidth);
				
				fill(0,0,0);
				rect((500 - (boxInnerWidth / 2)) + posX, (500 - (boxInnerWidth / 2)), boxInnerWidth, boxInnerWidth);
				//box
				
				//bossimage
				if(bossImgs[i]!=null) 
				{
					float width = boxInnerWidth + 7f;
					float factor = (bossImgs[i].width * 1f) / bossImgs[i].height;					
					image(bossImgs[i], (500 - (width / 2)) + posX + ((width - (factor * width)) / 2), 500 - (width / 2), width * factor, width);	
					//fix resolution, center
				}	
				//bossimage
				//BossBox0
			}
			//BossBoxen		
		}
		else if(drawMode == 1) //Boss Overview Menu
		{
			background(0);
			//header
			fill(230, 138, 0);
			rect(10, 10, 820, 150);
			fill(0, 0, 0);
			rect(20, 20, 800, 130);		
			
			//hero
			//float factor1 = (imgPlayer.width * 1f) / imgPlayer.height;					
			//image(imgPlayer, (830 - (337 / 2)) + ((337 - (factor1 * 337)) / 2), 337 - (337 / 2), 337 * factor1, 337);				
			//image(imgPlayer, (235 - (137 / 2)) + ((137 - (factor1 * 137)) / 2), 82 - (137 / 2), 137 * factor1, 137);
			
			fill(255, 255, 255);
			text("Spieler:", 55, 100);
			text(spieler.name, 300, 100);
			//hero
			//header
			
			//back
			//fill(230, 138, 0);	
			fill(120, 160, 230);
			if(hovering(840, 10, 150, 150))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
					//chooseBoss(0);
				}
			}
			rect(840, 10, 150, 150);
			fill(0, 0, 0);
			rect(850, 20, 130, 130);
			
			fill(255, 255, 255);
			text("Back", 870, 100);
			//back
			
			// Hauptfenster
			fill(230, 138, 0);
			rect(10, 170, 650, 820);
			fill(0, 0, 0);
			rect(20, 180, 630, 800);
			// Hauptfenster
			
			// Bossfenster
			fill(230, 138, 0);
			rect(670, 170, 320, 405);
			fill(0, 0, 0);
			rect(680, 180, 300, 385);
			
			fill(255, 255, 255);
			text("Boss:", 700, 490);
			text(bossManager.bosse[activeBoss].name, 700, 540);
			
			if(bossImgs[activeBoss]!=null) 
			{
				float factor = (bossImgs[activeBoss].width * 1f) / bossImgs[activeBoss].height;		
				image(bossImgs[activeBoss], (830 - (277 / 2)) + ((277 - (factor * 277)) / 2), 317 - (277 / 2), 277 * factor, 277);
			}
			// Bossfenster
			
			// Befehlenster
			fill(230, 138, 0);
			rect(670, 585, 320, 405);
			fill(0, 0, 0);
			rect(680, 595, 300, 385);
			
			//buttons
			for (int i = 0; i < buttons.length; i++)
			{
				int posY = 60 * i;
				//float fill = 1f;
				fill(120, 160, 230);
				if(hovering(690, 605 + posY, 280, 50))
				{	
					fill(137, 137, 137);
					//fill = 0.537f;
					if(mousePressed)
					{
						fill(100, 100, 100);
						//fill = 0.3f;
						//onButton(i);
					}
				}
				//fill(255 * fill, 255 * fill, 255 * fill);
				rect(690, 605 + posY, 280, 50);
				fill(0, 0, 0);
				rect(695, 610 + posY, 270, 40);
				//fill(255 * fill, 255 * fill, 255 * fill);
				fill(255,255,255);
				if(i < buttons.length)
				{
					text(buttons[i], 700, 645 + posY);
				}
				
			}

			//buttons
			// Befehlfenster		
			
			//infos
			fill(255, 255, 255);
			//text("You have chosen Boss " + bossManager.bosse[activeBoss].name, 150, 400);
			text("Infos", 40, 235);
			
			for(int i = 0; i < bossManager.bosse[activeBoss].triviaCategory.length; i++)
			{
				text(bossManager.bosse[activeBoss].triviaCategory[i] + ": ", 80, 290 + (60 * i));
				if(bossManager.bosse[activeBoss].triviaUnlocked[i])
				{
					text(bossManager.bosse[activeBoss].trivia[i], 450, 290 + (60 * i));
				}
				else
				{
					text("???", 450, 290 + (60 * i));
				}		
			}
			//infos
		}
		else if(drawMode == 2)  //ChoseItems
		{
			background(0);
			
			//header
			fill(230, 138, 0);
			rect(10, 10, 820, 150);
			fill(0, 0, 0);
			rect(20, 20, 800, 130);		
			
			//hero
			//float factor1 = (imgPlayer.width * 1f) / imgPlayer.height;					
			//image(imgPlayer, (830 - (337 / 2)) + ((337 - (factor1 * 337)) / 2), 337 - (337 / 2), 337 * factor1, 337);				
			//image(imgPlayer, (235 - (137 / 2)) + ((137 - (factor1 * 137)) / 2), 82 - (137 / 2), 137 * factor1, 137);
			
			fill(255, 255, 255);
			text("Spieler:", 55, 100);
			text(spieler.name, 300, 100);
			//hero
			//header
			
			//back
			//fill(230, 138, 0);
			fill(120, 160, 230);
			if(hovering(840, 10, 150, 150))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(244,244,244);
					//chooseBoss(0);
				}
			}
			rect(840, 10, 150, 150);
			fill(0, 0, 0);
			rect(850, 20, 130, 130);
			
			fill(255, 255, 255);
			text("Back", 870, 100);
			//back
			
			// Bossfenster
			fill(230, 138, 0);
			rect(670, 170, 320, 405);
			fill(0, 0, 0);
			rect(680, 180, 300, 385);
			
			fill(255, 255, 255);
			text("Boss:", 700, 490);
			text(bossManager.bosse[activeBoss].name, 700, 540);
			
			if(bossImgs[activeBoss]!=null) 
			{
				float factor = (bossImgs[activeBoss].width * 1f) / bossImgs[activeBoss].height;		
				image(bossImgs[activeBoss], (830 - (277 / 2)) + ((277 - (factor * 277)) / 2), 317 - (277 / 2), 277 * factor, 277);
			}
			// Bossfenster
			
			// Befehlenster
			fill(230, 138, 0);
			rect(670, 585, 320, 405);
			fill(0, 0, 0);
			rect(680, 595, 300, 385);
			// Befehlfenster
			
			// Hauptfenster
			fill(230, 138, 0);
			rect(10, 170, 650, 820);
			fill(0, 0, 0);
			rect(20, 180, 630, 800);
			
			// kategories	
			//weapon
			{
			int posX = 335;
			int posY = 300;
			fill(230, 138, 0);
			rect(posX - 140, posY - 25, 280, 50);
			fill(0, 0, 0);
			rect(posX - 135, posY - 20, 270, 40);
			fill(255,255,255);
			text(itemManager.waffen[activeWeapon].name, posX - 130, posY + 15);
			//arrowRight
			fill(120, 160, 230);
			if(hovering(posX + 145, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX + 150, posY - 25, posX + 150, posY + 25, posX + 150 + 50, posY);
			fill(0, 0, 0);
			triangle(posX + 150, posY - 20, posX + 150, posY + 20, posX + 150 + 40, posY);
			//arrowRight
			//arrowLeft
			fill(120, 160, 230);
			if(hovering(posX - 195, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX - 150, posY - 25, posX - 150, posY + 25, posX - 150 - 50, posY);
			fill(0, 0, 0);
			triangle(posX - 150, posY - 20, posX - 150, posY + 20, posX - 150 - 40, posY);
			}
			//arrowLeft
			//weapon
			
			//armor
			{
			int posX = 335;
			int posY = 400;
			fill(230, 138, 0);
			rect(posX - 140, posY - 25, 280, 50);
			fill(0, 0, 0);
			rect(posX - 135, posY - 20, 270, 40);
			fill(255,255,255);
			text(itemManager.armors[activeArmor].name, posX - 130, posY + 15);
			//arrowRight
			fill(120, 160, 230);
			if(hovering(posX + 145, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX + 150, posY - 25, posX + 150, posY + 25, posX + 150 + 50, posY);
			fill(0, 0, 0);
			triangle(posX + 150, posY - 20, posX + 150, posY + 20, posX + 150 + 40, posY);
			//arrowRight
			//arrowLeft
			fill(120, 160, 230);
			if(hovering(posX - 195, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX - 150, posY - 25, posX - 150, posY + 25, posX - 150 - 50, posY);
			fill(0, 0, 0);
			triangle(posX - 150, posY - 20, posX - 150, posY + 20, posX - 150 - 40, posY);
			//arrowLeft
			}
			//armor
			
			//item0
			{
			int posX = 335;
			int posY = 700;
			fill(230, 138, 0);
			rect(posX - 140, posY - 25, 280, 50);
			fill(0, 0, 0);
			rect(posX - 135, posY - 20, 270, 40);
			fill(255,255,255);
			if(activeItems[0] == -1)
			{
				text("Kein Item", posX - 130, posY + 15);
			}
			else
			{
				text(itemManager.items[activeItems[0]].name, posX - 130, posY + 15);
			}
			//arrowRight
			fill(120, 160, 230);
			if(hovering(posX + 145, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX + 150, posY - 25, posX + 150, posY + 25, posX + 150 + 50, posY);
			fill(0, 0, 0);
			triangle(posX + 150, posY - 20, posX + 150, posY + 20, posX + 150 + 40, posY);
			//arrowRight
			//arrowLeft
			fill(120, 160, 230);
			if(hovering(posX - 195, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX - 150, posY - 25, posX - 150, posY + 25, posX - 150 - 50, posY);
			fill(0, 0, 0);
			triangle(posX - 150, posY - 20, posX - 150, posY + 20, posX - 150 - 40, posY);
			//arrowLeft
			}
			//item0
			
			//item1
			{
			int posX = 335;
			int posY = 800;
			fill(230, 138, 0);
			rect(posX - 140, posY - 25, 280, 50);
			fill(0, 0, 0);
			rect(posX - 135, posY - 20, 270, 40);
			fill(255,255,255);
			if(activeItems[1] == -1)
			{
				text("Kein Item", posX - 130, posY + 15);
			}
			else
			{
				text(itemManager.items[activeItems[1]].name, posX - 130, posY + 15);
			}
			//arrowRight
			fill(120, 160, 230);
			if(hovering(posX + 145, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX + 150, posY - 25, posX + 150, posY + 25, posX + 150 + 50, posY);
			fill(0, 0, 0);
			triangle(posX + 150, posY - 20, posX + 150, posY + 20, posX + 150 + 40, posY);
			//arrowRight
			//arrowLeft
			fill(120, 160, 230);
			if(hovering(posX - 195, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX - 150, posY - 25, posX - 150, posY + 25, posX - 150 - 50, posY);
			fill(0, 0, 0);
			triangle(posX - 150, posY - 20, posX - 150, posY + 20, posX - 150 - 40, posY);
			//arrowLeft
			}
			//item1
			
			//item2
			{
			int posX = 335;
			int posY = 900;
			fill(230, 138, 0);
			rect(posX - 140, posY - 25, 280, 50);
			fill(0, 0, 0);
			rect(posX - 135, posY - 20, 270, 40);
			fill(255,255,255);
			if(activeItems[2] == -1)
			{
				text("Kein Item", posX - 130, posY + 15);
			}
			else
			{
				text(itemManager.items[activeItems[2]].name, posX - 130, posY + 15);
			}
			//arrowRight
			fill(120, 160, 230);
			if(hovering(posX + 145, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX + 150, posY - 25, posX + 150, posY + 25, posX + 150 + 50, posY);
			fill(0, 0, 0);
			triangle(posX + 150, posY - 20, posX + 150, posY + 20, posX + 150 + 40, posY);
			//arrowRight
			//arrowLeft
			fill(120, 160, 230);
			if(hovering(posX - 195, posY - 25, 50, 50))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(100,100,100);
				}
			}
			triangle(posX - 150, posY - 25, posX - 150, posY + 25, posX - 150 - 50, posY);
			fill(0, 0, 0);
			triangle(posX - 150, posY - 20, posX - 150, posY + 20, posX - 150 - 40, posY);
			//arrowLeft
			}
			//item2
			
			// kategories				
			// Hauptfenster
			
		}
		else if(drawMode == 3 && currentKampf != null) //Kampf
		{
			//currentKampf.draw();
			if(currentKampf.draw())
			{
				endFight();
			}	
		}
		else if(drawMode == 4 && currentLabyrinth != null) //Labyrinth
		{
			currentLabyrinth.draw();
		}
		else
		{
			background(0);
			//back
			//fill(230, 138, 0);
			fill(120, 160, 230);
			if(hovering(840, 10, 150, 150))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(244,244,244);
					//chooseBoss(0);
				}
			}
			rect(840, 10, 150, 150);
			fill(0, 0, 0);
			rect(850, 20, 130, 130);
			
			fill(255, 255, 255);
			text("Back", 870, 100);
			//back
		}
	}	
}
