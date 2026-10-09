
/*
逐天取月
Jinhsimod:ZTQyue	逐天取月	攻击	普通	造成2点伤害5次	造成3点伤害5次

 */

package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.XTattack;
import jinhsimod.cards.AbstractSignatureCard;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;

public class ZTQyue extends AbstractSignatureCard {
    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:ZTQyue";
    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/ZTQyue.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

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
    public ZTQyue() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //基础数值
        this.damage = this.baseDamage = 2;
        this.magicNumber = this.baseMagicNumber = 2;
    }

    //升级效果：伤害类
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.upgradeMagicNumber(3);
        }
    }



    //伤害类事件
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new XTattack(p,m,this.magicNumber,DamageInfo.DamageType.NORMAL));
    }
}
