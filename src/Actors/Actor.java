package Actors;

import java.util.Locale;

import Items.Item;

public class Actor {
	public String name = "John Doe";
	private Item waffe = null;
	private Item armor = null;
	public double leben =100;
	public boolean def=false;
	private String[] schwaechen;
	private String[] staerken;

	public Actor(String n) {
		name = n;
		
		schwaechen = new String[4];
		staerken= new String[5];
		for(int i=0;i<schwaechen.length;i++) {
			schwaechen[i]="";
		}
		for(int i=0;i<staerken.length;i++) {
			staerken[i]="";
		}
	}
	
	public Actor(String n,Item wep,Item arm) {
		name = n;
		waffe=wep;
		armor=arm;
		
		schwaechen = new String[4];
		staerken= new String[5];
		for(int i=0;i<schwaechen.length;i++) {
			schwaechen[i]="";
		}
		for(int i=0;i<staerken.length;i++) {
			staerken[i]="";
		}
	}
	
	//Setzt Waffe
	public void setWaffe(Item i) {
		if(i.slot==0) {
			waffe=i;
			System.out.println(name+" Waffe ausgerüstet: "+waffe.name);
		}
	}
	
	//Setzt Rüstung
	public void setArmor(Item i) {
		if(i.slot==1) {
			armor=i;
			System.out.println(name+" Rüstung ausgerüstet: "+armor.name);
		}
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
	
	//Gibt Waffe zurück
	public Item getWaffe() {
		if(waffe!=null) {
		return waffe;
		}else {return null;}
	}
	
	//Gibt Rüstung zurück
	public Item getArmor() {
		if(armor!=null) {
		return armor;
		}else {return null;}
	}
	
	//Gibt Name der Waffe zurück
	public String getWaffeName() {
		if(waffe!=null) {
		return waffe.name;
		}else {return "";}
	}

	//Gibt Name der Rüstung zurück
	public String getArmorName() {
		if(armor!=null) {
		return armor.name;
		}else {return "";}
	}
	
	//Parsed Schwächen
	//0=Rüstungsschwäche
	//Parsed Stärken
	//0=Rüstungsstärke
	public void parseCats() {
		schwaechen = new String[1];
		
		schwaechen[0]=armor.schwachGegen;
		
		staerken= new String[1];
		
		staerken[0]=armor.starkGegen;
	}
	


	//Gibt zurück ob übergebene Kategorie schwach oder stark gegen den Actor ist
	public String starkOderSchwach(String s) {
		
		for(int i=0;i<schwaechen.length;i++) {
			if(s.toLowerCase().equals(schwaechen[i].toLowerCase())) {
				return "stark";
			}
		}
		
		for(int i=0;i<schwaechen.length;i++) {
			if(s.toLowerCase().equals(staerken[i].toLowerCase())) {
				return "schwach";
			}
		}
		
		return "";
	}

}
