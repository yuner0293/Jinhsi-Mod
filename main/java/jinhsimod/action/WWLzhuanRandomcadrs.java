
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

//-----------时韵判断逻辑-----------
/*
        AbstractDungeon.actionManager.addToBottom(
                new Haveshiyun(
                        p,4,() ->{
                            //写触发的效果
                            );
                       }
                )
        );
 */

public class WWLzhuanRandomcadrs extends AbstractGameAction {
    private final AbstractPlayer player; // 玩家实例
    private final int Amount; // 自定义的触发值（如4）


    // 构造方法：需要玩家、目标数、之后进行处理
    public WWLzhuanRandomcadrs(AbstractPlayer p, int requiredAmount) {
        this.player = p;
        this.Amount = requiredAmount;
        this.actionType = ActionType.SPECIAL; // 特殊动作类型
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> playerCardPool = new ArrayList<>();
        for (AbstractCard card : CardLibrary.cards.values()) {
            if (card.color == Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor) {
                playerCardPool.add(card);
            }
        }

        if (playerCardPool.isEmpty()) {
            this.isDone = true;
            return;
        }

        for (int i = 0 ; i < Amount;i++){
            // 随机选择一张卡牌并创建其副本
            int randomcard = AbstractDungeon.cardRandomRng.random(playerCardPool.size()-1);
            AbstractCard randomCard1 = playerCardPool.get(randomcard).makeStatEquivalentCopy();

            if (randomCard1.cost >= 1) { // baseCost是卡牌的原始能量消耗
                randomCard1.cost -= 1;
                randomCard1.costForTurn = randomCard1.cost;
                randomCard1.isCostModified = true; // 标记费用已修改（界面显示正确）
            }
            randomCard1.costForTurn = randomCard1.cost;
            CardModifierManager.addModifier(randomCard1, new ExhaustMod());
            CardModifierManager.addModifier(randomCard1, new EtherealMod());
            // 3. 使用 MakeTempCardInHandAction 将卡牌添加到手牌
            // 参数说明：卡牌对象, 数量（这里为1）, 是否在屏幕中央显示新卡牌（false为随机位置显示）
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(randomCard1, 1, false)
            );
        }


        // 标记动作完成
        this.isDone = true;
    }

}
