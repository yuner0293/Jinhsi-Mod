package jinhsimod.modcore;

import static basemod.BaseMod.registerModBadge;
import static com.megacrit.cardcrawl.core.Settings.language;
import static com.megacrit.cardcrawl.helpers.CardLibrary.getAllCards;
import static jinhsimod.signatureLib.utils.internal.SignatureHelperInternal.hasSignature;

import basemod.AutoAdd;
import basemod.ModLabeledToggleButton;
import basemod.eventUtil.AddEventParams;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import jinhsimod.action.Haveshiyun;
import jinhsimod.action.XTattack;
import jinhsimod.cards.attack.SoBDawn;
import jinhsimod.cards.power.Chen;
import jinhsimod.cards.skill.BaoDi;
import jinhsimod.character.JinhsiSkin;
import jinhsimod.character.Jinhsicharacter;
import com.badlogic.gdx.graphics.Color;
import jinhsimod.event.DestinyEvent;
import jinhsimod.event.ShiShuoEvent;
import jinhsimod.monster.Jue;
import jinhsimod.potion.YCsu;
import jinhsimod.relics.*;
import jinhsimod.signatureLib.utils.internal.ConfigHelper;
import jinhsimod.signatureLib.utils.internal.SignatureHelperInternal;
import jinhsimod.variable.Damage;
import jinhsimod.variable.Precent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;


@SpireInitializer // 加载mod的注解
public class Jinhsimod implements
        EditCardsSubscriber, EditStringsSubscriber,
        EditCharactersSubscriber,EditRelicsSubscriber,
        EditKeywordsSubscriber,PostInitializeSubscriber,
        OnStartBattleSubscriber,PostCreateStartingRelicsSubscriber{

    //颜色定义
    public static final Color Jinhsicolor =
            CardHelper.getColor(233, 233, 189);  // 使用CardHelper
    //------char----------//
    //立绘
    private static final String Jinhsi_portrait =
            "Jinhsimodresources/img/char/JSillu.png";
    //人物界面按钮
    private static final String Jinhsi_button =
            "Jinhsimodresources/img/char/Character_Button.png";
    //卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB =
            "Jinhsimodresources/img/char/small_orb.png";
    //卡牌预览界面的能量图标
    private static final String BIG_ORB =
            "Jinhsimodresources/img/char/card_orb.png";
    //小尺寸的能量图标
    private static final String ENEYGY_ORB =
            "Jinhsimodresources/img/char/cost_orb.png";

    //------512------//
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 =
            "Jinhsimodresources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 =
            "Jinhsimodresources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 =
            "Jinhsimodresources/img/512/bg_skill_512.png";

    //------1024------//
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 =
            "Jinhsimodresources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 =
            "Jinhsimodresources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 =
            "Jinhsimodresources/img/1024/bg_skill.png";

    public static final AbstractCard.CardColor Jinhsicardcolor =
            jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsicardcolor;

    public static final AbstractPlayer.PlayerClass Jinhsi =
            jinhsimod.character.Jinhsicharacter.PlayerColorEnum.Jinhsi;

    // 皮肤变量
    // 0 是 qing, 1 是 zhuo
    public static int currentSkinIndex = 0;
    public static java.util.ArrayList<JinhsiSkin> skins = new java.util.ArrayList<>();

    //颜色
    public Jinhsimod(){
        BaseMod.subscribe(this);


        //注册颜色
        BaseMod.addColor(Jinhsicardcolor, Jinhsicolor.cpy(), Jinhsicolor.cpy(), Jinhsicolor.cpy(),
                Jinhsicolor.cpy(), Jinhsicolor.cpy(), Jinhsicolor.cpy(), Jinhsicolor.cpy(),
                BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,
                ENEYGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,
                BIG_ORB,SMALL_ORB);
    }


    public static void initialize(){
        new Jinhsimod();

        //皮肤
        // 在 Jinhsimod.java 的 initialize 方法末尾添加
        try {
            SpireConfig config = new SpireConfig("Jinhsimod", "Common");
            if (config.has("selectedSkin")) {
                currentSkinIndex = config.getInt("selectedSkin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    //本地化判断
    public void receiveEditStrings(){
        String lang;
        if (language == Settings.GameLanguage.ZHS){
            lang = "ZHS";
        }else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class,
                "Jinhsimodresources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                "Jinhsimodresources/localization/" + lang + "/character.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                "Jinhsimodresources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                "Jinhsimodresources/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                "Jinhsimodresources/localization/" + lang + "/monsters.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                "Jinhsimodresources/localization/" + lang + "/potions.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                "Jinhsimodresources/localization/" + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(EventStrings.class,
                "Jinhsimodresources/localization/" + lang + "/event.json");



    }

    //注册卡包
    public void receiveEditCards() {
        new AutoAdd("Jinhsimod") //
                // 寻找所有和此类同一个包及内部包的类
                .packageFilter(SoBDawn.class)
                    .setDefaultSeen(true) // 是否将卡牌标为可见
                        .cards(); // 开始批量添加卡牌
        new AutoAdd("Jinhsimod")
                .packageFilter(BaoDi.class)
                        .setDefaultSeen(true)
                                .cards();
        new AutoAdd("Jinhsimod")
                .packageFilter(Chen.class)
                    .setDefaultSeen(true)
                        .cards();
        //动态变量
        BaseMod.addDynamicVariable(new Damage());
        BaseMod.addDynamicVariable(new Precent());

    }

    // 注册人物
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Jinhsicharacter(CardCrawlGame.playerName),
                Jinhsi_button, Jinhsi_portrait, Jinhsi);
        this.receiveEditPotions();
    }



    //注册遗物
    //BaseMod.addRelic(new HJJCsheng(), RelicType.SHARED);全局
    //BaseMod.addRelicToCustomPool(new YueKa(), Jinhsicardcolor);独有
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new Drscale(),Jinhsicardcolor);
        BaseMod.addRelicToCustomPool(new HJJCsheng(), Jinhsicardcolor);
        BaseMod.addRelicToCustomPool(new XLbao(), Jinhsicardcolor);
        BaseMod.addRelicToCustomPool(new YueKa(), Jinhsicardcolor);
        BaseMod.addRelicToCustomPool(new DBSxiang(), Jinhsicardcolor);
    }

    //注册药水
    public void receiveEditPotions() {
        BaseMod.addPotion(
                YCsu.class, // 药水类
                null,// 液体颜色
                null, // 瓶子轮廓颜色（可选）
                null, // 光泽颜色（可选）
                YCsu.POTION_ID// 药水ID
        );
    }

    //加入关键词
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }
        String json = Gdx.files.internal(
                "Jinhsimodresources/localization/" + lang + "/Keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("keyword", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
    public static SpireConfig jinhsiConfig;
    public static boolean enableSuiZhuBoss = true; // 默认开启

    @Override
    public void receivePostInitialize() {
        // 调用怪物注册方法
        receiveEditMonsters();
        initializeSkins();


        BaseMod.addEvent(new AddEventParams.Builder(ShiShuoEvent.ID, ShiShuoEvent.class)
                .spawnCondition(() -> false) // 这样地图随机格子里绝对遇不到
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(DestinyEvent.ID, DestinyEvent.class)
                .spawnCondition(() -> false)
                .create());

        // 3. 初始化配置 (SpireConfig)
        Properties jinhsiDefaultSettings = new Properties();
        jinhsiDefaultSettings.setProperty("enableSuiZhuBoss", "TRUE");
        try {
            // 创建或读取名为 JinhsiConfig.prop 的文件
            jinhsiConfig = new SpireConfig("Jinhsimod", "JinhsiConfig", jinhsiDefaultSettings);
            enableSuiZhuBoss = jinhsiConfig.getBool("enableSuiZhuBoss");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. 注册设置面板
        registerModSettingsPanel();
        jinhsimod.signatureLib.utils.internal.SignatureHelperInternal.initLibraryTypeNotice();

    }

    private void registerModSettingsPanel() {
        // 获取本地化文本（建议在 ui.json 中添加相关词条）
        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("JinhsiModConfigMenu");
        String labelText = (uiStrings != null) ? uiStrings.TEXT[0] : "Challenge SuiZhu Boss after Heart";
        String signatureToggleText = (uiStrings != null && uiStrings.TEXT.length > 1) ? uiStrings.TEXT[1] : "Enable all alternate card arts";

        // 加载 Mod 图标 (32x32)
        com.badlogic.gdx.graphics.Texture badgeTexture = com.megacrit.cardcrawl.helpers.ImageMaster.loadImage("Jinhsimodresources/img/UI/config/config.png");

        // 创建面板
        basemod.ModPanel settingsPanel = new basemod.ModPanel();

        // 添加勾选框
        basemod.ModLabeledToggleButton bossToggle = new basemod.ModLabeledToggleButton(
                labelText,
                350.0f, 750.0f, com.megacrit.cardcrawl.core.Settings.CREAM_COLOR,
               FontHelper.charDescFont,
                enableSuiZhuBoss, settingsPanel,
                (label) -> {},
                (button) -> {
                    enableSuiZhuBoss = button.enabled;
                    try {
                        jinhsiConfig.setBool("enableSuiZhuBoss", enableSuiZhuBoss);
                        jinhsiConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        // 2. 💡【全局异画一键控制勾选框】（Y 坐标：680.0f，向下延续不重叠）
        // 默认状态：我们可以判定是不是所有异画都已经启用了
        boolean allSignaturesEnabled = true;
        for (AbstractCard card : getAllCards()) {
            if (hasSignature(card)) {
                if (!SignatureHelperInternal.isEnabled(card.cardID)) {
                    allSignaturesEnabled = false;
                    break;
                }
            }
        }
        ModLabeledToggleButton signatureAllToggle = new ModLabeledToggleButton(
                signatureToggleText,
                350.0f, 680.0f, Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                allSignaturesEnabled, settingsPanel,
                (label) -> {},
                (button) -> {
                    if (button.enabled) {
                        // 勾选：全部启用
                        SignatureHelperInternal.enableAllSignatures();
                    } else {
                        // 取消勾选：全部禁用
                        SignatureHelperInternal.disableAllSignatures();
                    }
                    // 实时保存到异画配置文件中
                    ConfigHelper.save();
                });
        settingsPanel.addUIElement(bossToggle);
        settingsPanel.addUIElement(signatureAllToggle);

        // 注册到 BaseMod
        BaseMod.registerModBadge(badgeTexture, "Jinhsi Mod", "yuner", "Add Jinhsi character and custom final event.", settingsPanel);
    }

    public static int suizhuStoredHp = -1;
    public static void resetSuizhuHp() {
        suizhuStoredHp = -1;
    }

    // 怪物注册方法
    private void receiveEditMonsters() {
        // 注册Jue怪物
        BaseMod.addMonster(
                "Jinhsimod:Jue",  // 怪物唯一ID，必须与Jue类中的ID一致
                CardCrawlGame.languagePack.getMonsterStrings("Jinhsimod:Jue").NAME,  // 从本地化文件获取名称
                () -> new Jue(0.0F, 0.0F)  // 怪物构造器（匿名函数形式）
        );
        BaseMod.addMonster(
                jinhsimod.monster.SuiZhuBoss.ID,
                CardCrawlGame.languagePack.getMonsterStrings(jinhsimod.monster.SuiZhuBoss.ID).NAME,
                () -> new jinhsimod.monster.SuiZhuBoss(0.0F, 0.0F)
        );
    }

    public static void initializeSkins() {
        jinhsimod.signatureLib.utils.internal.ConfigHelper.loadConfig();
        jinhsimod.signatureLib.utils.internal.ConfigHelper.conf.setBool("enableDebugging", true);
        skins.clear();
        //本地化
        String[] skinNames = CardCrawlGame.languagePack.getUIString("JinhsiSkinNames").TEXT;
        //名字 路径 是否是动画
        skins.add(new JinhsiSkin(skinNames[0],
                "Jinhsimodresources/img/char/skin/qing", true));
        skins.add(new JinhsiSkin(skinNames[1],
                "Jinhsimodresources/img/char/skin/zhuo", true));
        skins.add(new JinhsiSkin(skinNames[2],
                "Jinhsimodresources/img/char/skin/shuimo", true));

    }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        // 玩家进入新局，重置 BOSS 血量记录
        resetSuizhuHp();
    }


    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        Haveshiyun.clearListeners();
        XTattack.clearListeners();
    }

}




