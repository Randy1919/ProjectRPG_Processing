package Actors;
import Items.Item;

public class Spieler {
	public String name = "John Doe";

	public Item waffe = null;
	public Item Armor = null;
	public int Siege = 0;

	Spieler(String n) {
		name = n;
		Siege = 0;
	}

}
