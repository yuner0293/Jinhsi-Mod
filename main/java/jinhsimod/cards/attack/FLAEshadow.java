/*
序号：4
ID：Jinhsi:FLAEshadow
流光夕影	攻击	普通

"造成5点伤害两次
时韵4：费用降为0，
在手牌中加入 神霓飞芒"

伤害+3
费用2
Flowing light and evening shadow
*/
package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.cards.skill.SFeimang;
import jinhsimod.powers.Shiyun;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shiyunxiaohao;

public class FLAEshadow extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:FLAEshadow";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/FLAEshadow.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS）
    private static final CardType TYPE = CardType.ATTACK;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE
    private static final CardRarity RARITY = CardRarity.COMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ENEMY;


    //初始化
    public FLAEshadow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 5; // 基础格挡值设为5点
        this.baseMagicNumber = 4;          //!M!占符位
        SFeimang cardl = new SFeimang();
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
            // 将该卡牌的伤害提高3点。
            this.upgradeDamage(3);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();

            SFeimang cardl = new SFeimang();
            cardl.upgrade();
            CardModifierManager.addModifier(cardl, new ExhaustMod());
            this.cardsToPreview = cardl;
        }
    }


    //卡牌核心
    @Override
    public void use(AbstractPlayer p,AbstractMonster m){

        //基础效果
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                        m,new DamageInfo(
                                p,this.damage,DamageInfo.DamageType.NORMAL))
        );
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                        m,new DamageInfo(
                        p,this.damage,DamageInfo.DamageType.NORMAL))
        );
        //时韵4 添加 神霓飞芒（消耗）到手牌
        AbstractDungeon.actionManager.addToBottom(
                new Haveshiyun(
                        p,4,() ->{

                                    //添加手牌
                    SFeimang cardToAdd = new SFeimang();
                    if (this.upgraded) { // 如果本卡已升级
                        cardToAdd.upgrade(); // 添加升级版的神霓飞芒
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





