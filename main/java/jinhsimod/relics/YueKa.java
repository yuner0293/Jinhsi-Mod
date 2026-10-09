//---------月相观测卡 稀有  每场战斗开始，获得3金币，并在手牌中加入一张唤取
package jinhsimod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import jinhsimod.cards.skill.Qiyuncard;
import jinhsimod.helpers.ModHelper;



public class YueKa extends CustomRelic {

    // 遗物ID
    public static final String ID = ModHelper.makePath("YueKa");

    // 遗物图片
    private static final String IMG_PATH =
            "Jinhsimodresources/img/relics/YueKa.png";

    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.RARE;

    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public YueKa() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {

        return this.DESCRIPTIONS[0];
    }


    //战斗开始时
    @Override
    public void atBattleStart(){
        //金币3
        AbstractDungeon.player.loseGold(3);
        //加入祈愿手牌
        addToBot(new MakeTempCardInHandAction(new Qiyuncard()));
    }

    public AbstractRelic makeCopy() {
        return new YueKa();
    }


}
