package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.helpers.ModHelper;

public class JinGu extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("JinGu");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/JinGu84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/JinGu32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private boolean hasSavedIntent = false;
    private byte originalMove = -1;
    private AbstractMonster.Intent originalIntent = AbstractMonster.Intent.NONE;

    public JinGu(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        if(this.owner instanceof AbstractMonster){
            actionjingu();
        }
        this.updateDescription();
    }
    //清空意图
    private void actionjingu(){
        if (this.owner instanceof AbstractMonster) {
            AbstractMonster monster = (AbstractMonster) this.owner;
            if (monster.isDead || monster.isDying || monster.halfDead){
                return;
            }
            // 只保存一次原始意图
            if (!hasSavedIntent) {
                if (monster.intent != AbstractMonster.Intent.NONE){
                    originalMove = monster.nextMove;
                    originalIntent = monster.intent;
                    hasSavedIntent = true;
                }
            }
            // 清空敌人行动
            monster.setMove((byte) -1, AbstractMonster.Intent.NONE);
            monster.createIntent();
        }
    }

    private void restoreIntent(){
        if (this.owner instanceof AbstractMonster && hasSavedIntent) {
            AbstractMonster monster = (AbstractMonster) this.owner;
            if (!monster.isDead && !monster.isDying) {
                // 恢复原始意图
                monster.setMove(originalMove, originalIntent);
                monster.createIntent();
            }
        }
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner instanceof AbstractMonster) {
            // 减少持续回合数
            this.amount--;
            if (this.amount >= 0){
                actionjingu();
            }
            if (this.amount <= 0) {
                // 效果结束，移除这个Power
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(this.owner, this.owner, this.ID)
                );
            }
            updateDescription();
        }
    }

    @Override
    public void onRemove() {
        restoreIntent();
    }
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}