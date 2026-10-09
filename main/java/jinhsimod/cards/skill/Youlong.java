
/*
Jinhsimod:Youlong	游龙迥光	技能	罕见	选择消耗1张牌，并在手牌中加入惊龙破空（消耗）	选择消耗2张牌，并在手牌中加入惊龙破空（消耗）

 */


package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.cards.attack.JBAir;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class Youlong extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:Youlong";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/Youlong.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 0;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS 特殊牌：SPECIAL）
    private static final CardType TYPE = CardType.SKILL;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE 诅咒牌:CURSE 特殊牌：SPECIAL
    private static final CardRarity RARITY = CardRarity.UNCOMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;




    //初始化
    public Youlong() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        JBAir cardl = new JBAir();
        CardModifierManager.addModifier(cardl, new ExhaustMod());
        this.cardsToPreview = cardl;
    }

    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeMagicNumber(1);
            JBAir cardl = new JBAir();
            cardl.upgrade();
            CardModifierManager.addModifier(cardl, new ExhaustMod());
            this.cardsToPreview = cardl;
        }
    }

    @Override

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom
                (new ExhaustAction(this.magicNumber,false,true,true));
        JBAir cardToAdd = new JBAir();
        if (this.upgraded) { // 如果本卡已升级
            cardToAdd.upgrade(); // 添加升级版的神霓飞芒
        }
        CardModifierManager.addModifier(cardToAdd, new ExhaustMod());
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(cardToAdd, 1)
        );
    }
}
