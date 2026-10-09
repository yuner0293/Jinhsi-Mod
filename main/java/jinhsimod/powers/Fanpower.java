/*
67	Jinhsimod:Fan	泛	能力	稀有	反击对全体敌人生效	费用-1	2
 */

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.action.Haveshiyun;
import jinhsimod.helpers.FanJiListener;
import jinhsimod.helpers.ModHelper;

public class Fanpower extends AbstractPower implements FanJiListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Fanpower");
    // Fanpower
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Fan84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Fan32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private static boolean action = false;

    public Fanpower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
        if (!action){
            FanJi.addListener(this);
            action = true;
        }

    }

    @Override
    public void FanJichufa(int amount) {
        // 清除原有的单体伤害动作（关键步骤）
        AbstractDungeon.actionManager.actions.removeIf(action ->
                action instanceof com.megacrit.cardcrawl.actions.common.DamageAction);

        // 添加全体伤害动作
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        this.owner,
                        DamageInfo.createDamageMatrix(amount, true),
                        DamageInfo.DamageType.THORNS,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                )
        );
    }

    //移除
    @Override
    public void onRemove() {
        FanJi.removeListener(this);
        action = false;
        super.onRemove();
    }
    //战斗结束移除
    @Override
    public void onVictory() {
        FanJi.removeListener(this);
        action = false;
        super.onVictory();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }


}