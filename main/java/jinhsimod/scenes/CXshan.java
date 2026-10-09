package jinhsimod.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.scenes.AbstractScene;


public class CXshan extends AbstractScene {
    private final Texture bg;
    private float snowTimer = 0.0F; // 用于控制下雪频率的计时器

    public CXshan() {
        super("endingScene/scene.atlas");
        this.bg = ImageMaster.loadImage("Jinhsimodresources/img/scenes/CXshan.png");
        this.ambianceName = "AMBIANCE_BEYOND";
        this.fadeInAmbiance();
    }

    // 在 CXshan.java 中修改 update 方法
    @Override
    public void update() {
        super.update();
        this.snowTimer -= Gdx.graphics.getDeltaTime();

        // 每 0.02 秒生成一次，这样既密集又不会撑爆内存
        if (this.snowTimer < 0.0F) {
            this.snowTimer = 0.02F;
            // 每次生成 1-2 个即可，因为残影已经提供了视觉厚度
            for (int i = 0; i < 2; i++) {
                AbstractDungeon.effectsQueue.add(new CXshanSnowEffect());
            }
        }
    }



    @Override
    public void randomizeScene() {
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(this.bg, 0, 0, (float)Settings.WIDTH, (float)Settings.HEIGHT);
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {
        // 如果你想让雪花飘在怪物和玩家“前面”，
        // 也可以不写在 update 里，而是直接在这里处理渲染。
        // 但使用 AbstractDungeon.effectsQueue 是最简单且性能最好的做法。
    }

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireBg, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.bg != null) {
            this.bg.dispose();
        }
    }
}