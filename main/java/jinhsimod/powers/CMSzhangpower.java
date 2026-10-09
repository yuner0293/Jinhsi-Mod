/*
协奏：草木生长	能力	稀有	"每次触发协同攻击
将额外攻击敌人1次，并获得5点格挡"
"每次触发协同攻击
将额外攻击敌人2次，并获得5点格挡"	1

 */

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.action.XTattack;
import jinhsimod.helpers.ModHelper;
import jinhsimod.helpers.XTattackListener;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class CMSzhangpower extends AbstractPower implements XTattackListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("CMSzhangpower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/CMSZhang84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/CMSZhang32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private static final int MAX_STACKS = 2;

    public CMSzhangpower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        this.amount = amount;

        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
        if (this.amount >= MAX_STACKS){
            this.amount = MAX_STACKS;
        }

    }

    //注册
    @Override
    public void onInitialApplication() {
        XTattack.addListener(this);
    }

    //触发
    @Override
    public void XTattackchufa(int amount1, AbstractMonster m){
        this.flash(); // 触发反馈
        for (int i = 0; i < this.amount; i++){
            //目标一致
            if (m != null && !m.isDeadOrEscaped()) {
                addToBot(new DamageAction(m, new DamageInfo(player, amount1, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
            } else {
                //随机目标
                addToBot(new DamageRandomEnemyAction(new DamageInfo(player, amount1, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
            // 获得韶光
            addToBot(new ApplyPowerAction(player, player, new shaoguang(player, amount1), amount1));
        }
        //获得格挡
        addToBot(new GainBlockAction(player, player, 5));
    }

    //叠层
    @Override
    public void stackPower(int stackAmount) {
        // 调用父类逻辑增加数值
        super.stackPower(stackAmount);

        // 强制限制在 MAX_STACKS (2) 以内
        if (this.amount > MAX_STACKS) {
            this.amount = MAX_STACKS;
        }

        // 刷新描述，否则显示的文字可能还是旧的
        this.updateDescription();
    }

    //移除
    @Override
    public void onRemove() {
        XTattack.removeListener(this);
    }
    //战斗结束移除
    @Override
    public void onVictory() {
        XTattack.removeListener(this);
    }
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}