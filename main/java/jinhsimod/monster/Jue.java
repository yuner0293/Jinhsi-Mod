package jinhsimod.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import jinhsimod.patch.YoufangSystem;


public class Jue extends AbstractMonster {
    public static final String ID = "Jinhsimod:Jue";
    public static final MonsterStrings monsterStrings;
    public static final String NAME;
    public Jue( final float x,final float y){
        super(NAME,
                ID,
                10,
                0.0f,
                0.0f,
                250.0f * Settings.scale,
                270.0f * Settings.scale,
                "Jinhsimodresources/img/monster/shenghaiJue.png",
                x,y
                );
        this.setHp(20,20);
        this.damage.add(new DamageInfo((AbstractCreature)this, 20));
        //位置偏移
        this.drawX = AbstractDungeon.player.drawX + 300.0f * Settings.scale;
        this.drawY = AbstractDungeon.player.drawY;
        //
        this.hb.move(this.drawX + this.hb_x, this.drawY + this.hb_y);
        this.healthHb.move(this.hb.cX, this.hb.y - this.healthHb.height);
        //
        this.type = EnemyType.NORMAL;
        YoufangSystem.FieldPatch.isYoufang.set(this, true);

    }

    //入场效果  无
    @Override
    public void usePreBattleAction(){
        super.usePreBattleAction();

    }

    //单位回合行动
    @Override
    public void takeTurn() {
        addToBot(new DamageAction(
                this, new DamageInfo(
                this, 20, DamageInfo.DamageType.NORMAL)
        ));

    }

    //行动意图
    @Override
    protected void getMove(int num){
        this.setMove(
                (byte) 0,                  // 行动ID
                Intent.NONE,             //
                0                        //
        );
    }


    //死亡效果
    @Override
    public void die() {
        super.die();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && monster != this) {
                // 造成30点伤害
                AbstractDungeon.actionManager.addToBottom(new DamageAction(
                        monster,
                        new DamageInfo(this, 30, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.FIRE
                ));

                // 附加2层易伤
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        monster,
                        this,
                        new VulnerablePower(monster, 2, false),
                        2,
                        true,
                        AbstractGameAction.AttackEffect.NONE
                ));
            }
        }

    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
    }

}
