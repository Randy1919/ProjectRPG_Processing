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
	String[] infoStrings;
	
	Boolean setUp = false;
	int drawMode = 0;
	int activeBoss = 0;
	
	boolean[] infosFound;	
	
	
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
		
		//infoStrings = new String[7];
		infoStrings =  new String[] {"Name", "Age", "Age", "Age", "Age", "Age", "Age", "Age", "Age", "Age"};
		infosFound = new boolean[7];
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

	ItemManager im = new ItemManager();
	BossManager bo = new BossManager(im);
	Spieler hero = new Spieler("Spieler");
	
	public void chooseBoss(int nr)
	{
		if(nr != 0)
		{
			infosFound = new boolean[7];
			drawMode = 1;
			activeBoss = nr;
		}	
		else
		{
			drawMode = 0;
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
		
		background(0);
		
		//drawMode = 0;
		if(drawMode == 0) //Choose Boss Menu
		{	
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
				
				int iCheck = i; 
				if(bossManager.bosse.length % 2 != 0) {iCheck++;}
				if(iCheck % 2 == 0)
				{
					posX *= iCheck / 2;
				}
				else
				{
					posX *= -iCheck / 2;
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
			//header
			fill(230, 138, 0);
			rect(10, 10, 820, 150);
			fill(0, 0, 0);
			rect(20, 20, 800, 130);		
			
			//boss
			float factor = (bossImgs[activeBoss].width * 1f) / bossImgs[activeBoss].height;					
			image(bossImgs[activeBoss], (225 - (137 / 2)) + ((137 - (factor * 137)) / 2), 82 - (137 / 2), 137 * factor, 137);
			
			fill(255, 255, 255);
			text("Boss:", 55, 100);
			text(bossManager.bosse[activeBoss].name, 300, 100);
			//boss
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
			
			// Spielerfenster
			fill(230, 138, 0);
			rect(670, 170, 320, 405);
			fill(0, 0, 0);
			rect(680, 180, 300, 385);
			
			fill(255, 255, 255);
			text("Spieler:", 700, 490);
			text(spieler.name, 700, 540);
			
			//float factor1 = (imgPlayer.width * 1f) / imgPlayer.height;					
			//image(imgPlayer, (830 - (337 / 2)) + ((337 - (factor1 * 337)) / 2), 337 - (337 / 2), 337 * factor1, 337);
			image(bossImgs[activeBoss], (830 - (277 / 2)) + ((277 - (factor * 277)) / 2), 317 - (277 / 2), 277 * factor, 277);
			// Spielerfenster
			
			// Befehlenster
			fill(230, 138, 0);
			rect(670, 585, 320, 405);
			fill(0, 0, 0);
			rect(680, 595, 300, 385);
			// Befehlfenster
			
			//rects
			fill(137, 137, 137);
			rect(690, 605, 280, 50);
			rect(690, 665, 280, 50);
			rect(690, 725, 280, 50);
			rect(690, 785, 280, 50);
			rect(690, 845, 280, 50);
			rect(690, 905, 280, 50);
			//rects
			
			
			
			
			

			
			
			
			//float factor = (bossImgs[activeBoss].height * 1f) / bossImgs[activeBoss].width;
			//image(bossImgs[activeBoss], 155, 17, 137, 137 * factor);

			
			
			fill(255, 255, 255);
			//text("You have chosen Boss " + bossManager.bosse[activeBoss].name, 150, 400);
			text("Infos:", 55, 250);
			
			for(int i = 0; i < infoStrings.length; i++)
			{
				text(infoStrings[i] + ": ", 100, 310 + (60 * i));
				text("" + infosFound[0], 277, 310 + (60 * i));
			}
		}
	}	
}
