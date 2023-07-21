package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Xray extends Module {
    public static ModuleSliderSetting r;
    public static ModuleTickSetting a;
    public static ModuleTickSetting b;
    public static ModuleTickSetting c;
    public static ModuleTickSetting d;
    public static ModuleTickSetting e;
    public static ModuleTickSetting f;
    public static ModuleTickSetting g;
    private java.util.Timer t;
    private List<BlockPos> ren;

    public Xray() {
        super("Xray", Module.category.Visual, 0);
        this.registerSetting(r = new ModuleSliderSetting("Range", 20.0D, 5.0D, 50.0D, 1.0D));
        this.registerSetting(a = new ModuleTickSetting("Iron", true));
        this.registerSetting(b = new ModuleTickSetting("Gold", true));
        this.registerSetting(c = new ModuleTickSetting("Diamond", true));
        this.registerSetting(d = new ModuleTickSetting("Emerald", true));
        this.registerSetting(e = new ModuleTickSetting("Lapis", true));
        this.registerSetting(f = new ModuleTickSetting("Redstone", true));
        this.registerSetting(g = new ModuleTickSetting("Coal", true));
    }

    public void onEnable() {
        this.ren = new ArrayList<>();
        (this.t = new java.util.Timer()).scheduleAtFixedRate(this.findBlocks(), 0L, 200L);
    }

    public void onDisable() {
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }

    }

    private TimerTask findBlocks() {
        return new TimerTask() {
            public void run() {
                Xray.this.ren.clear();
                int ra = (int) Xray.r.getInput();
                for (int y = ra; y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                            Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                            if (Xray.a.isToggled() && bl.equals(Blocks.iron_ore) || Xray.b.isToggled() && bl.equals(Blocks.gold_ore) || Xray.c.isToggled() && bl.equals(Blocks.diamond_ore) || Xray.d.isToggled() && bl.equals(Blocks.emerald_ore) || Xray.e.isToggled() && bl.equals(Blocks.lapis_ore) || Xray.f.isToggled() && bl.equals(Blocks.redstone_ore) || Xray.g.isToggled() && bl.equals(Blocks.coal_ore)) {
                                Xray.this.ren.add(p);
                            }
                        }
                    }
                }
            }
        };
    }

    @SubscribeEvent
    public void renderBlockList(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame() && !this.ren.isEmpty()) {
            List<BlockPos> tRen = new ArrayList<>(this.ren);

            for (BlockPos p : tRen) {
                this.drawHighlight(p);
            }
        }

    }

    private void drawHighlight(BlockPos p) {
        int[] rgb = this.getBlockColor(mc.theWorld.getBlockState(p).getBlock());
        if (rgb[0] + rgb[1] + rgb[2] != 0) {
            GLUtils.HighlightBlock(p, (new Color(rgb[0], rgb[1], rgb[2])).getRGB(), false);
        }

    }

    private int[] getBlockColor(Block b) {
        int red = 0;
        int green = 0;
        int blue = 0;
        if (b.equals(Blocks.iron_ore)) {
            red = 255;
            green = 255;
            blue = 255;
        } else if (b.equals(Blocks.gold_ore)) {
            red = 255;
            green = 255;
        } else if (b.equals(Blocks.diamond_ore)) {
            green = 220;
            blue = 255;
        } else if (b.equals(Blocks.emerald_ore)) {
            red = 35;
            green = 255;
        } else if (b.equals(Blocks.lapis_ore)) {
            green = 50;
            blue = 255;
        } else if (b.equals(Blocks.redstone_ore)) {
            red = 255;
        } else if (b.equals(Blocks.obsidian)) {
            red = 30;
            blue = 135;
        }

        return new int[]{red, green, blue};
    }
}
