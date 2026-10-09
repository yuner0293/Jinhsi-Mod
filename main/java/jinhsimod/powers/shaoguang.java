package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.helpers.ModHelper;
import jinhsimod.relics.HJJCsheng;
import jinhsimod.relics.XLbao;

public class shaoguang extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("shaoguang");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/shaoguang84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/shaoguang32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private boolean hasUnlimitedRelic() {
        return AbstractDungeon.player != null &&
                AbstractDungeon.player.hasRelic(XLbao.ID);
    }

    public shaoguang(AbstractCreature owner, int Amount) {
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.type = AbstractPower.PowerType.BUFF;
    this.amount = Amount;
    // 使用静态图像资源
    this.region128 =  REGION_128;
    this.region48  =  REGION_48;
    // 首次添加能力更新描述
    this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        if (hasUnlimitedRelic()) {
            // 有遗物时无上限
            this.amount += stackAmount;
        } else {
            // 无遗物时保持50上限
            this.amount = Math.min(this.amount + stackAmount, 50);
        }
        this.updateDescription();
        /*
        this.amount = Math.min(this.amount + stackAmount, 50);
        this.updateDescription();
         */
    }

    // 能力修改描述
    public void updateDescription() {
        if (hasUnlimitedRelic()) {
            // 有遗物时显示无上限描述
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } else {
            // 无遗物时显示有上限描述
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

}