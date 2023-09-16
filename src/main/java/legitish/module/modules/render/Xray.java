package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Xray extends Module {
    public static ModuleSliderSetting range;
    public static ModuleTickSetting iron, gold, diamond, emerald, lapis, redstone, coal;
    private java.util.Timer timer;
    private List<BlockPos> blockPosList;

    public Xray() {
        super("Xray", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Highlights certain blocks."));
        this.registerSetting(range = new ModuleSliderSetting("Range", 20.0D, 5.0D, 50.0D, 1.0D));
        this.registerSetting(iron = new ModuleTickSetting("Iron", true));
        this.registerSetting(gold = new ModuleTickSetting("Gold", true));
        this.registerSetting(diamond = new ModuleTickSetting("Diamond", true));
        this.registerSetting(emerald = new ModuleTickSetting("Emerald", true));
        this.registerSetting(lapis = new ModuleTickSetting("Lapis", true));
        this.registerSetting(redstone = new ModuleTickSetting("Redstone", true));
        this.registerSetting(coal = new ModuleTickSetting("Coal", true));
    }

    public void onEnable() {
        this.blockPosList = new ArrayList<>();
        (this.timer = new java.util.Timer()).scheduleAtFixedRate(this.findBlocks(), 0L, 200L);
    }

    public void onDisable() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }

    }

    private TimerTask findBlocks() {
        return new TimerTask() {
            public void run() {
                Xray.this.blockPosList.clear();
                int ra = (int) Xray.range.getInput();
                for (int y = ra; y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                            Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                            if (Xray.iron.isToggled() && bl.equals(Blocks.iron_ore) || Xray.gold.isToggled() && bl.equals(Blocks.gold_ore) || Xray.diamond.isToggled() && bl.equals(Blocks.diamond_ore) || Xray.emerald.isToggled() && bl.equals(Blocks.emerald_ore) || Xray.lapis.isToggled() && bl.equals(Blocks.lapis_ore) || Xray.redstone.isToggled() && bl.equals(Blocks.redstone_ore) || Xray.coal.isToggled() && bl.equals(Blocks.coal_ore)) {
                                Xray.this.blockPosList.add(p);
                            }
                        }
                    }
                }
            }
        };
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void renderBlockList(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame() && !this.blockPosList.isEmpty()) {
            List<BlockPos> tRen = new ArrayList<>(this.blockPosList);

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
