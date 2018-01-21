package Actors;
import Items.Item;
import Kampf.Actor;

public class Spieler extends Actor{

	public int siege = 0;
	
	public Item[] item= new Item[4];
	public int[] itemAnzahl= {3,3,3,3};

	
	public Spieler(String n) {
		super(n);
		siege = 0;
	}

}
