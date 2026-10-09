//---------
package jinhsimod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.helpers.ModHelper;
import jinhsimod.monster.SuiZhuBoss;
import jinhsimod.powers.Shiyun;
import jinhsimod.powers.shaoguang;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class Drscale extends CustomRelic {

    // 遗物ID
    public static final String ID = ModHelper.makePath("Drscale");

    // 遗物图片
    private static final String IMG_PATH =
            "Jinhsimodresources/img/relics/Drscale.png";

    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;

    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public Drscale() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Drscale();
    }

    // 监听伤害事件，实现"造成伤害时获得韶光"
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        //过滤，只有战斗中触发
        if (AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return damageAmount; // 非战斗场景直接返回，不执行后续逻辑
        }
        // 过滤条件：玩家造成的非荆棘伤害（不限制目标和伤害值）
        if (info.owner == AbstractDungeon.player &&
                info.type != DamageInfo.DamageType.THORNS) {
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(
                            player,player,new shaoguang(player,1),1)

            );
            this.flash();

        }
        return damageAmount;
    }

    // 每打出一张攻击牌，获得1点时韵
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        // 检查打出的卡牌是否为攻击牌
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 2. 关键过滤：如果目标是岁主且正处于复活态，则不触发
            if (target instanceof SuiZhuBoss) {
                SuiZhuBoss boss = (SuiZhuBoss) target;
                // 检查 halfDead（系统自带）或 isTransforming（你的标志位）
                if (boss.halfDead || boss.phase < 3 && boss.isDeadOrEscaped()) {
                    return; // 直接返回，不闪烁，不给时韵
                }
            }
            // 触发"获得1点时韵"的效果：将自定义Action加入队列顶部
            addToBot(new ApplyPowerAction(
                    player,player,new Shiyun(player,1),1
            ));
            this.flash();
        }
    }
}
