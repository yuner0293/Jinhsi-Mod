//祈愿行动

package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jinhsimod.relics.HJJCsheng;

public class Qiyuan extends AbstractGameAction {
    public Qiyuan(){
        this.actionType = ActionType.SPECIAL;
    }
    @Override
    public void update(){
        HJJCsheng relic = (HJJCsheng) AbstractDungeon.player.getRelic(HJJCsheng.ID);
        if(relic != null) {
            relic.unlockLayer();
        }
        this.isDone = true;
    }
}
