package jinhsimod.signatureLib.interfaces;

import com.megacrit.cardcrawl.screens.GameOverScreen;
import jinhsimod.signatureLib.utils.EasyUnlock;


import java.util.ArrayList;

public interface EasyUnlockSubscriber {
	default EasyUnlock receiveOnGameOver(GameOverScreen screen) {
		return null;
	}
	
	default ArrayList<EasyUnlock> receiveOnGameOverMultiUnlocks(GameOverScreen screen) {
		return null;
	}
}
