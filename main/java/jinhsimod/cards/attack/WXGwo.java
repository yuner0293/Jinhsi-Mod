
/*
Jinhsimod:WXGwo	万象共我	攻击	罕见	"造成5点伤害，
获得10点格挡，
随机获得1点乘岁凌霄/力量/敏捷/岁蕴/福泽
消耗"	"造成8点伤害，
获得14点格挡，
随机获得1点乘岁凌霄/力量/敏捷/岁蕴/福泽"	1
 */


package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.Fuze;
import jinhsimod.powers.RYSSky;
import jinhsimod.powers.Suiyun;

import java.util.Random;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class WXGwo extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:WXGwo";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/WXGwo.png";

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
    private static final CardRarity RARITY = CardRarity.UNCOMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ENEMY;




    //初始化
    public WXGwo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 5;
        this.block = this.baseBlock = 10;
        this.exhaust = true;
    }

    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeBlock(4);

            this.exhaust = false;

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
        AbstractDungeon.actionManager.addToBottom(
                //格挡的目标  格挡的来源  格挡的数值
                new GainBlockAction(p, p, this.block));

        Random random = new Random();
        int randomNum = random.nextInt(5);

        if (randomNum == 0){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new RYSSky(p, 1), 1)
            );
        }
        if (randomNum == 1 ){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1)
            );
        }
        if (randomNum == 2){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1)
            );
        }
        if (randomNum == 3){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new Suiyun(p, 1), 1)
            );
        }
        if (randomNum == 4){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new Fuze(p, 1), 1)
            );
        }

    }
}
