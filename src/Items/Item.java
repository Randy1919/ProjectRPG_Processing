package Items;

public class Item {
	public String name = "";
	public String kategorie = "";
	public String starkGegen ="";
	public String schwachGegen ="";
	public int slot = 99;
	public String beschreibung = "";

	public Item(String n, String k, String stg,String swg, int s, String b) {
		name = n;
		kategorie = k;
		starkGegen=stg;
		schwachGegen=swg;
		slot = s;
		beschreibung = b;
	}
	
	public Item(String n) {
		name=n;
	}

}
