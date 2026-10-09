
/*
沐光	技能	罕见	"获得4点时韵，
抽4张牌，
选择1张牌获得保留
消耗"	费用-1

 */


package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.Shiyun;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class MuGuang extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:MuGuang";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/MuGuang.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

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
    public MuGuang() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = 10;
        this.exhaust = true;
    }

    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeBaseCost(COST - 1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //扩展描述
        String selectionText = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        AbstractDungeon.actionManager.addToBottom(
                //格挡的目标  格挡的来源  格挡的数值
                new GainBlockAction(p, p, this.block)
        );
        //获得时韵
        AbstractDungeon.actionManager.addToBottom
                (new ApplyPowerAction(p,p,new Shiyun(p,4),3));
        //抽4
        AbstractDungeon.actionManager.addToBottom(
                (new DrawCardAction(4))
        );
        AbstractDungeon.actionManager.addToBottom(
                new SelectCardsInHandAction(
                        1,
                        selectionText,
                        true,
                        true,
                        c->true,
                        (selecter)->{
                            if (selecter != null&& !selecter.isEmpty()){
                                AbstractCard card = selecter.get(0);
                                CardModifierManager.addModifier(card, new RetainMod());
                            }
                        }
                )
        );
    }
}
