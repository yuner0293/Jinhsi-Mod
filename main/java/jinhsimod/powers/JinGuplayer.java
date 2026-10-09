package jinhsimod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class JinGuplayer extends AbstractPower {
    public JinGuplayer(AbstractCreature owner, int amount) {
        this.name = "禁锢";
        this.ID = "CannotPlayCards";
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadRegion("entangle"); // 复用缠绕图标
    }

    // 核心逻辑：拦截玩家出牌请求
    @Override
    public boolean canPlayCard(AbstractCard card) {
        card.cantUseMessage = "你被禁锢了，无法行动！";
        return false;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }
}