package Actors;
import Items.Item;
import Kampf.Actor;

public class Spieler extends Actor{

	public int siege = 0;
	
	public Item item1;
	public int item1Anzahl=3;
	
	public Item item2;
	public int item2Anzahl=3;
	
	public Item item3;
	public int item3Anzahl=3;

	
	public Spieler(String n) {
		super(n);
		siege = 0;
	}

}
