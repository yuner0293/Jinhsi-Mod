//---------
package jinhsimod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDeathSubscriber;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.helpers.ModHelper;
import jinhsimod.powers.HJYFchun;
import jinhsimod.powers.Shiyun;
import jinhsimod.powers.shaoguang;



import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;


public class HJJCsheng extends CustomRelic implements
        OnPowersModifiedSubscriber,CustomSavable<Integer> ,PostBattleSubscriber, PostDeathSubscriber
{
    public static final String ID = ModHelper.makePath("HJJCsheng");
    private static final String IMG_PATH = "Jinhsimodresources/img/relics/HJJCsheng.png";
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final RelicStrings STRINGS = CardCrawlGame.languagePack.getRelicStrings(ID);

    //初始化层数与最大层数
    public int unlockedLayers = 0;
    private static final int MAX_LAYERS = 6;
    private boolean[] unlockLayerEffect = new boolean[MAX_LAYERS+1];

    //1
    public int one_1 = 0;
    public int one_2 = 0;
    public int one_old = 0;
    //3
    public int three_1 = 0;
    public int three_2 = 0;
    public int three_old = 0;
    //5
    public int five_1 = 0;
    public int five_2 = 0;
    public int five_old = 0;

    //----------初始化
    public HJJCsheng() {

        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        for (int i = 1;i <= MAX_LAYERS;i++){
            unlockLayerEffect[i] = false;
        }
        this.description = getUpdatedDescription();
        this.setCounter(unlockedLayers);

    }

    //局内保存
    @Override
    public Integer onSave() {
        return unlockedLayers;
    }

    // 读取
    @Override
    public void onLoad(Integer save) {

        // 处理存档数据异常情况
        if (save == null) {
            unlockedLayers = 0;
        } else {
            // 确保层数不超过最大值
            unlockedLayers = Math.min(save, MAX_LAYERS);
        }

        // 根据加载的层数恢复效果开关状态
        for (int i = 1; i <= MAX_LAYERS; i++) {
            unlockLayerEffect[i] = (i <= unlockedLayers);
        }

        //更新UI
        setCounter(unlockedLayers);
        this.description = getUpdatedDescription(); // 更新遗物描述
    }

    //---------描述
    public String getUpdatedDescription() {
        return HJJCsheng.STRINGS.DESCRIPTIONS[0] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[1] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[2] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[3] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[4] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[5] +
                " NL " + HJJCsheng.STRINGS.DESCRIPTIONS[6] ;
    }
    @Override
    public AbstractRelic makeCopy(){
        return new HJJCsheng();
    }

    //解锁
    public void unlockLayer() {
        if (unlockedLayers < MAX_LAYERS) {
            unlockedLayers++;
            unlockLayerEffect[unlockedLayers] = true; // 标记为已激活
            flash(); // 视觉反馈
            setCounter(unlockedLayers);
            description = getUpdatedDescription();
        }
    }

    //1,3,5
    @Override
    public void receivePowersModified() {
        if (AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT ||
                player == null) {
            return;
        }
        AbstractPower sg = player.getPower(shaoguang.POWER_ID);

        if (sg == null){
            one_old = three_old = five_old = 0;
            return;
        }
        // 只有第1层解锁后才触发效果
        if (unlockLayerEffect[1]) {
            checkShaoguangGain_1();
        }
        if (unlockLayerEffect[3] ){
//            checkShaoguangGain_3();
        }
        if (unlockLayerEffect[5] ){
            checkShaoguangGain_5();
        }
    }
    //1
    private void checkShaoguangGain_1() {
        AbstractPower shaoguangpower = player.getPower(shaoguang.POWER_ID);
        if (shaoguangpower == null) {
            return;
        }
        int currentamount = shaoguangpower.amount;
        int zengamount_1 = currentamount - one_old;

        if (zengamount_1 > 0) {
            one_1 += zengamount_1 + one_2;
            one_2 = 0;

            int one = one_1 / 10;
            if (one > 0) {
                AbstractDungeon.actionManager.addToBottom(
                        new DrawCardAction(
                                player, one));
                this.flash();
                one_2 = one_1 % 10;
                one_1 = 0;
            }
        }else if(zengamount_1 < 0) {
            one_old = currentamount;
            return;
        }
        one_old = currentamount;

    }
    //5层，每获得35韶光，获得1点力量与敏捷
    private void checkShaoguangGain_5() {
        AbstractPower shaoguangpower = player.getPower(shaoguang.POWER_ID);
        if (shaoguangpower == null) {
            return;
        }
        int currentamount = shaoguangpower.amount;
        int zengamount_5 = currentamount - five_old;

        if (zengamount_5 > 0) {
            five_1 += zengamount_5 + five_2;
            five_2 = 0;

            int five = five_1 / 35;
            if (five > 0) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(player,
                                player, new StrengthPower(player,five),five)
                );
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(player,
                                player, new DexterityPower(player,five),five)
                );
                this.flash();
                five_2 = five_1 % 35;
                five_1 = 0;
            }
        }
        if (zengamount_5 < 0) {
            five_old = currentamount; // 直接同步值，不累计
            return;
        }
        five_old = currentamount;
    }

    //2
    @Override
    public void  atTurnStart(){

        // 校验：仅在战斗中触发（非战斗回合无需执行）
        if (AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        if (unlockLayerEffect[2]){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(player,player,new shaoguang(player,5),5)
            );

        }
    }

    // 4
    @Override
    public void onPlayerEndTurn() {
        // 确保在战斗中且玩家存在
        if (AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT ||
                player == null) {
            return;
        }
        // 第4层：每回合结束获得等于韶光点数的格挡
        if (unlockLayerEffect[4]) {
            AbstractPower shaoguangPower = player.getPower(shaoguang.POWER_ID);
            if (shaoguangPower != null && shaoguangPower.amount > 0) {

                // 添加格挡动作，数值等于当前韶光量一半
                addToBot(new GainBlockAction(player, player, shaoguangPower.amount/2));
                flash(); // 视觉反馈
            }
        }
    }

    //6
    private static boolean actionHJJCsheng = false;

    //战斗开始时
    @Override
    public void atBattleStart(){
        if(!actionHJJCsheng){
            BaseMod.subscribe(this);
            actionHJJCsheng = true;
        }
        resetCounters();

        //6层，战斗开始获得1增伤
        if (unlockLayerEffect[6]) {
            addToBot(new ApplyPowerAction(player, player,
                    new HJYFchun(player, 1), 1));
        }

        //3层，战斗开始获得4时韵
        if (unlockLayerEffect[3] ){
            addToBot(new ApplyPowerAction(player, player,
                    new Shiyun(player, 4), 4));
        }
    }

    private void resetCounters() {
        one_1 = one_2 = one_old = 0;
        three_1 = three_2 = three_old = 0;
        five_1 = five_2 = five_old = 0;
    }

    //战斗后重置
    @Override
    public void receivePostBattle(AbstractRoom battleRoom){
        resetCounters();
    }

    //玩家死亡
    @Override
    public void receivePostDeath() {
        Gdx.app.postRunnable(() -> {
            BaseMod.unsubscribe(this);
            actionHJJCsheng = false;
        });
    }
}
