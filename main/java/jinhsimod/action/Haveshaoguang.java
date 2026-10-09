
//其他+时韵n

package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import jinhsimod.powers.shaoguang;

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

public class Haveshaoguang extends AbstractGameAction {
    private final AbstractPlayer player; // 玩家实例
    private final int requiredAmount; // 自定义的触发阈值（如4）
    private final Runnable onTrigger; // 达阈值时要执行的卡牌效果

    // 构造方法：需要玩家、触发阈值、达阈值时的效果
    public Haveshaoguang(AbstractPlayer p, int requiredAmount, Runnable onTrigger) {
        this.player = p;
        this.requiredAmount = requiredAmount;
        this.onTrigger = onTrigger;
        this.actionType = ActionType.SPECIAL; // 特殊动作类型
    }

    @Override
    public void update() {
        // 1. 获取玩家身上的时韵状态
        shaoguang shaoguangpower = (shaoguang) player.getPower(shaoguang.POWER_ID);

        // 2. 检查时韵是否达到或超过阈值
        if (shaoguangpower != null && shaoguangpower.amount >= requiredAmount) {
            // 3. 触发卡牌的额外效果（如添加神霓飞芒）
            onTrigger.run();

        }
        // 标记动作完成
        this.isDone = true;
    }

}
