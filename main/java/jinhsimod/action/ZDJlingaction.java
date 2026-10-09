/*
addToBot(new XTattack(p,m,this.magicNumber,DamageInfo.DamageType.NORMAL));
 */


package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.powers.FanJi;

public class ZDJlingaction extends AbstractGameAction {
    private final AbstractPlayer player;
    public ZDJlingaction(){
        this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
        this.tickDuration();

        if (this.isDone){
            int monsters = 0;
            //获取怪物
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (monster != null && monster.lastDamageTaken > 0){
                    monsters +=  monster.lastDamageTaken ;
                }
            }
            //获取反击
            if (monsters > 0){
                this.addToBot(new ApplyPowerAction(player,player,new FanJi(player,monsters),monsters));
                monsters = 0;
            }
            this.addToTop(new WaitAction(0.1f));
        }
    }
}
