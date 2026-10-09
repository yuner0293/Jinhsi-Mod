
/*
14	Jinhsimod：CLHxing	蚩龙化形	攻击	普通	韶光大于25才能打出，造成30点伤害	韶光大于15才能打出，造成36点伤害	1

 */


package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.action.Haveshaoguang;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.character.Jinhsicharacter.PlayerColorEnum;
import jinhsimod.powers.Shiyun;
import jinhsimod.powers.shaoguang;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class CLHxing extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:CLHxing";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/CLHxing.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 1;

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
    public CLHxing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 30;
        this.magicNumber = this.baseMagicNumber = 25;
    }

    //光晕
        public void triggerOnGlowCheck(){
            shaoguang shaoguangpower = (shaoguang) AbstractDungeon.player.getPower(shaoguang.POWER_ID);
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
            if (shaoguangpower != null && shaoguangpower.amount >= this.magicNumber){
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }


    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeDamage(6);
            this.upgradeMagicNumber(5);
        }
    }

    //伤害类事件
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new Haveshaoguang(p,this.magicNumber,()->{
            AbstractDungeon.actionManager.addToBottom
                    (new DamageAction
                            (m, new DamageInfo
                                    (p, damage, DamageInfo.DamageType.NORMAL)));
        }));
    }

    @Override
    public boolean canUse(AbstractPlayer p,AbstractMonster m){
        shaoguang shaoguangpower = (shaoguang) p.getPower(shaoguang.POWER_ID);
        if (shaoguangpower == null || shaoguangpower.amount < this.magicNumber){
            return false;
        }
        return true;
    }
}
