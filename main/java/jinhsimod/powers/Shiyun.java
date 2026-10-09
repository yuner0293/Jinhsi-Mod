package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.helpers.ModHelper;

public class Shiyun extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Shiyun");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Shiyun84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Shiyun32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);



    public Shiyun(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;

        // 首次添加能力更新描述
        this.updateDescription();
    }


    @Override
    public void stackPower(int stackAmount) {
        // 叠加时检查：新数值 = 当前值 + 叠加值，若超过上限则设为上限(4)
        this.amount = Math.min(this.amount + stackAmount, 4);
        // 更新描述（如数值变化后需要刷新显示）
        this.updateDescription();
    }

    // 防止通过其他方式绕过上限
    public void setAmount(int newAmount) {
        this.amount = Math.min(newAmount, 4);
        this.updateDescription();
    }

    // 能力在更新时修改描述（建议在本地化文件中说明上限）
    public void updateDescription() {
        // 假设本地化文件中DESCRIPTIONS[0]是前缀，DESCRIPTIONS[1]是后缀，例如：
        // DESCRIPTIONS[0] = "你的时韵为"，DESCRIPTIONS[1] = "点（上限4点）。"
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}