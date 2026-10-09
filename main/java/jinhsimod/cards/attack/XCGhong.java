
/*
12	Jinhsimod：XCGhong	星槎贯虹	攻击	普通	"消耗
时韵3：对全体敌人造成上次反击点伤害，1次"	时韵3：对全体敌人造成反击点伤害，2次	2

 */


package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.character.Jinhsicharacter.PlayerColorEnum;
import jinhsimod.helpers.FanJiListener;
import jinhsimod.powers.FanJi;
import jinhsimod.powers.Shiyun;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class XCGhong extends AbstractSignatureCard implements FanJiListener,PostBattleSubscriber  {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:XCGhong";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/XCGhong.png";

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
    private static final CardRarity RARITY = CardRarity.COMMON;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;

    private static int lastdamage = 0;
    private boolean action = true;
    //初始化
    public XCGhong() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 0;
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
        if (action){
            FanJi.addListener(this);
            action =false;
        }
    }

    //光晕
        public void triggerOnGlowCheck(){
            Shiyun shiyunpower = (Shiyun) AbstractDungeon.player.getPower(Shiyun.POWER_ID);
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
            if (shiyunpower != null && shiyunpower.amount >= 3){
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }


    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }

    //伤害类事件
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //实例变量储存
        final int finaldamage = XCGhong.lastdamage;
        addToBot(new Haveshiyun(p,3,()->{
            for (int i = 0; i<this.magicNumber; i ++){
                AbstractDungeon.actionManager.addToBottom
                        (new DamageAllEnemiesAction(p,finaldamage,DamageInfo.DamageType.NORMAL,
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
        }));

    }

    //监听
    @Override
    public void FanJichufa(int amount){
        XCGhong.lastdamage = amount;
        this.damage=this.baseDamage = amount;
        this.initializeDescription();
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        FanJi.removeListener(this); // 移除监听器
        this.damage = this.baseDamage = 0; // 重置伤害
        initializeDescription(); // 更新描述
    }


}
