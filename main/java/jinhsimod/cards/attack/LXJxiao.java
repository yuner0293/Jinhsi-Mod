/*
Jinhsimod：LXJxiao	龙啸九霄	攻击	普通	"造成10点伤害
时韵3：造成20点伤害"	"造成10点伤害
时韵3：造成20点伤害
获得1点能量"

 */

package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class LXJxiao extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:LXJxiao";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/LXJxiao.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 3;

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
    public LXJxiao() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //基础数值
        this.damage = this.baseDamage = 10;
        tags.add(shiyunxiaohao);
    }
    //光晕
    public void triggerOnGlowCheck(){
        Shiyun shiyunpower = (Shiyun) AbstractDungeon.player.getPower(Shiyun.POWER_ID);
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (shiyunpower != null && shiyunpower.amount >= 3){
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom
                (new DamageAction
                        (m, new DamageInfo
                                (p, damage, DamageInfo.DamageType.NORMAL)));

        //判定
        AbstractDungeon.actionManager.addToBottom(

                new Haveshiyun(
                        p,3,() ->{
                    AbstractDungeon.actionManager.addToBottom
                            (new DamageAction
                                    (m, new DamageInfo
                                            (p, 20, DamageInfo.DamageType.NORMAL)));
                    if (this.upgraded){
                        AbstractDungeon.actionManager.addToBottom(
                                new GainEnergyAction(1)
                        );
                    }
                    }
                )
        );
    }
}
