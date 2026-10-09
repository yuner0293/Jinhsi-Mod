
/*
驭时	技能	稀有	"获得4点时韵
本回合，每触发2次时韵，
随机禁锢1名敌人
消耗"	"获得4点时韵
本回合，每触发2次时韵，
随机禁锢1名敌人
消耗"
 */

package jinhsimod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import jinhsimod.action.Haveshiyun;
import jinhsimod.helpers.ModHelper;
import jinhsimod.helpers.ShiyunListener;

import java.util.List;
import java.util.stream.Collectors;

public class Yushipower extends AbstractPower implements ShiyunListener {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("Yushipower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //静态变量存储图像资源
    private static final Texture power128 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/yushi84.png");
    private static final Texture power48 = ImageMaster.loadImage(
            "Jinhsimodresources/img/powers/yushi32.png");
    private static final TextureAtlas.AtlasRegion REGION_128 =
            new TextureAtlas.AtlasRegion(power128,0,0,128,128);
    private static final TextureAtlas.AtlasRegion REGION_48 =
            new TextureAtlas.AtlasRegion(power48,0,0,40,40);

    private static boolean action = false;
    private int amonut = 0;

    public Yushipower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;
        // 使用静态图像资源
        this.region128 =  REGION_128;
        this.region48  =  REGION_48;
        this.updateDescription();
        if (!action){
            Haveshiyun.addListener(this);
            action =true;
        }
    }

    //每回合开始，清空
    @Override
    public void atStartOfTurn() {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, Yushipower.POWER_ID));
    }

    @Override
    public void Shiyunchufa(int triggerThreshold){
        amonut++;
        if (amonut == 2){
            amonut = 0;
            //随机选怪物
            List<AbstractMonster> aliveMonsters = AbstractDungeon.getMonsters().monsters.stream()
                    .filter(monster -> !monster.isDead && !monster.isDying && !(monster instanceof jinhsimod.monster.Jue))
                    .collect(Collectors.toList());

            if (!aliveMonsters.isEmpty()) {
                AbstractMonster randomMonster = aliveMonsters.get(
                        AbstractDungeon.aiRng.random(aliveMonsters.size() - 1)
                );

                this.addToBot(new ApplyPowerAction(
                        randomMonster,
                        this.owner,
                        new JinGu(randomMonster, 1),
                        1
                ));
            }

        }
    }

    //移除
    @Override
    public void onRemove() {
        Haveshiyun.removeListener(this);
        action = false;
        super.onRemove();
    }
    //战斗结束移除
    @Override
    public void onVictory() {
        Haveshiyun.removeListener(this);
        action = false;
        super.onVictory();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}