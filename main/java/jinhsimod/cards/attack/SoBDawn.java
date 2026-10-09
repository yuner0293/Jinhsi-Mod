/*
Jinhsi:SoBDawn	浮光霁寒	攻击	普通	造成3点伤害两次	Slash of Breaking Dawn
*/
package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;

public class SoBDawn extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:SoBDawn";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/SoBDawn.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 1;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS）
    private static final CardType TYPE = CardType.ATTACK;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE
    private static final CardRarity RARITY = CardRarity.BASIC;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ENEMY;




    //初始化
    public SoBDawn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //基础伤害
        this.damage = this.baseDamage = 3;
        //卡牌标签
        this.tags.add(AbstractCard.CardTags.STARTER_STRIKE);
        this.tags.add(AbstractCard.CardTags.STRIKE);

    }





    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            // 将该卡牌的伤害提高3点。
            this.upgradeDamage(3);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }




    @Override
    //p 你的玩家实体类。m 指向的怪物类。无指向时为null，包括攻击所有敌人时
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        /* DamageAction(AbstractCreature target, DamageInfo info)
                         * `target`是该事件造成伤害的目标，比如你指向的怪物。
                         * `info`是伤害信息。*/
                        (m, new DamageInfo
                                /*DamageInfo(AbstractCreature source, int base, DamageType type)
                                  描述伤害的信息。
                                  `source`是该伤害的来源（比如说玩家）。
                                  `base`是该伤害的数值（关于伤害计算详见另外章节）。
                                  `type`是伤害类型。攻击伤害使用`NORMAL`，非攻击伤害（荆棘等）使用`THORNS`，失去生命使用`HP_LOSS`。*/
                                (p, damage, DamageInfo.DamageType.NORMAL)));
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));

    }
}
