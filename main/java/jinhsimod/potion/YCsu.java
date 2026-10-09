package jinhsimod.potion;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import jinhsimod.character.Jinhsicharacter;
import jinhsimod.helpers.ModHelper;
import jinhsimod.modcore.Jinhsimod;
import jinhsimod.powers.shaoguang;

/**
 * 迎春酥 - 稀有药水
 * 效果：恢复5点生命，获得10点韶光（ShaoGuang）
 */
public class YCsu extends CustomPotion  {
// 直接定义完整、唯一的药水ID（小写模组名 + 冒号 + 药水名）
    public static final String POTION_ID = "Jinhsimod:YCsu";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = YCsu.potionStrings.NAME;
    public static final String[] DESCRIPTIONS = YCsu.potionStrings.DESCRIPTIONS;

    // 构造函数
    public YCsu() {
        super(
                YCsu.NAME,
                YCsu.POTION_ID,
                AbstractPotion.PotionRarity.RARE,      // 稀有药水
                AbstractPotion.PotionSize.M,           // 中等尺寸（也可用 FAIRY）
                PotionColor.SMOKE       //
        );
        Texture container = new Texture("Jinhsimodresources/img/potions/YCsu.png");//液体
        Texture liquid = new Texture("Jinhsimodresources/img/potions/YCsucontainer.png");//轮廓
        Texture outline = new Texture("Jinhsimodresources/img/potions/YCsuOutline.png");//选中

        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", container);
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "liquidImg", liquid);
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "outlineImg", outline);

        this.isThrown = false;
        // 描述：恢复5 HP，获得10 韶光
        this.description = DESCRIPTIONS[0];
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // 【核心】使用药水时的效果
    @Override
    public void use(AbstractCreature target) {
        // 恢复 5 点生命
        addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 5));
        // 获得 10 层“韶光”能力
        addToBot(new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new shaoguang(AbstractDungeon.player, 10), 10));
    }

    //
    @Override
    public int getPotency(int ascensionLevel) {
        return 10; // 或 return 5; 取决于你希望“强度”指代哪个效果
    }

    // 创建副本
    @Override
    public AbstractPotion makeCopy() {
        return new YCsu();
    }
}