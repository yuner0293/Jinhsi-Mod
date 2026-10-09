//福泽福泽：每回合1次，消耗韶光的攻击牌，每消耗5点韶光，伤害+1

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jinhsimod.helpers.ModHelper;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static jinhsimod.tag.DiyTag.shaoguangxiaohao;
import static jinhsimod.tag.DiyTag.shiyunxiaohao;

public class Fuze extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Fuze");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Fuze84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/Fuze32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private boolean action = false;


    public Fuze(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        this.amount = amount;

        // 使用静态图像资源
        this.region128 = REGION_128;
        this.region48 = REGION_48;
        this.updateDescription();
    }

    private int damageboot = 0;
    @Override
    public void atStartOfTurnPostDraw() {
        action = false;
        damageboot = 0;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type,AbstractCard card) {
        //非荆棘
        if (owner.isPlayer && type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS
                && card.hasTag(shaoguangxiaohao) && !action && card.type == AbstractCard.CardType.ATTACK) {

                    shaoguang shaoguangpower = (shaoguang) player.getPower(shaoguang.POWER_ID);
                    int shaoguanjiancha = (shaoguangpower != null) ? shaoguangpower.amount : 0;
                    damageboot = (shaoguanjiancha / 5)* this.amount;
                    return damage + damageboot;
                }
        return damage;
    }
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        // 检查是否是带有shaoguangxiaohao标签的卡牌且还未触发过
        if (card != null && card.hasTag(shaoguangxiaohao) && !action && card.type == AbstractCard.CardType.ATTACK) {
            action = true;
        }
    }

        public void updateDescription () {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
}

