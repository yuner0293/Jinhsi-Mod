
//其他+时韵n

package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import jinhsimod.helpers.ShiyunListener;
import jinhsimod.powers.Shiyun;

import java.util.ArrayList;
import java.util.List;

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

public class Haveshiyun extends AbstractGameAction {
    private final AbstractPlayer player; // 玩家实例
    private final int requiredAmount; // 自定义的触发阈值（如4）
    private final Runnable onTrigger; // 达阈值时要执行的卡牌效果

    //时韵触发对象
    private static final List<ShiyunListener> globalListeners = new ArrayList<>();
    //静态监听
    public static void addListener(ShiyunListener Listener){
        if (Listener != null && !globalListeners.contains(Listener)) {
            globalListeners.add(Listener);
        }
    }

    public static void removeListener(ShiyunListener Listener) {
        globalListeners.remove(Listener);
    }

    public static void clearListeners() {
        globalListeners.clear();
    }

    // 构造方法：需要玩家、触发阈值、达阈值时的效果
    public Haveshiyun(AbstractPlayer p, int requiredAmount, Runnable onTrigger) {
        this.player = p;
        this.requiredAmount = requiredAmount;
        this.onTrigger = onTrigger;
        this.actionType = ActionType.SPECIAL; // 特殊动作类型
    }

    @Override
    public void update() {
        // 1. 获取玩家身上的时韵状态
        Shiyun shiyun = (Shiyun) player.getPower(Shiyun.POWER_ID);

        // 2. 检查时韵是否达到或超过阈值
        if (shiyun != null && shiyun.amount >= requiredAmount) {
            //触发卡牌的额外效果
            onTrigger.run();
            //  清空时韵
            shiyun.setAmount(0);
            //执行时韵触发
            Tshiyun();
        }
        // 标记动作完成
        this.isDone = true;
    }
    private void Tshiyun(){
        for (ShiyunListener Listener : new ArrayList<>(globalListeners)){
            Listener.Shiyunchufa(requiredAmount);
        }
    }

}
