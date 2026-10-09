//幻	能力	稀有	每反击敌人2点伤害，获得1点反击	费用-1	2
package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.helpers.FanJiListener;
import jinhsimod.helpers.ModHelper;

public class Huanpower extends AbstractPower implements FanJiListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Huanpower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/huan84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/huan32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private static  boolean action = false;
    private static final int MAX_STACKS = 2;

    public Huanpower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        this.amount = amount;
        if (!action){
            FanJi.addListener(this);
            action =true;
        }
        if (this.amount >= MAX_STACKS){
            this.amount = MAX_STACKS;
        }
        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
    }

    @Override
    public void FanJichufa(int damageamount) {

        int currentamount = (damageamount / 2) * this.amount;
        addToBot(new ApplyPowerAction(this.owner,this.owner,new FanJi(this.owner,currentamount)));
    }
    //移除监听
    @Override
    public void onRemove() {
        FanJi.removeListener(this);
        super.onRemove();
        action = false;
    }

    //叠层
    @Override
    public void stackPower(int stackAmount) {
        // 调用父类逻辑增加数值
        super.stackPower(stackAmount);

        // 强制限制在 MAX_STACKS (2) 以内
        if (this.amount > MAX_STACKS) {
            this.amount = MAX_STACKS;
        }

        // 刷新描述，否则显示的文字可能还是旧的
        this.updateDescription();
    }

    @Override
    public void onVictory() {
        FanJi.removeListener(this);
        super.onVictory();
        action = false;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}