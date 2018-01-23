package Actors;
import Items.Item;

public class Spieler extends Actor{

	public int siege = 0;
	
	public Item[] item= new Item[4];

	
	public Spieler(String n) {
		super(n);
		siege = 0;
	}

}
