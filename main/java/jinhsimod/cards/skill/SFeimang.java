/*
16
Jinhsi:SFeimang
神霓飞芒
技能
罕见
"获得5点格挡
获得乘岁凌霄2，
在手牌中加入 惊龙破空*"
0
Shenni Feimang
 */


package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.cards.attack.JBAir;
import jinhsimod.powers.RYSSky;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;


public class SFeimang extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:SFeimang";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/SFeimang.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 0;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS）
    private static final CardType TYPE = CardType.SKILL;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE
    private static final CardRarity RARITY = CardRarity.UNCOMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;


    //初始化
    public SFeimang() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //基础数值
        this.block = this.baseBlock = 5; // 基础格挡值设为5点
        this.magicNumber = this.baseMagicNumber = 2;


        JBAir cardl = new JBAir();
        CardModifierManager.addModifier(cardl, new ExhaustMod());
        this.cardsToPreview = cardl;
    }

        //升级效果
        @Override
        public void upgrade() {
            if (!this.upgraded) {
                this.upgradeName();
                this.upgradeBlock(3); // 升级后格挡增加3点（总8点）
                this.upgradeMagicNumber(1);
                this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
                this.initializeDescription();

                JBAir cardl = new JBAir();
                cardl.upgrade();
                CardModifierManager.addModifier(cardl, new ExhaustMod());

                this.cardsToPreview = cardl;
            }
        }

    //卡牌核心
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                //格挡的目标  格挡的来源  格挡的数值
                new GainBlockAction(
                        p, p, this.block)
        );
        //两点能力
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,p,new RYSSky(p,this.magicNumber),this.magicNumber
                )
        );
        //添加手牌
        JBAir cardToAdd = new JBAir();
        if (this.upgraded) { cardToAdd.upgrade(); }
        CardModifierManager.addModifier(cardToAdd, new ExhaustMod());
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(
                        cardToAdd,1
                )
        );
    }
}
