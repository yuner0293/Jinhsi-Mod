//反击

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import jinhsimod.helpers.FanJiListener;
import jinhsimod.helpers.ModHelper;
import jinhsimod.helpers.ShiyunListener;

import java.util.ArrayList;
import java.util.List;

public class FanJi extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("FanJi");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/fanji84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/fanji32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);


    private static final List<FanJiListener> Fanjiread = new ArrayList<>();
    //静态监听
    public static void addListener(FanJiListener Listener){
        if (Listener != null && !Fanjiread.contains(Listener)) {
            Fanjiread.add(Listener);
        }
    }

    public static void removeListener(FanJiListener Listener) {
        Fanjiread.remove(Listener);
    }

    public FanJi(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        this.amount = amount;

        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
    }

    private boolean tiaojian = false;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS
                && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner && !tiaojian)
        {
            tiaojian = true;
            this.addToBot(new DamageAction(
                    info.owner, new DamageInfo(
                            this.owner,this.amount, DamageInfo.DamageType.THORNS)));
            FanjiChufa();
            addToBot(
                    new ReducePowerAction(this.owner,this.owner,this.ID,this.amount)
            );
        }
        return damageAmount;
    }
    //监听
    private void FanjiChufa(){
        for (FanJiListener Listener : new ArrayList<>(Fanjiread)){
            Listener.FanJichufa(this.amount);
        }
    }
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);   // 先正常加层数
        tiaojian = false;            // 获得新层数时重置标记
    }
    //
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

}