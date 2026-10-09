/*
5	Jinhsimod：Qiyuncard	祈愿	技能	普通	"花费10金币
抽1张牌
有1/10概率进行祈愿"	"花费10金币
抽2张牌
有1/10概率进行祈愿"	0

 */

package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.Qiyuan;
import jinhsimod.cards.AbstractSignatureCard;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;

public class Qiyuncard extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:Qiyuncard";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/Qiyuan.png";

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
    private static final CardRarity RARITY = CardRarity.COMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;


    //初始化
    public Qiyuncard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;

    }


        @Override
        public void upgrade() {
            if (!this.upgraded) {
                this.upgradeName();
                this.upgradeMagicNumber(1);
            }
        }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 检查是否有10金币
        if (AbstractDungeon.player.gold < 10) {
            String selectionText = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            this.cantUseMessage = selectionText;
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //失去金币
        AbstractDungeon.player.loseGold(10);
        //抽牌
        addToBot(new DrawCardAction(this.magicNumber));
        //10分之1概率祈愿
        if (AbstractDungeon.miscRng.random(7) == 0) { // 0-9中随机，等于0就是1/10概率
            AbstractDungeon.actionManager.addToBottom(new Qiyuan());
        }
        }

}
