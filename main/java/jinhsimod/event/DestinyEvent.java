
package jinhsimod.event;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import jinhsimod.character.Jinhsicharacter;
import jinhsimod.monster.SuiZhuBoss;

public class DestinyEvent extends AbstractImageEvent {
    public static final String ID = "Jinhsimod:Destiny";
    private static final EventStrings eventStrings;

    // 必须有这几个静态字段，Loadout 和其他 Mod 才能认出你
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        // 增加保底逻辑：如果 JSON 没加载出来，不至于让全游戏崩溃
        NAME = (eventStrings != null) ? eventStrings.NAME : "Destiny";
        DESCRIPTIONS = (eventStrings != null) ? eventStrings.DESCRIPTIONS : new String[]{"..."};
        OPTIONS = (eventStrings != null) ? eventStrings.OPTIONS : new String[]{"[Leave]"};
    }

    public DestinyEvent() {
        super(NAME, DESCRIPTIONS[0], "Jinhsimodresources/img/event/destiny.png");
        // 注意：AbstractImageEvent 会自动处理 body，通常不需要手动再次赋值
        this.imageEventText.setDialogOption(OPTIONS[0]); // [迎战]
        this.imageEventText.setDialogOption(OPTIONS[1]); // [撤退]
        this.hasDialog = true;
        this.hasFocus = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        // 迎战岁主
        if (buttonPressed == 0) {
            //恢复满血
            AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);

            //场景
            AbstractDungeon.scene = new jinhsimod.scenes.CXshan();
            AbstractDungeon.scene.nextRoom(AbstractDungeon.getCurrRoom());

            //音乐
            CardCrawlGame.music.playTempBgmInstantly("CXSHAN_BGM", true);

            AbstractDungeon.player.drawX += -110.0F * Settings.scale;
            AbstractDungeon.player.drawY += -100.0F * Settings.scale;

            AbstractDungeon.player.hb.move(
                    AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x,
                    AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0f
            );

            // 1. 播放音乐（模仿 MindBloom）
            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new SuiZhuBoss(150.0F, 0.0F));

            //3.进场
            this.enterCombatFromImage();

        } else {
            // 撤退
            // 1. 标志位锁定，确保 Patch 不再拦截
            // 2. 设置全局胜利标志（影响分数计算和解锁）
            com.megacrit.cardcrawl.screens.GameOverScreen.isVictory = true;
            // 3. 直接进入结算界面
            // 传入 null 表示没有特定的怪群触发，直接以当前状态通关
            AbstractDungeon.victoryScreen = new com.megacrit.cardcrawl.screens.VictoryScreen(null);
            // 4. 强制修改房间状态，防止其尝试返回地图
            AbstractDungeon.getCurrRoom().phase = com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMPLETE;
        }
    }
}


