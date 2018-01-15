package Kampf;

import Actors.Boss;
import Actors.BossManager;
import Actors.Spieler;

public class Kampf {

	BossManager bm;
	
	Spieler held;
	Boss gegner;
	
	public Kampf(Spieler s, BossManager b) {
		held = s;
		bm=b;
		gegner=bm.getCurrentBoss();
	}
	
	private void onPlayerWin() {
		held.Siege++;
		bm.setNextBoss();
	}
	
}
