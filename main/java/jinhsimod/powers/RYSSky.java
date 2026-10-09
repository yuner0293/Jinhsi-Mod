//乘岁凌霄	减伤25%，增伤25%，回合结束时减少一点

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.helpers.ModHelper;



public class RYSSky extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("RYSSky");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/RYSSky84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/RYSSky32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);



    public RYSSky(AbstractCreature owner, int Amount) {
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

    // 每回合结束时减少1层（核心逻辑1）
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 添加减少层数的动作（减少1点，若层数≤0则移除能力）
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, this.ID, 1)
        );
    }

    // 增伤25%（核心逻辑2：修改玩家造成的伤害）
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (owner.isPlayer && type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
            return damage * 1.25f;
        }
        return damage;
    }

    // 减伤25%（核心逻辑3：减少玩家受到的伤害）
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        // 只对玩家受到的伤害生效
        if (owner.isPlayer && type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
            // 减少25%伤害（例如：100点伤害→75点）
            return damage * 0.75f;
        }
        return damage;
    }



    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}