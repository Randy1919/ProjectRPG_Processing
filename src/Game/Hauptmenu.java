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
	
	int activeBoss = 0;
	
	PImage[] bossImgs;
	
	int drawMode = 0;
	
	Boolean setUp = false;
	
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
		drawMode = 1;
		activeBoss = nr;
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
			fill(230, 138, 0);
			rect(10, 10, 485, 150);
			fill(0, 0, 0);
			rect(20, 20, 465, 130);		
			
			fill(230, 138, 0);
			rect(505, 10, 485, 150);
			fill(0, 0, 0);
			rect(515, 20, 465, 130);
			
			fill(255, 255, 255);
			text("Spieler:", 50, 100);
			text(spieler.name, 200, 100);
			text("Boss:", 555, 100);
			text(bossManager.bosse[activeBoss].name, 700, 100);

			// Hauptfenster
			fill(230, 138, 0);
			rect(10, 170, 980, 550);
			fill(0, 0, 0);
			rect(20, 180, 960, 530);

			// Ergebnisse und Menü
			rect(10, 730, 485, 260);
			rect(505, 730, 485, 260);

			

			//
			// Innenflächen
			//

			// Lebensbalken
			
			

			// Hauptfenster
			

			// Ergebnisse und Menü
			rect(20, 740, 465, 240);
			rect(515, 740, 465, 240);
			
			
			fill(255, 255, 255);
			text("You have chosen Boss " + bossManager.bosse[activeBoss].name, 150, 400);
		}
	}	
}
