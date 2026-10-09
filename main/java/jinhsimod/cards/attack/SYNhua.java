/*
Jinhsimod：SYNhua	碎玉凝华	攻击	普通	"造成1点伤害，韶光次
最多20次
清空所有韶光
消耗"	"清空所有韶光
造成1点伤害，韶光次
最多22次
消耗"	1

 */
package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.shaoguang;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shaoguangxiaohao;

public class SYNhua extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:SYNhua";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/SYNhua.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 1;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS 特殊牌：SPECIAL）
    private static final CardType TYPE = CardType.ATTACK;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE 诅咒牌:CURSE 特殊牌：SPECIAL
    private static final CardRarity RARITY = CardRarity.COMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ENEMY;


    //初始化
    public SYNhua() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 1;
        this.magicNumber = this.baseMagicNumber = 20;
        this.exhaust = true;
        tags.add(shaoguangxiaohao);
    }

    //升级效果：伤害类
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeMagicNumber(2);

        }
    }
    //伤害类事件
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //攻击
        int hitcount = 0;
        shaoguang shaoguangpower = (shaoguang) p.getPower(shaoguang.POWER_ID);
        if (shaoguangpower != null) {
            if (shaoguangpower.amount > this.magicNumber) {
                hitcount = this.magicNumber;
            } else {
                hitcount = shaoguangpower.amount;
            }
        }
        for (int i = 0;i < hitcount;i++){
            AbstractDungeon.actionManager.addToTop
                    (new DamageAction
                            (m, new DamageInfo
                                    (p, damage, DamageInfo.DamageType.NORMAL)));
        }
        //清空
        addToBot(
                new ReducePowerAction(p,p,shaoguang.POWER_ID,50)
        );
        addToBot(
                new ApplyPowerAction(p,p,new shaoguang(p,0),0)
        );
    }
}
