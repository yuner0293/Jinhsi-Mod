package jinhsimod.event;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import jinhsimod.character.Jinhsicharacter;
import jinhsimod.relics.Drscale;


public class ShiShuoEvent extends AbstractImageEvent {
    public static final String ID = "Jinhsimod:ShiShuoEvent";
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

    public ShiShuoEvent() {
        // 参数：标题，文本，背景图路径
        super(NAME, DESCRIPTIONS[0], "Jinhsimodresources/img/event/ShiShuo.png");

        // 选项 1：不做反应
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                //给予初始遗物
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                        (float)Settings.WIDTH / 2.0F,
                        (float)Settings.HEIGHT / 2.0F,
                        new Drscale()
                );
                CardCrawlGame.nextDungeon = "TheEnding";
                AbstractDungeon.fadeOut();
                AbstractDungeon.isDungeonBeaten = true;
                // 3. 强制结束当前房间阶段，防止点击后还能操作卡牌
                AbstractDungeon.getCurrRoom().phase = com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMPLETE;
                CardCrawlGame.music.fadeOutTempBGM();
                break;
        }
    }
}