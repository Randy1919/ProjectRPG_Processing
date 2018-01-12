package Game;

import Actors.Boss;
import Actors.BossManager;
import Actors.Spieler;

public class Strategie {

	BossManager bm;
	
	Spieler held;
	Boss gegner;
	
	public Strategie(Spieler s, BossManager b) {
		held = s;
		bm=b;
		gegner=bm.getCurrentBoss();
	}
	
	private void onPlayerWin() {
		held.Siege++;
		bm.setNextBoss();
	}
	
}
