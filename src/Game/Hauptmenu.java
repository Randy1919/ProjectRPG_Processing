package Game;

import Actors.BossManager;
import Items.ItemManager;

public class Hauptmenu 
{
	public static void main(String[] args) 
	{
		ItemManager im = new ItemManager();
		BossManager bo = new BossManager(im);
		//test
	}
}
