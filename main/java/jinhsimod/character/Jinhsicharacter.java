package jinhsimod.character;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import jinhsimod.cards.attack.FLAEshadow;
import jinhsimod.cards.attack.SoBDawn;
import jinhsimod.cards.skill.Dodge;
import jinhsimod.cards.skill.Qiyuncard;
import jinhsimod.cards.skill.Wanwuyixin;
import jinhsimod.modcore.Jinhsimod;
import jinhsimod.relics.Drscale;
import jinhsimod.relics.HJJCsheng;
import java.util.ArrayList;

import static jinhsimod.modcore.Jinhsimod.*;


public class Jinhsicharacter extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 =
            "Jinhsimodresources/img/char/shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 =
            "Jinhsimodresources/img/char/shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE =
            "Jinhsimodresources/img/char/corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            "Jinhsimodresources/img/UI/orb/5.png",
            "Jinhsimodresources/img/UI/orb/4.png",
            "Jinhsimodresources/img/UI/orb/3.png",
            "Jinhsimodresources/img/UI/orb/2.png",
            "Jinhsimodresources/img/UI/orb/1.png",
            "Jinhsimodresources/img/UI/orb/6.png",
            "Jinhsimodresources/img/UI/orb/5d.png",
            "Jinhsimodresources/img/UI/orb/4d.png",
            "Jinhsimodresources/img/UI/orb/3d.png",
            "Jinhsimodresources/img/UI/orb/2d.png",
            "Jinhsimodresources/img/UI/orb/1d.png"
    };

    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };


    // 人物的本地化文本引用
    private static final CharacterStrings characterStrings =
            CardCrawlGame.languagePack.getCharacterString
                    ("Jinhsimod:Jinhsicharacter");

    public Jinhsicharacter(String name) {
        //初始化
        super(name,Jinhsi, ORB_TEXTURES,
                "Jinhsimodresources/img/UI/orb/vfx.png", LAYER_SPEED,
                null, null);

        if (skins.isEmpty()) {
            // 如果为空，先紧急初始化一次，或者报错提醒
            jinhsimod.modcore.Jinhsimod.initializeSkins();
        }
        JinhsiSkin curSkin = skins.get(Jinhsimod.currentSkinIndex);

        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.drawX += 0.0F * Settings.scale;
        this.drawY += 0.0F * Settings.scale;
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                /*
//                "Jinhsimodresources/img/char/character1.png", // 人物图片
//                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
//                CORPSE_IMAGE, // 人物死亡图像
//                this.getLoadout(),
//                0.0F, 0.0F,
//                200.0F, 220.0F, // 人物碰撞箱大小，越大的人物模型这个越大
//                new EnergyManager(3) // 初始每回合的能量
                 */
                curSkin.img,  // 人物图片
                curSkin.shoulder2,
                curSkin.shoulder1,
                curSkin.corpse, // 人物死亡图像
                this.getLoadout(),
                0F, 0F,
                200.0F, 300.0F,// 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3)// 初始每回合的能量
        );

//// 3. 核心逻辑：判断是否加载动画
        if (curSkin.atlas != null && curSkin.json != null) {
            JinhsiSkin curSkin1 = skins.get(currentSkinIndex);

                // 加载 Spine 动画
                this.loadAnimation(curSkin.atlas, curSkin.json, 1.3F);

                float spineH = this.skeleton.getData().getHeight();

                // 安全设置骨骼位置
                if (this.skeleton != null) {
                    //5.0,向右微偏
                    this.skeleton.getRootBone().setX( (hb_x + 5.0f) * Settings.scale);
                    this.skeleton.getRootBone().setY( (spineH * 0.7f / 2f + hb_y) * Settings.scale);
                    AnimationState.TrackEntry e = this.state.setAnimation(0, "newAnimation", true);
                    e.setTime(e.getEndTime() * MathUtils.random());
                    e.setTimeScale(1.0F);
                }
        } else {
            // 如果是静态皮肤，清除可能残留的动画状态
            this.state = null;
            this.skeleton = null;
        }
    }

    // ------------------初始卡组-------------
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(SoBDawn.ID);//浮光霁寒*4
        retVal.add(SoBDawn.ID);
        retVal.add(SoBDawn.ID);
        retVal.add(SoBDawn.ID);

        retVal.add(FLAEshadow.ID);//流光夕影

        retVal.add(Dodge.ID);//闪避*4
        retVal.add(Dodge.ID);
        retVal.add(Dodge.ID);
        retVal.add(Dodge.ID);

        retVal.add(Qiyuncard.ID);//祈愿

        retVal.add(Wanwuyixin.ID);//万物一心

        return retVal;
    }

    // --------------------初始遗物------------
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Drscale.ID);
        retVal.add(HJJCsheng.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                80, // 当前血量
                80, // 最大血量
                0, // 初始充能球栏位
                100, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    //预览
    public void refreshSkin() {
        // 1. 获取当前皮肤数据
        JinhsiSkin curSkin = skins.get(jinhsimod.modcore.Jinhsimod.currentSkinIndex);

        // 2. 重新加载静态贴图 (即便后续加载动画，这也作为底图)
        this.img = com.megacrit.cardcrawl.helpers.ImageMaster.loadImage(curSkin.img);

        // 3. 仿照 Muelsyse 的逻辑：如果有动画资源，则完整重载 Spine 实例
        if (curSkin.atlas != null && curSkin.json != null) {
            try {
                this.atlas = new com.badlogic.gdx.graphics.g2d.TextureAtlas(com.badlogic.gdx.Gdx.files.internal(curSkin.atlas));
                com.esotericsoftware.spine.SkeletonJson json = new com.esotericsoftware.spine.SkeletonJson(this.atlas);

                // 这里的 scale 需和你构造函数里的 1.0F 保持一致
                json.setScale(com.megacrit.cardcrawl.core.Settings.renderScale / 1.0F);
                com.esotericsoftware.spine.SkeletonData skeletonData = json.readSkeletonData(com.badlogic.gdx.Gdx.files.internal(curSkin.json));
                this.skeleton = new com.esotericsoftware.spine.Skeleton(skeletonData);
                this.skeleton.setColor(com.badlogic.gdx.graphics.Color.WHITE);
                this.skeleton.getRootBone().setY(190.0F * com.megacrit.cardcrawl.core.Settings.scale);

                this.stateData = new com.esotericsoftware.spine.AnimationStateData(skeletonData);
                this.state = new com.esotericsoftware.spine.AnimationState(this.stateData);

                // 播放你的默认动画
                this.state.setAnimation(0, "newAnimation", true); // 默认（皮肤 0）用这个


            } catch (Exception e) {
                e.printStackTrace();
                // 如果加载失败，降级为静态显示
                this.skeleton = null;
                this.state = null;
            }
        } else {
            // 静态皮肤：清空动画实例，让渲染器只画 initializeClass 里的 img
            this.skeleton = null;
            this.state = null;
        }
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Jinhsicardcolor;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new SoBDawn();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return Jinhsicolor;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel("Jinhsimodresources/img/char/Victory11.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("Jinhsimodresources/img/char/Victory22.png"));
        panels.add(new CutscenePanel("Jinhsimodresources/img/char/Victory33.png"));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new Jinhsicharacter(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return Jinhsicolor;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return Jinhsicolor;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    public static class PlayerColorEnum {
        // 修改为你的颜色名称，确保不会与其他mod冲突
        @SpireEnum
        public static PlayerClass Jinhsi;

        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        @SpireEnum
        public static AbstractCard.CardColor Jinhsicardcolor;
    }

    public static class PlayerLibraryEnum {
        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // 这个变量未被使用（呈现灰色）是正常的
        @SpireEnum
        public static CardLibrary.LibraryType Jinhsicardcolor;
    }
}







