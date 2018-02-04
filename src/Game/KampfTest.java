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

public class KampfTest 
{
	public boolean drawKampf(Hauptmenu app)
	{
		//Header
		boolean done = false;
		
		app.fill(230, 138, 0);
		if(app.hovering(257, 10, 485, 150)) 
		{
			app.fill(137, 137, 137);
			if(app.mousePressed)
			{
				done = true;
				app.fill(244,244,244);
			}
		}		
		app.rect(257, 10, 485, 150);
		
		app.fill(0,0,0);
		app.rect(267, 20, 465, 130);
		
		app.fill(255, 255, 255);
		app.text("Fate | Processing RPG", 300, 100);
		//Header
		
		return done;
	}
	
}
