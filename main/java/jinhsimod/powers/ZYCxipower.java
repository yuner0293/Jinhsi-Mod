
/*
Jinhsimod:ZYCxi	朝月辰夕	技能	罕见	"本回合内，每触发1次时韵
减少当前手牌费用1点"	费用-1	2

 */

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.action.Haveshiyun;
import jinhsimod.helpers.ModHelper;
import jinhsimod.helpers.ShiyunListener;

public class ZYCxipower extends AbstractPower implements ShiyunListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("ZYCxipower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/ZYCxi84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/ZYCxi32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    public ZYCxipower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;


        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();

    }


    //每回合开始，清空
    @Override
    public void atStartOfTurn() {
        if (this.amount == 0) {
            // 层数为0时移除能力
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ZYCxipower.POWER_ID));
        } else {
            // 层数>0时，减少1层
            this.addToBot(new ReducePowerAction(this.owner, this.owner, ZYCxipower.POWER_ID, 1));
        }
    }

    @Override
    public void onInitialApplication() {
        Haveshiyun.addListener(this);
    }

    @Override
    public void Shiyunchufa(int triggerThreshold){
            for (AbstractCard card : AbstractDungeon.player.hand.group){
                if (card.cost > 0 ){
                    card.setCostForTurn(card.costForTurn-1);
                }

            }
    }

    @Override
    public void onRemove() {
        Haveshiyun.removeListener(this);
    }
    @Override
    public void onVictory() {
        Haveshiyun.removeListener(this);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}