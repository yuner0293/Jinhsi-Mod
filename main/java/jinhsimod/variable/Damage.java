package jinhsimod.variable;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jinhsimod.cards.attack.JBAir;

public class Damage extends DynamicVariable {
    @Override
    public String key() {
        return "J_D";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return true; // 总是显示为修改状态
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {}

    @Override
    public int value(AbstractCard card) {
        if (card instanceof JBAir && AbstractDungeon.player != null) {
            return ((JBAir) card).calculateDamage(AbstractDungeon.player);
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return value(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}