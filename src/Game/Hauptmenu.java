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
	
	Kampf currentKampf;
	
	
	public static void main(String[] args) 
	{		
		PApplet.main("Game.Hauptmenu");		
	}
	
	public void SetUpMenu()
	{
		itemManager = new ItemManager();
		bossManager = new BossManager(itemManager);
		spieler = new Spieler("Awesomeness");
		
		spieler.setWaffe(itemManager.getWeaponByName("Kendoschwert"));
		spieler.setArmor(itemManager.getArmorByName("Kendorüstung"));
		spieler.item[1] = itemManager.getItemByName("Powerkugel");
		spieler.item[2] = itemManager.getItemByName("Schneeball");
		spieler.item[3] = itemManager.getItemByName("Pizza Hawaii");
		
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
	
	public void startFight()
	{
		currentKampf = new Kampf(spieler, bossManager, this);
		drawMode = 3;
	}
	
	public void endFight()
	{
		currentKampf = null;
		drawMode = 1;
	}
	
	public boolean hovering(int posX, int posY, int width, int height)
	{
		if (mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height)
		{
			return true;
		}
		return false;
	}	
	
	public void mousePressed()
	{
		if(currentKampf != null)
		{
			currentKampf.mousePressed();
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
			SetUpMenu();
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
				fill(230, 138, 0);
				//if mouse is hovering/down change color of outer box + run method
				if(hovering) 
				{
					fill(137, 137, 137);
					if(mousePressed)
					{
						fill(244,244,244);
						chooseBoss(i);
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
			float factor1 = (imgPlayer.width * 1f) / imgPlayer.height;					
			//image(imgPlayer, (830 - (337 / 2)) + ((337 - (factor1 * 337)) / 2), 337 - (337 / 2), 337 * factor1, 337);				
			image(imgPlayer, (225 - (137 / 2)) + ((137 - (factor1 * 137)) / 2), 82 - (137 / 2), 137 * factor1, 137);
			
			fill(255, 255, 255);
			text("Spieler:", 55, 100);
			text(spieler.name, 300, 100);
			//hero
			//header
			
			//back
			fill(230, 138, 0);	
			if(hovering(840, 10, 150, 150))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(244,244,244);
					chooseBoss(0);
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
			
			float factor = (bossImgs[activeBoss].width * 1f) / bossImgs[activeBoss].height;		
			image(bossImgs[activeBoss], (830 - (277 / 2)) + ((277 - (factor * 277)) / 2), 317 - (277 / 2), 277 * factor, 277);
			// Bossfenster
			
			// Befehlenster
			fill(230, 138, 0);
			rect(670, 585, 320, 405);
			fill(0, 0, 0);
			rect(680, 595, 300, 385);
			
			//buttons
			//button0
			fill(230, 138, 0);		
			if(hovering(690, 605, 280, 50))
			{	
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(244,244,244);
					startFight();
				}
			}
			rect(690, 605, 280, 50);
			//button0
			
			fill(230, 138, 0);	
			rect(690, 665, 280, 50);
			rect(690, 725, 280, 50);
			rect(690, 785, 280, 50);
			rect(690, 845, 280, 50);
			rect(690, 905, 280, 50);
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
		else if(drawMode == 2)  //ChoseItems --wird imom übersprungen--
		{
			drawMode = 3;
		}
		else if(drawMode == 3 && currentKampf != null) //Kampf
		{
			//currentKampf.draw();
			if(currentKampf.draw())
			{
				endFight();
			}	
		}
		else
		{
			background(0);
			//back
			fill(230, 138, 0);	
			if(hovering(840, 10, 150, 150))
			{
				fill(137, 137, 137);
				if(mousePressed)
				{
					fill(244,244,244);
					chooseBoss(0);
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
