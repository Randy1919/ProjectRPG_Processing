package Game;

import Actors.Boss;
import Actors.BossManager;

public class Archiv {
	BossManager bm;
	
	Boss gegner;
	
	public Archiv(BossManager b) {
		bm=b;
		gegner=bm.getCurrentBoss();
	}
	
	//Hier kann man sich die Informationen über den Boss ansehen! 
}
