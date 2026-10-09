
/*
Jinhsimod：SHGming	岁时共鸣	技能	普通	"获得3点韶光，消耗5点韶光
时韵2：获得1点能量 获得5点"
 */


package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.Haveshaoguang;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.Shiyun;
import jinhsimod.powers.shaoguang;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shiyunxiaohao;

public class SHGming extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:SHGming";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/SHGming.png";

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
    public SHGming() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        tags.add(shiyunxiaohao);
    }

    //光晕
        public void triggerOnGlowCheck(){
            Shiyun shiyunpower = (Shiyun) AbstractDungeon.player.getPower(Shiyun.POWER_ID);
            shaoguang shaoguangpower = (shaoguang) AbstractDungeon.player.getPower(shaoguang.POWER_ID);
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
            if (shiyunpower != null  && shiyunpower.amount >= 2){
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }


    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);

        }
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//            //抽牌
//            AbstractDungeon.actionManager.addToBottom
//                    (new DrawCardAction(this.magicNumber));

        addToBot(
                new ApplyPowerAction(p,p,new shaoguang(p,magicNumber),magicNumber)
        );
            //时韵2--获得能量
            addToBot(new Haveshiyun(p,2,()->{
                addToBot(new GainEnergyAction(1));
            }));

    }

}
