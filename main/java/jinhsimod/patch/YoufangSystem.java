package jinhsimod.patch;
//友方补丁
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class YoufangSystem {

    // 1. 【字段注入】
    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class FieldPatch {
        public static SpireField<Boolean> isYoufang = new SpireField<>(() -> false);
        // 新增：内部逃跑锁，防止 update 里疯狂塞 EscapeAction
        public static SpireField<Boolean> isEscaping = new SpireField<>(() -> false);
    }

    // 2. 【核心监听：撤退触发】
    // 拦截 AbstractMonster 的 update，当检测到场上没有敌人时，友方怪自动撤退
    @SpirePatch(clz = AbstractMonster.class, method = "update")
    public static class AutoEscapePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __instance) {
            // 如果我是友方，且还没开始逃跑
            if (FieldPatch.isYoufang.get(__instance) && !FieldPatch.isEscaping.get(__instance)) {

                // 检查场上是否还有真正的敌对怪物
                boolean enemyExists = false;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!FieldPatch.isYoufang.get(m) && !m.isDeadOrEscaped()) {
                        enemyExists = true;
                        break;
                    }
                }

                // 如果敌人死光了，我也撤退
                if (!enemyExists && !AbstractDungeon.getCurrRoom().isBattleOver) {
                    FieldPatch.isEscaping.set(__instance, true);
                    __instance.escaped = true; // 立即标记，让胜利判定能看到
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(__instance));
                }
            }
        }
    }

}