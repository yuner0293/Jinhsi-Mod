/*
addToBot(new XTattack(p,m,this.magicNumber,DamageInfo.DamageType.NORMAL));
 */


package jinhsimod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jinhsimod.helpers.ShiyunListener;
import jinhsimod.helpers.XTattackListener;
import jinhsimod.powers.shaoguang;

import java.util.ArrayList;
import java.util.List;

public class XTattack extends AbstractGameAction {
    private final AbstractPlayer player; // 玩家实例
    private final AbstractMonster target; // 目标敌人
    private final int amount; // 伤害量和力量数值
    private final DamageInfo.DamageType damageType; // 伤害类型

    private static final List<XTattackListener> globalListeners = new ArrayList<>();
    //静态监听
    public static void addListener(XTattackListener Listener){
        if (Listener != null && !globalListeners.contains(Listener)) {
            globalListeners.add(Listener);
        }
    }

    public static void removeListener(XTattackListener Listener) {
        globalListeners.remove(Listener);
    }

    public static void clearListeners() {
        globalListeners.clear();
    }


    public XTattack(AbstractPlayer player, AbstractMonster target, int amount, DamageInfo.DamageType damageType){
        this.player = player;
        this.target = target;
        this.amount = amount;
        this.damageType = damageType;
        this.actionType = ActionType.DAMAGE; // 主要行动类型为伤害
        this.source = player; // 行动来源为玩家
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()){
            addToBot(new DamageAction(
                    target,new DamageInfo(
                    player,amount,damageType), AttackEffect.NONE

            ));
        }else {
            addToBot(new DamageRandomEnemyAction(
                    new DamageInfo(player, amount, damageType),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL // 不同动画区分
            ));
        }
        addToBot(new ApplyPowerAction(player,player,new shaoguang(player,amount),amount));

        XT();
        this.isDone = true;
    }

    private void XT(){
        for (XTattackListener Listener : new ArrayList<>(globalListeners)){
            Listener.XTattackchufa(this.amount,target);
        }
    }
}
