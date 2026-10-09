package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.event.ShiShuoEvent;

public class Shishuoaction extends AbstractGameAction {

    public Shishuoaction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.5F; // 给一点点缓冲时间
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {
            // 1. 立即清除动作队列中剩余的所有动作（如多段伤害、遗物触发等）
            AbstractDungeon.actionManager.clearPostCombatActions();

            // 2. 隐藏战斗 UI（能量球、牌堆等）
            AbstractDungeon.overlayMenu.hideCombatPanels();

            // 3. 渐隐背景音乐，防止战斗 BGM 带入事件
            CardCrawlGame.music.fadeOutTempBGM();
        }

        this.tickDuration();

        if (this.isDone) {
            // 4. 强制转换房间相位为 EVENT
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;

            // 5. 实例化并进入事件
            ShiShuoEvent event = new ShiShuoEvent();
            AbstractDungeon.getCurrRoom().event = event;
            event.onEnterRoom();

            // 6. 额外保险：如果是某些特殊 Mod 环境，确保玩家无法在此时操作卡牌
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }
}