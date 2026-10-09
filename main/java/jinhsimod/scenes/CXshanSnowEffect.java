package jinhsimod.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CXshanSnowEffect extends AbstractGameEffect {
    private float x, y;
    private float vX, vY;
    private TextureAtlas.AtlasRegion img;
    // 用于记录历史位置实现残影
    private static final int GHOST_COUNT = 3;

    public CXshanSnowEffect() {
        this.img = ImageMaster.vfxAtlas.findRegion("env/dustCloud");

        // 初始位置：从屏幕上方和右侧外进入
        this.x = MathUtils.random(Settings.WIDTH * 0.2F, Settings.WIDTH * 1.2F);
        this.y = Settings.HEIGHT + 50.0F * Settings.scale;

        // --- 速度加快 ---
        // 向左下方疾驰
        this.vX = MathUtils.random(-800.0F, -400.0F) * Settings.scale;
        this.vY = MathUtils.random(-1000.0F, -600.0F) * Settings.scale;

        this.duration = 1.5F; // 速度快了，存活时间缩短
        this.scale = MathUtils.random(0.1F, 0.3F) * Settings.scale;
        this.color = Color.WHITE.cpy();
        this.color.a = 0.0F;
    }

    @Override
    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();

        // 快速淡入淡出
        if (this.duration > 1.0F) {
            this.color.a = MathUtils.lerp(this.color.a, 0.6F, Gdx.graphics.getDeltaTime() * 10.0F);
        } else {
            this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 3.0F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F || this.y < -50.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // --- 残影处理 ---
        // 我们沿着速度的反方向画几个稍淡的图层
        for (int i = GHOST_COUNT; i > 0; i--) {
            // 计算残影位置（根据当前速度回溯）
            float ghostX = this.x - (this.vX * Gdx.graphics.getDeltaTime() * i * 1.5F);
            float ghostY = this.y - (this.vY * Gdx.graphics.getDeltaTime() * i * 1.5F);

            // 残影颜色更淡
            Color ghostColor = this.color.cpy();
            ghostColor.a /= (i + 1);
            sb.setColor(ghostColor);

            sb.draw(this.img, ghostX, ghostY, img.packedWidth/2f, img.packedHeight/2f,
                    img.packedWidth, img.packedHeight, this.scale, this.scale, this.rotation);
        }

        // 绘制本体
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, img.packedWidth/2f, img.packedHeight/2f,
                img.packedWidth, img.packedHeight, this.scale, this.scale, this.rotation);
    }

    public void dispose() {}
}