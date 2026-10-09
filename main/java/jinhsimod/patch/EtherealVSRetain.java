package jinhsimod.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jinhsimod.modcore.Jinhsimod;

@SpirePatch(
        clz = DiscardAtEndOfTurnAction.class,
        method = "update"
)
public class EtherealVSRetain {

    @SpirePrefixPatch
    public static void Prefix(DiscardAtEndOfTurnAction __instance) {
        // 1. 确保只在今汐角色下生效
        if (AbstractDungeon.player != null && AbstractDungeon.player.getCardColor() == Jinhsimod.Jinhsicardcolor) {

            // 2. 在系统判定哪些牌该保留、哪些牌该丢弃之前
            for (AbstractCard c : AbstractDungeon.player.hand.group) {

                // 3. 如果一张牌具备“虚无”属性
                if (c.isEthereal) {
                    // 核心逻辑：强行关闭它的所有保留标记
                    // 这样系统原生的 DiscardAtEndOfTurnAction 就会把它视为普通牌处理
                    c.retain = false;
                    c.selfRetain = false;
                }
            }
        }
    }
}