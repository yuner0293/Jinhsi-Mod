
package jinhsimod.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import jinhsimod.event.DestinyEvent;


public class JinhsiFinalEventPatch {
    // 判断是否需要劫持跳转到事件
    public static boolean shouldRedirectToEvent() {
        // 引用主类中实时更新的布尔值
        if (!jinhsimod.modcore.Jinhsimod.enableSuiZhuBoss) {
            return false;
        }
        return AbstractDungeon.id.equals("TheEnding") && isPlayerJinhsi();
    }

    private static boolean isPlayerJinhsi() {
        return AbstractDungeon.player != null &&
                AbstractDungeon.player.chosenClass == jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsi;
    }

    public static void doRedirect() {
        System.out.println("=== [Jinhsi] 准备劫持跳转（原地替换模式） ===");

        CardCrawlGame.stopClock = false;
        AbstractDungeon.closeCurrentScreen();
        CardCrawlGame.music.fadeOutBGM();

        // 1. 暴力清理奖池：即便房间重置抽签，也只能抽到你
        AbstractDungeon.eventList.clear();
        AbstractDungeon.shrineList.clear();
        AbstractDungeon.specialOneTimeEventList.clear();
        AbstractDungeon.eventList.add(jinhsimod.event.DestinyEvent.ID);
        System.out.println("=== [Jinhsi] 奖池清理完毕 ===");

        // 2. 获取当前节点（心脏所在的节点）
        MapRoomNode currNode = AbstractDungeon.currMapNode;
        if (currNode == null) {
            System.out.println("=== [Jinhsi] 错误：当前节点为空！ ===");
            return;
        }

        // 3. 原地替换：把当前节点的房间换成事件房，并塞入你的事件实例
        EventRoom eventRoom = new EventRoom();
        eventRoom.event = com.megacrit.cardcrawl.helpers.EventHelper.getEvent(jinhsimod.event.DestinyEvent.ID);

        // 关键点：不仅要给下个房间赋值，还要把当前地图节点的房间直接改掉
        currNode.room = eventRoom;
        AbstractDungeon.nextRoom = currNode;

        System.out.println("=== [Jinhsi] 原地替换完成，目标: " + eventRoom.event.getClass().getSimpleName());

        // 4. 启动转场
        // 由于 nextRoom == currMapNode，游戏会认为是在“原地重进”一个新房间
        AbstractDungeon.nextRoomTransitionStart();

        // 5. 隐藏 UI
        AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
        System.out.println("=== [Jinhsi] 转场已启动 ===");
    }


    // 补丁：在心脏死亡时提前恢复时钟
    @SpirePatch(clz = CorruptHeart.class, method = "die")
    public static class HeartDiePatch {
        @SpirePostfixPatch
        public static void Postfix(CorruptHeart __instance) {
            if (isPlayerJinhsi()) {
                CardCrawlGame.stopClock = false;
            }
        }
    }


    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class RedirectPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    // 劫持去往真结局的唯一入口
                    if (m.getMethodName().equals("goToTrueVictoryRoom")) {
                        // 如果满足条件，执行 doRedirect 且【不再】执行原版的 $proceed($$)
                        m.replace(String.format(
                                "{ if (%s.shouldRedirectToEvent()) { %s.doRedirect(); } " +
                                        "else { $proceed($$); } }",
                                JinhsiFinalEventPatch.class.getName(),
                                JinhsiFinalEventPatch.class.getName()
                        ));
                    }
                }
            };
        }
    }
}