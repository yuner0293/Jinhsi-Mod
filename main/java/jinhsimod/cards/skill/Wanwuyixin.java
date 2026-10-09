/*
万物一心	技能	普通	"获得10点防御
时韵4：费用降为0，
在手牌中加入 沐光
消耗"	"格挡+3
沐光+"	2

*/
package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.Shiyun;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shiyunxiaohao;

public class Wanwuyixin extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:Wanwuyixin";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/Wanwuyixin.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS）
    private static final CardType TYPE = CardType.SKILL;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE
    private static final CardRarity RARITY = CardRarity.COMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;


    //初始化
    public Wanwuyixin() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.block = this.baseBlock = 10; // 基础格挡值设为5点
        this.baseMagicNumber = 4;          //!M!占符位
        MuGuang cardl = new MuGuang();
        CardModifierManager.addModifier(cardl, new ExhaustMod());
        this.cardsToPreview = cardl;
        tags.add(shiyunxiaohao);
    }

    //光晕
    public void triggerOnGlowCheck(){
        Shiyun shiyunpower = (Shiyun) AbstractDungeon.player.getPower(Shiyun.POWER_ID);
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (shiyunpower != null && shiyunpower.amount >= 4){
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeBlock(3);

            MuGuang cardl = new MuGuang();
            cardl.upgrade();
            this.cardsToPreview = cardl;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }


    //卡牌核心
    @Override
    public void use(AbstractPlayer p,AbstractMonster m){
        //格挡
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p,p,this.block)
        );

        //时韵4 添加 沐光 到手牌
        AbstractDungeon.actionManager.addToBottom(
                new Haveshiyun(
                        p,4,() ->{
                    //添加手牌
                    MuGuang cardToAdd = new MuGuang();
                    if (this.upgraded) {
                        cardToAdd.upgrade();
                    }
                    CardModifierManager.addModifier(cardToAdd, new ExhaustMod());
                    AbstractDungeon.actionManager.addToBottom(
                            new MakeTempCardInHandAction(cardToAdd, 1)
                    );
                }
                )
        );

    }
    // 时韵达标时费用降为0
    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null) {
            // 检查时韵是否≥4（与卡牌描述一致）
            Shiyun shiyun = (Shiyun) p.getPower(Shiyun.POWER_ID);
            if (shiyun != null && shiyun.amount >= 4) {
                this.costForTurn = 0; // 时韵达标时费用降为0
            } else {
                this.costForTurn = this.cost; // 否则使用原始费用
            }
        }
    }

}





