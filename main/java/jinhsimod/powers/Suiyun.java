//岁蕴

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jinhsimod.action.Haveshiyun;
import jinhsimod.helpers.ModHelper;
import jinhsimod.helpers.ShiyunListener;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
public class Suiyun extends AbstractPower implements ShiyunListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Suiyun");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Suiyun84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Suiyun32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private  boolean triggeredThisTurn = true;

    public Suiyun(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        this.amount = amount;

        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
    }



    // --- 标准化注册 ---
    @Override
    public void onInitialApplication() {
        Haveshiyun.addListener(this);
    }

    // 回合开始重置标记
    @Override
    public void atStartOfTurn() {
        this.triggeredThisTurn = false;
    }

    // 时韵触发回调
    @Override
    public void Shiyunchufa(int triggerThreshold) {
        if (!triggeredThisTurn) {
            this.flash();
            // 获得力量
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, this.amount), this.amount));
            this.triggeredThisTurn = true;
        }
    }

    //移除监听
    @Override
    public void onRemove() {
        Haveshiyun.removeListener(this);
    }

    @Override
    public void onVictory() {
        Haveshiyun.removeListener(this);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}