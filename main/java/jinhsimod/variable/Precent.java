package jinhsimod.variable;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class Precent extends DynamicVariable {
    @Override
    public String key() {
        return "Precent";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return card.upgraded;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {}

    @Override
    public int value(AbstractCard card) {
        return card.upgraded ? 2 : 1; // 升级后2%，升级前1%
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card.upgraded ? 2 : 1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return card.upgraded;
    }
}