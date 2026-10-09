/*
41
Jinhsi：JBAir
惊龙破空
攻击
稀有
"时韵4：消耗全部韶光，
造成20+韶光点伤害
每点韶光获得1%的增伤"
升级：增伤为2%
2
Jinglong breaks through the air

 */

package jinhsimod.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.*;
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
import jinhsimod.powers.shaoguang;

import static jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;
import static jinhsimod.tag.DiyTag.shaoguangxiaohao;
import static jinhsimod.tag.DiyTag.shiyunxiaohao;

public class JBAir extends AbstractSignatureCard {

    //ID: 一张卡牌的同种类标识，用来区分不同卡名的卡牌。
    public static final String ID = "Jinhsimod:JBAir";

    //NAME: 显示在卡牌上方的卡牌名字。
    private static final CardStrings CARD_STRINGS =
            CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;

    /*IMG_PATH是该卡牌卡图的路径。attack skill power*/
    private static final String IMG_PATH = "Jinhsimodresources/img/cards/attack/JBAir.png";

    //COST:卡牌的费用。特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。
    private static final int COST = 2;

    //DESCRIPTION:卡牌的描述
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;


    //TYPE:卡牌类型。
    // （攻击牌:ATTACK、技能牌:SKILL、能力牌:POWER、诅咒牌:CURSE、状态牌:STATUS）
    private static final CardType TYPE = CardType.ATTACK;

    //COLOR:卡牌颜色
    private static final CardColor COLOR = Jinhsicardcolor;

    //RARITY: 卡牌稀有度。
    //基础：BASIC 普通：COMMON 罕见：UNCOMMON 稀有：RARE
    private static final CardRarity RARITY = CardRarity.RARE;

    //TARGET:卡牌指向类型的目标。实际功能只有是否指向敌人的区分。
    // CardTarget.ENEMY（指向敌人）和 CardTarget.NONE（无指向目标）
    private static final CardTarget TARGET = CardTarget.ENEMY;


    //初始化
    public JBAir() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 20;
        this.magicNumber =this.baseMagicNumber = 3;
        tags.add(shaoguangxiaohao);
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

    //升级效果：伤害类
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);

        }

    }
    //
    public int calculateDamage(AbstractPlayer p) {
        if (p == null) {
            return this.damage;
        }
        // 获取常规加成后的基础伤害（已包含力量等所有常规加成）
        int baseDamage = this.damage;
        // 获取韶光点数
        shaoguang shaoguangPower = (shaoguang) p.getPower(shaoguang.POWER_ID);
        int shaoguangAmount = (shaoguangPower != null) ? shaoguangPower.amount : 0;

        // 计算韶光独立增伤倍数
        float totalMultiplier = 1.0f + (shaoguangAmount * this.magicNumber * 0.01f);

        // 最终伤害 = (常规加成后的基础伤害 + 韶光点数) × 韶光增伤倍数
        return (int) ((baseDamage + shaoguangAmount) * totalMultiplier);
    }

    //卡牌核心
    //时韵不足则无法打出
    @Override
    public boolean canUse(AbstractPlayer p,AbstractMonster m){
        Shiyun shiyunPower = (Shiyun) p.getPower(Shiyun.POWER_ID);
        if (shiyunPower == null || shiyunPower.amount < 4){
            return false;
        }
        return true;
    }

    @Override
    //
    public void use(AbstractPlayer p,AbstractMonster m){
        AbstractDungeon.actionManager.addToBottom(
                new Haveshiyun(p,4,() ->{
                        int finalDamage = calculateDamage(p);
                        AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(
                                    m,new DamageInfo(
                                            p,finalDamage, DamageInfo.DamageType.NORMAL)
                            )
                        );
                        //清空韶光
                        addToBot(
                            new ReducePowerAction(p,p,shaoguang.POWER_ID,50)
                        );
                        addToBot(
                            new ApplyPowerAction(p,p,new shaoguang(p,0),0)
                        );
                })
        );
    }
}
