
//其他+时韵n

package jinhsimod.action;

import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import jinhsimod.character.Jinhsicharacter;

import java.util.ArrayList;
public class GetPowercard extends AbstractGameAction {
    private final AbstractPlayer player; // 玩家实例
    private final int Amount; // 自定义的触发值（如4）


    // 构造方法：需要玩家、目标数、之后进行处理
    public GetPowercard(AbstractPlayer p, int requiredAmount) {
        this.player = p;
        this.Amount = requiredAmount;
        this.actionType = ActionType.SPECIAL; // 特殊动作类型
    }

    @Override
    public void update() {
        //卡牌池实例
        ArrayList<AbstractCard> playerCardPool = new ArrayList<>();
        //遍历卡牌
        for (AbstractCard card : CardLibrary.cards.values()) {
            if (card.color == Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor
                    && card.type == AbstractCard.CardType.POWER ) {
                playerCardPool.add(card);
            }
        }
        //防空
        if (playerCardPool.isEmpty()) {
            this.isDone = true;
            return;
        }
        //
        for (int i = 0 ; i < Amount;i++){
            // 随机选择一张卡牌并创建其副本
            int randomcard = AbstractDungeon.cardRandomRng.random(playerCardPool.size()-1);
            AbstractCard randomCard1 = playerCardPool.get(randomcard).makeStatEquivalentCopy();
            //费用为0
            randomCard1.setCostForTurn(0);
            //添加到手牌
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(randomCard1, 1, false)
            );
        }
        // 标记动作完成
        this.isDone = true;
    }

}
