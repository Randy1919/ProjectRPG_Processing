package Kampf;

import Items.Item;

public class Actor {
	public String name = "John Doe";
	public Item waffe = null;
	public Item armor = null;
	public double leben =100;
	public boolean def=false;

	public Actor(String n) {
		name = n;
	}
	
	public Actor(String n,Item wep,Item arm) {
		name = n;
		waffe=wep;
		armor=arm;
	}
	
	//Gibt Kategorie der Waffe zurück
	public String getWaffeCat() {
		if(waffe!=null) {
		return waffe.kategorie;
		}else {return "";}
	}

	//Gibt Kategorie der Rüstung zurück
	public String getArmorCat() {
		if(armor!=null) {
		return armor.kategorie;
		}else {return "";}
	}

}
