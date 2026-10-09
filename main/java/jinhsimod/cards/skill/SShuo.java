
/*
65	Jinhsi：SShuo	时溯	技能	稀有	"时韵4：下回合获得4点时韵，
额外获得1回合
消耗"	"时韵3：下回合获得4点时韵，
额外获得1回合
消耗"	x

 */


package jinhsimod.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import jinhsimod.action.Haveshiyun;
import jinhsimod.cards.AbstractSignatureCard;
import jinhsimod.powers.Shiyun;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
public class SShuo extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:SShuo";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/skill/SShuo.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = -1;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS 特殊牌：SPECIAL）
    private static final CardType TYPE = CardType.SKILL;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE 诅咒牌:CURSE 特殊牌：SPECIAL
    private static final CardRarity RARITY = CardRarity.RARE;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.NONE;




    //初始化
    public SShuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
        this.exhaust = true;
    }



    //光晕
        public void triggerOnGlowCheck(){
            Shiyun shiyunpower = (Shiyun) AbstractDungeon.player.getPower(Shiyun.POWER_ID);
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
            if (shiyunpower != null && shiyunpower.amount >= this.magicNumber){
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }


    //升级效果
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int amount = EnergyPanel.totalCount;

        if(!this.freeToPlayOnce){
            p.energy.use(EnergyPanel.totalCount);
        }


        AbstractDungeon.actionManager.addToBottom(
                new Haveshiyun(p,this.magicNumber,()->{
                    this.addToBot((AbstractGameAction) new SkipEnemiesTurnAction());
                    this.addToBot((AbstractGameAction)new PressEndTurnButtonAction());
                    addToBot((AbstractGameAction) new ApplyPowerAction(p,p,new Shiyun(p,amount),
                            amount));
                })
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p,AbstractMonster m){
        Shiyun shiyunPower = (Shiyun) p.getPower(Shiyun.POWER_ID);
        if (shiyunPower == null || shiyunPower.amount < this.magicNumber){
            return false;
        }
        return true;
    }


}
