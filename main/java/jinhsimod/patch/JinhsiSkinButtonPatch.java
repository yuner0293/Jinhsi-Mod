package jinhsimod.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import jinhsimod.character.JinhsiSkin;
import jinhsimod.modcore.Jinhsimod;

import java.io.IOException;

public class JinhsiSkinButtonPatch {

    // ==================== UI 调整参数 (统一缩放逻辑) ====================
    private static final float UI_CENTER_X = 0.82f;       // 水平位置 (0-1)
    private static final float UI_CENTER_Y = 0.50f;       // 垂直地平线高度 (0-1)

    private static final float PREVIEW_SCALE = 1.0f;      // 动态模型缩放 (对应 Spine 的比例)
    private static final float STATIC_SCALE = 1.0f;       // 静态图缩放 (基于原图像素的比例)

    private static final float BUTTON_Y_OFFSET = -80.0f;  // 按钮相对于地平线的偏移 (负数向下)
    private static final float ARROW_SPACING = 140.0f;    // 左右箭头间距
    private static final String ANIMATION_NAME = "newAnimation";
    private static final boolean SHOW_DEBUG_LINE = false;  // 【调试开关】设为 false
    // =================================================================

    //UI交互
    public static Hitbox leftArrowHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
    public static Hitbox rightArrowHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);

    //预设资源
    public static TextureAtlas atlas;
    public static Skeleton skeleton;
    public static AnimationState state;
    public static Texture staticTexture;
    private static int lastSkinIndex = -1;  //记录上传皮肤索引

    //判断是否显示皮肤切换界面
    public static boolean shouldShow() {
        CharacterSelectScreen screen = CardCrawlGame.mainMenuScreen.charSelectScreen;
        boolean anySelected = ReflectionHacks.getPrivate(screen, CharacterSelectScreen.class, "anySelected");
        return CardCrawlGame.chosenCharacter == Jinhsimod.Jinhsi && anySelected;
    }

    //刷新皮肤预览资源
    public static void refreshPreview() {
        if (Jinhsimod.skins.isEmpty()) return;
        JinhsiSkin skin = Jinhsimod.skins.get(Jinhsimod.currentSkinIndex);

        skeleton = null;
        staticTexture = null;

        if (skin.atlas != null && skin.json != null) {
            try {
                atlas = new TextureAtlas(Gdx.files.internal(skin.atlas));
                SkeletonJson json = new SkeletonJson(atlas);
                // 动态模型缩放
                json.setScale(Settings.renderScale / PREVIEW_SCALE);
                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skin.json));
                skeleton = new Skeleton(skeletonData);

                skeleton.setColor(Color.WHITE);
                AnimationStateData stateData = new AnimationStateData(skeletonData);
                state = new AnimationState(stateData);
                state.setAnimation(0, ANIMATION_NAME, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            staticTexture = ImageMaster.loadImage(skin.img);
        }
        lastSkinIndex = Jinhsimod.currentSkinIndex;
    }


    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderPatch {
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
            if (shouldShow()) {
                float centerX = Settings.WIDTH * UI_CENTER_X;
                float centerY = Settings.HEIGHT * UI_CENTER_Y;

                // --- 调试辅助线 ---
                if (SHOW_DEBUG_LINE) {
                    sb.setColor(Color.RED);
                    // 画横线（轴）
                    sb.draw(ImageMaster.WHITE_SQUARE_IMG, centerX - 100f * Settings.scale, centerY, 200f * Settings.scale, 2f * Settings.scale);
                    // 画竖线（中心）
                    sb.draw(ImageMaster.WHITE_SQUARE_IMG, centerX, centerY - 100f * Settings.scale, 2f * Settings.scale, 200f * Settings.scale);
                    sb.setColor(Color.WHITE);
                }

                // --- 1. 渲染人物 (轴心对齐) ---
                if (skeleton != null) {
                    state.update(Gdx.graphics.getDeltaTime());
                    state.apply(skeleton);
                    skeleton.updateWorldTransform();
                    // 动态图设置原点为 (centerX, centerY)
                    float spineH = skeleton.getData().getHeight() * (Settings.renderScale / PREVIEW_SCALE);

                    skeleton.setPosition(centerX, centerY + spineH / 2f);

                    sb.end();
                    CardCrawlGame.psb.begin();
                    AbstractCreature.sr.draw(CardCrawlGame.psb, skeleton);
                    CardCrawlGame.psb.end();
                    sb.begin();

                } else if (staticTexture != null) {
                    // 静态图根据 STATIC_SCALE 比例缩放
                    float finalW = staticTexture.getWidth() * Settings.scale * STATIC_SCALE;
                    float finalH = staticTexture.getHeight() * Settings.scale * STATIC_SCALE;

                    // 绘制坐标：水平居中，底部对齐地平线 centerY
                    sb.draw(staticTexture,
                            centerX - finalW / 2f,
                            centerY,
                            finalW,
                            finalH);
                }

                // --- 2. 渲染 UI 按钮 (基于地平线偏移) ---
                float btnY = centerY + (BUTTON_Y_OFFSET * Settings.scale);

                leftArrowHb.move(centerX - (ARROW_SPACING * Settings.scale), btnY);
                rightArrowHb.move(centerX + (ARROW_SPACING * Settings.scale), btnY);

                sb.setColor(leftArrowHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
                sb.draw(ImageMaster.CF_LEFT_ARROW, leftArrowHb.cX - 24.0f * Settings.scale, leftArrowHb.cY - 24.0f * Settings.scale, 24.0f, 24.0f, 48.0f, 48.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 48, 48, false, false);

                sb.setColor(rightArrowHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
                sb.draw(ImageMaster.CF_RIGHT_ARROW, rightArrowHb.cX - 24.0f * Settings.scale, rightArrowHb.cY - 24.0f * Settings.scale, 24.0f, 24.0f, 48.0f, 48.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 48, 48, false, false);

                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, Jinhsimod.skins.get(Jinhsimod.currentSkinIndex).name, centerX, btnY, Color.GOLD);

                leftArrowHb.render(sb);
                rightArrowHb.render(sb);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdatePatch {
        public static void Postfix(CharacterSelectScreen __instance) {
            if (shouldShow()) {
                if (lastSkinIndex != Jinhsimod.currentSkinIndex) {
                    refreshPreview();
                }

                leftArrowHb.update();
                rightArrowHb.update();

                if (leftArrowHb.justHovered || rightArrowHb.justHovered) CardCrawlGame.sound.play("UI_HOVER");

                if (InputHelper.justClickedLeft) {
                    if (leftArrowHb.hovered) leftArrowHb.clickStarted = true;
                    if (rightArrowHb.hovered) rightArrowHb.clickStarted = true;
                }

                if (leftArrowHb.clicked || rightArrowHb.clicked) {
                    if (leftArrowHb.clicked) {
                        leftArrowHb.clicked = false;
                        Jinhsimod.currentSkinIndex = (Jinhsimod.currentSkinIndex - 1 < 0) ? Jinhsimod.skins.size() - 1 : Jinhsimod.currentSkinIndex - 1;
                    } else {
                        rightArrowHb.clicked = false;
                        Jinhsimod.currentSkinIndex = (Jinhsimod.currentSkinIndex + 1) % Jinhsimod.skins.size();
                    }
                    CardCrawlGame.sound.play("UI_CLICK_1");
                    refreshPreview();
                    saveConfig();
                }
            }
        }
    }

    private static void saveConfig() {
        try {
            SpireConfig config = new SpireConfig("Jinhsimod", "Common");
            config.setInt("selectedSkin", Jinhsimod.currentSkinIndex);
            config.save();
        } catch (IOException e) { e.printStackTrace(); }
    }
}