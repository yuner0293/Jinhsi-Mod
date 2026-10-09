//删牌
package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class RemoveCardFromMainDeck extends AbstractGameAction
{
    AbstractCard card;
    AbstractPlayer p;

    public RemoveCardFromMainDeck(final AbstractCard card) {
        this.card = card;
        this.p = AbstractDungeon.player;
    }

    public void update() {
        AbstractCard i = null;
        for (final AbstractCard c : this.p.masterDeck.group) {
            if (c.uuid.equals(this.card.uuid)) {
                i = c;
                break;
            }
        }
        if (i == null) {
            this.isDone = true;
            return;
        }
        CardCrawlGame.metricData.addPurgedItem(i.getMetricID());
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(i, Settings.WIDTH / 2.0f * 3.0f, Settings.HEIGHT / 4.0f));
        AbstractDungeon.player.masterDeck.removeCard(i);
        this.isDone = true;
    }
}
