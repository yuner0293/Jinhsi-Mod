package jinhsimod.monster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.action.Shishuoaction;
import jinhsimod.character.Jinhsicharacter;
import jinhsimod.event.ShiShuoEvent;
import jinhsimod.powers.JinGuplayer;
import jinhsimod.powers.SuiZhujue;



public class SuiZhuBoss extends AbstractMonster {
    public static final String ID = "Jinhsimod:SuiZhu";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    // --- 核心逻辑变量 ---
    public int phase = 1;          // 当前阶段 (1, 2, 3)
    private int rank = 1;           // 全局共享档位 (1, 2, 3...)
    private int currentBreakValue = 500; // 击破值
    private final int maxBreakValue = 500;

    // --- 状态标志 ---
    private boolean isBroken = false;       // 是否处于击破眩晕中
    private boolean isTransforming = false;  // 是否处于转阶段（无法选中）状态
    private int stunTimer = 0;              // 眩晕回合计数
    private boolean isCharging = false;     // 三阶段A是否正在蓄力

    public SuiZhuBoss(float x, float y) {
        super(monsterStrings.NAME, ID, 3000, 0.0F, 0.0F,
                450.0F, 500.0F, "Jinhsimodresources/img/monster/suizhu.png", x, y);

        if (jinhsimod.modcore.Jinhsimod.suizhuStoredHp > 0) {
            this.setHp(jinhsimod.modcore.Jinhsimod.suizhuStoredHp);
        } else {
            this.setHp(3000); // 初始血量
        }

        this.loadAnimation(
                "Jinhsimodresources/img/monster/JUE.atlas",
                "Jinhsimodresources/img/monster/JUE.json",
                1.0F);

        this.animX = 0.0F;
        this.animY = 0.0F;

        float spineH = this.skeleton.getData().getHeight();
        float spineW = this.skeleton.getData().getWidth();

        this.skeleton.getRootBone().setX((hb_x - spineW / 2.0f + 50.0f)*Settings.scale);
        this.skeleton.getRootBone().setY( (hb_y +spineH/2.0f )* Settings.scale);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Piao", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.0F);


        // 索引 0: 用于行为 A 或三阶段蓄力攻击
        this.damage.add(new DamageInfo(this, 30));
        // 索引 1: 用于行为 C (多段)
        this.damage.add(new DamageInfo(this, 15));
    }

//    第一阶段开场逻辑
    @Override
    public void usePreBattleAction() {
        // ：获得原版浮空 99 层
        this.addToBot(new ApplyPowerAction(this, this,
                new FlightPower(this, 99)));
        this.addToBot(new ApplyPowerAction(this,this,
                new SuiZhujue(this)));
        CardCrawlGame.music.playTempBgmInstantly("CXSHAN_BGM", true);
    }
    /**
     * 伤害监听与优先级控制
     */
    @Override
    public void damage(DamageInfo info) {
        // 1. 计算当前阶段的锁血阈值
        int threshold = (phase == 1) ? (int)(this.maxHealth * 0.6F) : (int)(this.maxHealth * 0.2F);
        // 2. 只有在非转阶段、非三阶段时才进行拦截
        if (this.phase < 3 && !this.isTransforming) {
            // 如果本次伤害会导致血量跌破阈值
            if (this.currentHealth - info.output < threshold) {
                // 强制截断伤害：只造成“到阈值点为止”的伤害
                info.output = Math.max(0, this.currentHealth - threshold);
                // 只要伤害被拦截，就说明触碰了阈值，立即触发转阶段逻辑
                if (this.phase == 1) startTransformation(2);
                else if (this.phase == 2) startTransformation(3);
            }
        }
        // 3. 执行真正的扣血逻辑 (此时 info.output 已经被我们修正过)
        super.damage(info);
        // 4. 击破判定逻辑：由于转阶段时 isTransforming 已变为 true，这里会自动跳过扣除击破值
        if (!this.isTransforming && !this.isBroken &&
                info.type == DamageInfo.DamageType.NORMAL && info.output > 0) {
            this.currentBreakValue -= info.output;
            if (this.currentBreakValue <= 0) {
                triggerVFX("BREAK");
                triggerBreak();
            }
        }
    }

    // --- 2. 行为逻辑执行 (takeTurn) ---
    @Override
    public void takeTurn() {
        // 优先级 1：转阶段状态处理
        if (this.isTransforming) {
            this.halfDead = false;
            this.isTransforming = false;
            handlePhaseEntryEffects(); // 立即触发“进入时”逻辑
            this.addToBot(new RollMoveAction(this));
            return;
        }
        // 优先级 2：击破状态处理
        if (this.isBroken) {
            // 检查眩晕计时
            if (this.stunTimer > 0) {
                this.stunTimer--;
                // 还没醒，继续跳过本回合
            }
            // 如果倒计时归零，说明本回合结束时该起床了
            if (this.stunTimer <= 0) {
                this.recoverFromBreak();
            }
            this.addToBot(new RollMoveAction(this));
            return;
        }
        //行为
        switch (this.nextMove) {
            case 1: // 行为 A
                this.addToBot(new DamageAction
                        (AbstractDungeon.player, this.damage.get(0),
                                AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;

            case 3:
                handleBehaviorC();
                break;

            case 4: // 蓄力攻击
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                this.isCharging = false;
                break;

            case 2: // 行为 B
                handleBehaviorB();
                break;
        }
        // 回合结束逻辑：档位提升 (一阶段上限3，二阶段上限5，三阶段上限为99)
        int rankLimit = (phase == 1) ? 3 : (phase == 2) ? 5 : 99;
        if (this.rank < rankLimit) {
            this.rank++;
        }
        this.addToBot(new RollMoveAction(this));
    }
    private void handleBehaviorB() {
        if (phase == 1) {
            int str = (rank == 1) ? 3 : (rank == 2 ? 5 : 6);
            this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, str)));
            this.addToBot(new GainBlockAction(this, 60));
        } else if (phase == 2) {
            int str = ((rank == 1) ? 3 : (rank == 2 ? 5 : 6)) + rank;
            this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, str)));
            this.addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
        } else if (phase == 3) {
            //随机清空能力||禁锢玩家||清空所有韶光（-50）
            this.addToBot(new ReducePowerAction(AbstractDungeon.player, this, "Jinhsimod:ShaoguangPower", 50));
            if (!AbstractDungeon.player.powers.isEmpty()) {
                AbstractPower rp = AbstractDungeon.player.powers.get(AbstractDungeon.monsterRng.random(AbstractDungeon.player.powers.size() - 1));
                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, this, rp.ID));
            }
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new JinGuplayer(AbstractDungeon.player, 1)));
        }
    }
    private void handleBehaviorC() {
        if (this.phase == 3) {
            // ================= 三阶段首次触发：时朔事件 =================
            // 1. 视觉演出：全屏白光闪烁
            this.addToBot(new com.megacrit.cardcrawl.actions.animations.VFXAction(
                    new com.megacrit.cardcrawl.vfx.BorderLongFlashEffect(Color.WHITE.cpy())
            ));
            // 2. 核心：弹出事件窗口
            this.addToBot(new Shishuoaction());
        } else {
            // ================= 各阶段固定的多段攻击逻辑 =================
            int hitsC = (rank == 1) ? 2 : (rank == 2 ? 3 : 5);
            // 精确阶段匹配，不再使用 >= 2
            if (this.phase == 1) {
                // 一阶段：基础段数 [2, 3, 5]
            } else if (this.phase == 2) {
                // 二阶段：固定增加 1 段 [3, 4, 6]
                hitsC += 1;
            }
            // 执行伤害
            for (int i = 0; i < hitsC; i++) {
                this.addToBot(new DamageAction(
                        AbstractDungeon.player,
                        this.damage.get(1),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                ));
            }
        }
    }
    /**
     *BOSS计算细节
     */
    // 击破
    private void triggerBreak() {
        this.isBroken = true;
        this.stunTimer = 2;
        this.isCharging = false;
        // 立即清空动作队列。如果 BOSS 正在执行多段攻击（例如 15 * 5 的第三下）
        AbstractDungeon.actionManager.clearPostCombatActions();
        // 移除“浮空”能力，使 BOSS 落地并失去减伤。
        this.addToTop(new RemoveSpecificPowerAction(this, this, "Flight"));
        // 施加 1 层易伤。由于是 addToTop，它会在本回合剩余逻辑处理前生效。
        this.addToTop(new ApplyPowerAction(this, this,
                new VulnerablePower(this, 1, false)));
        // 4. 视觉表现与意图重置
        // (byte)2 是你在 getMove 中预留的眩晕意图代号。
        // Intent.STUN 会在 BOSS 头顶显示标志性的蓝色“眩晕”图标。
        this.setMove((byte)2, Intent.STUN);
        this.createIntent();
    }
    //恢复击破
    private void recoverFromBreak() {
        this.isBroken = false;
        //恢复击破
        this.currentBreakValue = maxBreakValue;
        //恢复浮空
        this.addToBot(new ApplyPowerAction(this, this,
                new FlightPower(this, 99)));

        this.getMove(AbstractDungeon.monsterRng.random(99));
        this.createIntent();
    }
    //转阶段效果
    private void handlePhaseEntryEffects() {
        if (phase == 2) {
            this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 10)));
        } else if (phase == 3) {
            this.addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
        }
    }
    //触发转阶段状态（转阶段优先级最高）
    private void startTransformation(int nextPhase) {
        this.phase = nextPhase;
        this.isTransforming = true;
        this.isBroken = false; // 立即覆盖击破状态
        this.isCharging = false;
        //特效
        triggerVFX("TRANSFORM");
        // 立即打断当前所有动作序列
        AbstractDungeon.actionManager.clearPostCombatActions();
        // 设为半死状态
        this.halfDead = true;
        //意图
        this.setMove((byte)10, Intent.BUFF); // 意图设为强化
        this.createIntent();
    }

    /**
     * 集中管理所有特效触发逻辑
     */
    private void triggerVFX(String type) {
        switch (type) {
            case "BREAK": // 击破效果
                // 1. 模仿获得能力时的字样升起
                AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect(
                        this.hb.cX, this.hb.cY + this.hb.height / 2.0F, monsterStrings.MOVES[0]
                ));
                // 2. 破碎音效
                CardCrawlGame.sound.play("ORB_FROST_EVOKE");
                // 3. 屏幕微震
                AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect(this.hb.cX, this.hb.cY));
                break;

            case "TRANSFORM": // 转阶段
                //重创
                AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect(
                        this.hb.cX, this.hb.cY + this.hb.height / 2.0F, monsterStrings.MOVES[1]
                ));
                // 岁韵
                AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect(
                        this.hb.cX, this.hb.cY + this.hb.height / 2.0F, monsterStrings.MOVES[4]
                ));
                // 1. 核心：冲击波特效 (ShockWaveEffect)
                // 参数说明：位置X, 位置Y, 颜色, 冲击波类型 (ADDITIVE 是亮色叠加，类似能量爆发)
                AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect(
                        this.hb.cX,
                        this.hb.cY,
                        Color.CYAN.cpy(),
                        com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect.ShockWaveType.ADDITIVE
                ));
                // 3. 全屏闪光 (让转阶段更有震撼感)
                AbstractDungeon.effectsQueue.add
                        (new com.megacrit.cardcrawl.vfx.BorderFlashEffect(Color.CYAN, true));
                break;
        }
    }

    /**
     *意图设置
     */
    @Override
    protected void getMove(int num) {
        //状态转换
        if (this.isTransforming) {
            this.setMove((byte)10,Intent.BUFF);
            return;
        }
        if (this.isBroken) {
            if (this.stunTimer > 0) {
                this.stunTimer--;
            } else {
                recoverFromBreak();
            }
            return; // 击破状态必须 return
        }
        // 三阶段 A 的特殊蓄力逻辑
        if (phase == 3 && isCharging) {
            this.damage.get(0).base = 2000;
            this.setMove((byte)4, Intent.ATTACK, 2000);
            return;
        }
        // 基础概率：40% A, 40% B, 20% C
        String moveName;
        Intent moveIntent;

        // A: 攻击或蓄力
        if (num < 40) {
            setMoveA();

            // 40% 概率 B
        } else if ( num < 80) {
            setMoveB();

            // 20% 概率 C
        } else {
            setMoveC();
        }
    }


    private void setMoveA() {
        if (this.phase == 3) {
            this.isCharging = true;
            this.setMove((byte)5, Intent.UNKNOWN);
            AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect(
                    this.hb.cX, this.hb.cY + this.hb.height / 2.0F, monsterStrings.MOVES[2]
            ));
        } else {
            int baseA = (rank == 1) ? 30 : (rank == 2 ? 50 : 80);
            if (phase >= 2) baseA += (rank * 10);
            this.damage.get(0).base = baseA;
            this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        }
    }
    private void setMoveB() {
        // 行为 B
        Intent bIntent = (phase == 1 || phase == 2) ? Intent.DEFEND_BUFF : Intent.STRONG_DEBUFF;
        this.setMove((byte)2, bIntent);
        if (phase == 3){
            AbstractDungeon.effectsQueue.add(new com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect(
                    this.hb.cX, this.hb.cY + this.hb.height / 2.0F, monsterStrings.MOVES[3]
            ));
        }
    }
    private void setMoveC() {
        // 1. 伤害数值计算
        int baseC = (rank == 1) ? 15 : (rank == 2 ? 25 : 35);

        if (this.phase == 2) baseC += rank;

        else if (this.phase == 3) baseC += rank * 2;

        this.damage.get(1).base = baseC;

        // 2. 段数计算 (与 handleBehaviorC 保持严格一致)
        int hitsC = (rank == 1) ? 2 : (rank == 2 ? 3 : 5);
        if (this.phase == 2) hitsC += 1;
        else if (this.phase == 3) hitsC += 2;

        // 3. 意图设置
        if (this.phase == 3 ) {
            jinhsimod.modcore.Jinhsimod.suizhuStoredHp = this.currentHealth;
            this.setMove(monsterStrings.MOVES[5], (byte)3, Intent.MAGIC);
        }else {
            // 一、二阶段，以及三阶段后续：正常的连击攻击
            // 使用带布尔值的构造函数，显示多段伤害数字 (e.g., 15 x 2)
            this.setMove((byte)3, Intent.ATTACK, this.damage.get(1).base, hitsC, true);
        }
    }

    //死亡
    @Override
    public void die() {
        super.die();
        // 确保标志位已完成
        // 呼出最终结算屏
        AbstractDungeon.victoryScreen = new com.megacrit.cardcrawl.screens.VictoryScreen(
                AbstractDungeon.getMonsters());

        CardCrawlGame.music.fadeOutTempBGM();

    }

    // UI
    @Override
    public void render(final SpriteBatch sb) {
        // 1. 怪物本体与原版血条
        super.render(sb);

        if (!this.isDead && !Settings.hideCombatElements) {
            // 3. 渲染击破条 (仅在非眩晕、非转阶段时)
            if (!this.isBroken && !this.isTransforming) {
                this.renderBreakBar(sb);
            }
        }

    }
    //击破条UI
    private void renderBreakBar(SpriteBatch sb) {
        // === 数值调整区 ===
        float width = this.hb.width;           // 长度与血条对齐
        float height = 10.0F * Settings.scale; // 宽度更细一点
        float x = this.hb.cX - width / 2.0F;
        float y = this.hb.y;
        // ================

        // 计算百分比
        float percentage = (float)this.currentBreakValue / 500.0F;
        percentage = Math.max(0.0F, Math.min(1.0F, percentage));

        // 只有当百分比大于 0 时才渲染进度条
        if (percentage > 0) {
            // 渲染进度条本体（灰白色）
            sb.setColor(new Color(0.8F, 0.8F, 0.8F, 1.0F));
            drawRoundedBar(sb, ImageMaster.HB_SHADOW_L,
                    ImageMaster.HB_SHADOW_B,
                    ImageMaster.HB_SHADOW_R,
                    x, y, width * percentage, height);

            // 渲染文字
            String label = this.currentBreakValue + " / 500";
            // 字体稍微调小一点 (0.7F)，颜色用纯白增强对比
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, label, this.hb.cX, y + height / 2.0F, Color.WHITE, 0.7F);
        }
        // 还原画笔颜色
        sb.setColor(Color.WHITE);
    }
    // 击破条UI的辅助方法
    private void drawRoundedBar(SpriteBatch sb, Texture left, Texture mid, Texture right, float x, float y, float w, float h) {
        float rW = 6.0F * Settings.scale; // 稍微调小圆角半径，匹配更细的条
        sb.draw(left, x, y, rW, h);
        if (w > rW * 2) {
            sb.draw(mid, x + rW, y, w - rW * 2, h);
        }
        sb.draw(right, x + w - rW, y, rW, h);
    }

    @Override
    public void createIntent() {
        // 在父类画出图标和数字之前，先运行我们的刷新逻辑
        this.applyPowers();
        super.createIntent();
    }
}