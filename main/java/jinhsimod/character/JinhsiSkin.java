package jinhsimod.character;

// 这是一个逻辑概念类，用于存放皮肤资源信息
public class JinhsiSkin {
    public String name;
    public String img;      // 静态图或Spine的贴图
    public String atlas;    // Spine骨骼数据，静态图则为 null
    public String json;     // Spine结构数据，静态图则为 null
    public String shoulder1;
    public String shoulder2;
    public String corpse;

    public JinhsiSkin(String name, String folder, boolean isSpine) {
        this.name = name;
        this.img = folder + "/character1.png";
        this.shoulder1 = folder + "/shoulder1.png";
        this.shoulder2 = folder + "/shoulder2.png";
        this.corpse = folder + "/corpse.png";
        if (isSpine) {
            this.atlas = folder + "/character.atlas";
            this.json = folder + "/character.json";
        } else {
            this.atlas = null;
            this.json = null;
        }
    }
}