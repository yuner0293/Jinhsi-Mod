
/*
56	Jinhsimod:YSZxie	移岁诛邪	攻击	稀有	"时韵4：
对所有敌人造成10+韶光点伤害
当前每有5点韶光，伤害+1"	"对所有敌人造成5+韶光点伤害
当前每有5点韶光，伤害+2"	2

 */


package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.shaoguang;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shaoguangxiaohao;

public class YSZxie extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:YSZxie";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/YSZxie.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS 特殊牌：SPECIAL）
    private static final CardType TYPE = CardType.ATTACK;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE 诅咒牌:CURSE 特殊牌：SPECIAL
    private static final CardRarity RARITY = CardRarity.RARE;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;




    //初始化
    public YSZxie() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        //基础数值
        //基础数值
        this.damage = this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 1;
        tags.add(shaoguangxiaohao);
    }
    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeDamage(3);
            this.upgradeMagicNumber(1);
            this.upgradeBaseCost(COST - 1);
        }
    }


    //伤害类事件
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //增伤
        shaoguang shaoguangpower1 = (shaoguang) p.getPower(shaoguang.POWER_ID);
        int shaoguangpower = (shaoguangpower1  != null) ? shaoguangpower1.amount : 0;
        int zengshang = (shaoguangpower/2) * this.magicNumber;
        //群伤
        AbstractDungeon.actionManager.addToBottom
                (new DamageAllEnemiesAction(p,zengshang + this.damage,DamageInfo.DamageType.NORMAL,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

}
