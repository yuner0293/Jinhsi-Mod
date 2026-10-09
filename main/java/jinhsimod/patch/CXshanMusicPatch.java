package jinhsimod.patch; // 请确保这里的包名和你项目结构一致

import com.badlogic.gdx.audio.Music;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.evacipated.cardcrawl.modthespire.lib.*;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class CXshanMusicPatch {
    @SpirePrefixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        // 这里的 key 就是你在 playTempBgmInstantly 传入的字符串
        if ("CXSHAN_BGM".equals(key)) {
            // 使用 MainMusic.newMusic 能够绕过 TempMusic 的自动路径补全
            // 路径直接从 resources 下的第一级文件夹开始写
            return SpireReturn.Return(MainMusic.newMusic("Jinhsimodresources/audio/music/CXshanmusic.ogg"));

        }
        return SpireReturn.Continue();
    }
}