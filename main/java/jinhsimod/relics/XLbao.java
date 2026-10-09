//---------
package jinhsimod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import jinhsimod.helpers.ModHelper;
import jinhsimod.powers.shaoguang;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class XLbao extends CustomRelic {

    // 遗物ID
    public static final String ID = ModHelper.makePath("XLbao");

    // 遗物图片
    private static final String IMG_PATH =
            "Jinhsimodresources/img/relics/XLBao.png";

    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;

    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public XLbao() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public int shaoguangnulmber = 0;

    @Override
    public void onEquip(){
        if (player != null){
            shaoguang power = (shaoguang) AbstractDungeon.player.getPower(shaoguang.POWER_ID);
            if (power != null){
                power.updateDescription();
            }
        }

    }

    //回合开始失去1/3层韶光
    @Override
    public void atTurnStartPostDraw() {
        shaoguang shaoguangPower = (shaoguang) player.getPower(shaoguang.POWER_ID);
        if (shaoguangPower == null || shaoguangPower.amount <= 0) {
            return; // 如果没有韶光能力或层数<=0，直接返回
        }
        shaoguangnulmber = (int) Math.ceil(shaoguangPower.amount / 3.0);
        addToBot(new ReducePowerAction(player,player,shaoguang.POWER_ID,shaoguangnulmber));

    }


    public AbstractRelic makeCopy() {
        return new XLbao();
    }


}
