//---------月相观测卡  每回合开始获得1费并触发1次时韵
/*
令尹总是很忙，夜归部署、天工事务、巡尉安防……今州城里的桩桩件件都等着她一一过目。

待办文件堆满了边庭的办公桌，但光凭文件并不能做出最准确，或者说最合适的判断……今汐喜欢亲临现场，亲自倾听民众的诉求。

于是今汐拜托华胥研究院开发了一个方便随身携带的电子版待办清单，以便在外出时随时批阅文件。
 */
package jinhsimod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.skill.Qiyuncard;
import jinhsimod.helpers.ModHelper;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;


public class DBSxiang extends CustomRelic {

    // 遗物ID
    public static final String ID = ModHelper.makePath("DBSxiang");

    // 遗物图片
    private static final String IMG_PATH =
            "Jinhsimodresources/img/relics/DBSxiang.png";

    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.RARE;

    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DBSxiang() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {

        return this.DESCRIPTIONS[0];
    }


    //回合开始时
    @Override
    public void  atTurnStart(){
        //触发时韵
        addToBot(new Haveshiyun(player,0,()->{
        }));
        //能量
        addToBot(new GainEnergyAction(1));
    }
    public AbstractRelic makeCopy() {
        return new DBSxiang();
    }


}
