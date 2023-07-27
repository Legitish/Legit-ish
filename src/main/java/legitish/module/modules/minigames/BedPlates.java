package legitish.module.modules.minigames;

import legitish.module.Module;
import legitish.module.modules.client.Notifications;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.ColorUtils;
import legitish.utils.CooldownUtils;
import legitish.utils.GameUtils;
import legitish.utils.render.RRectUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class BedPlates extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ModuleSliderSetting updateRate, yShift, layers;
    public static ModuleTickSetting bedwarsOnly;
    private final CooldownUtils updateCooldown = new CooldownUtils(0);
    private final List<BlockPos> beds = new ArrayList<>();
    private final List<List<Block>> bedBlocks = new ArrayList<>();
    private BlockPos ownBed;


    public BedPlates() {
        super("Bed Plates", Module.category.Visual, 0);
        this.registerSetting(yShift = new ModuleSliderSetting("Y-shift", 2, -5, 10, 1));
        this.registerSetting(updateRate = new ModuleSliderSetting("Update rate (ms)", 1000, 250, 5000, 250));
        this.registerSetting(layers = new ModuleSliderSetting("Layers", 3, 1, 10, 1));
        this.registerSetting(bedwarsOnly = new ModuleTickSetting("Bedwars only", true));
    }

    public void onEnable() {
        for (int i = 0; i < 8; i++) {
            this.beds.add(null);
            this.bedBlocks.add(new ArrayList<>());
        }
    }

    public void onDisable() {
        this.beds.clear();
        this.bedBlocks.clear();
        this.ownBed = null;
    }

    public void updateBeds() {
        if (GameUtils.isPlayerInGame() && (!bedwarsOnly.isToggled() || GameUtils.isBedwars())) {
            findOwnBed();
            if (ownBed != null) {
                findBed(ownBed.getZ(), ownBed.getY(), ownBed.getX(), 1);
                findBed(-ownBed.getZ(), ownBed.getY(), ownBed.getX(), 2);
                findBed(ownBed.getZ(), ownBed.getY(), -ownBed.getX(), 3);
                findBed(-ownBed.getZ(), ownBed.getY(), -ownBed.getX(), 4);
                findBed(-ownBed.getX(), ownBed.getY(), ownBed.getZ(), 5);
                findBed(ownBed.getX(), ownBed.getY(), -ownBed.getZ(), 6);
                findBed(-ownBed.getX(), ownBed.getY(), -ownBed.getZ(), 7);
            }
        }
    }

    @SubscribeEvent
    public void renderBlockList(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame() && !this.beds.isEmpty()) {
            int index = 0;
            if (updateCooldown.hasFinished()) {
                updateBeds();
                updateCooldown.setCooldown((long) updateRate.getInput());
                updateCooldown.start();
            }
            for (BlockPos blockPos : this.beds) {
                if (beds.get(index) != null) {
                    this.drawPlate(blockPos, index);
                    index++;
                }
            }
        }

    }

    private void drawPlate(BlockPos blockPos, int index) {
        // this is probably terrible gl usage but idk how to use it anyway ¯\_(ツ)_/¯
        float rotateX = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glTranslatef((float) (blockPos.getX() - mc.getRenderManager().viewerPosX + 0.5), (float) (blockPos.getY() - mc.getRenderManager().viewerPosY + yShift.getInput() + 1), (float) (blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ + 0.5));
        glNormal3f(0.0F, 1.0F, 0.0F);
        glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        glRotatef(mc.getRenderManager().playerViewX, rotateX, 0.0F, 0.0F);
        glScaled(-0.01666666753590107D * Math.sqrt(mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())), -0.01666666753590107D * Math.sqrt(mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())), 0.01666666753590107D * Math.sqrt(mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        String dist = Math.round(mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())) + "m";
        RRectUtils.drawRound(Math.max(17.5, bedBlocks.get(index).size() * 17.5) / -2, -0.5, Math.max(17.5, bedBlocks.get(index).size() * 17.5) - 2.5, 26.5, 3, new Color(0, 0, 0, 90));
        mc.fontRendererObj.drawString(dist, -mc.fontRendererObj.getStringWidth(dist) / 2, 0, ColorUtils.getFontColor(1).getRGB());
        double offset = (bedBlocks.get(index).size() * -17.5) / 2;
        for (Block block : bedBlocks.get(index)) {
            ResourceLocation res = new ResourceLocation("legitish/images/" + block.getLocalizedName() + ".png");
            mc.getTextureManager().bindTexture(res);
            Gui.drawModalRectWithCustomSizedTexture((int) offset, 10, 0, 0, 15, 15, (float) 15, (float) 15);
            offset += 17.5;
        }
        glPopMatrix();
    }

    private void findOwnBed() {
        if (this.ownBed == null) {
            Notifications.sendNotification(Notifications.NotificationTypes.INFO, "Finding own bed.", 1000);
            for (int y = 2; y >= -2; --y) {
                for (int x = 0; x <= 20; ++x) {
                    for (int z = 0; z <= 20; ++z) {
                        if (findBed(Module.mc.thePlayer.posX - Module.mc.thePlayer.posX / Math.abs(Module.mc.thePlayer.posX) * x,
                                Module.mc.thePlayer.posY + (double) y,
                                Module.mc.thePlayer.posZ - Module.mc.thePlayer.posZ / Math.abs(Module.mc.thePlayer.posZ) * z, 0)) {
                            this.ownBed = this.beds.get(0);
                            return;
                        }
                    }
                }
            }
        } else {
            findBed(ownBed.getX(), ownBed.getY(), ownBed.getZ(), 0);
        }
    }

    private boolean findBed(double x, double y, double z, int index) {
        BlockPos bedPos = new BlockPos(x, y, z);
        Block bed = Module.mc.theWorld.getBlockState(bedPos).getBlock();
        bedBlocks.get(index).clear();
        beds.set(index, null);
        if (beds.contains(bedPos)) {
            return false;
        }
        if (bed.equals(Blocks.bed)) {
            for (int yi = 0; yi <= layers.getInput(); ++yi) {
                for (int xi = (int) -layers.getInput(); xi <= layers.getInput(); ++xi) {
                    for (int zi = (int) -layers.getInput(); zi <= layers.getInput(); ++zi) {
                        if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.wool) && !bedBlocks.get(index).contains(Blocks.wool)) {
                            bedBlocks.get(index).add(Blocks.wool);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.stained_hardened_clay) && !bedBlocks.get(index).contains(Blocks.stained_hardened_clay)) {
                            bedBlocks.get(index).add(Blocks.stained_hardened_clay);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.stained_glass) && !bedBlocks.get(index).contains(Blocks.stained_glass)) {
                            bedBlocks.get(index).add(Blocks.stained_glass);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.planks) && !bedBlocks.get(index).contains(Blocks.planks)) {
                            bedBlocks.get(index).add(Blocks.planks);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.log) && !bedBlocks.get(index).contains(Blocks.planks)) {
                            bedBlocks.get(index).add(Blocks.planks);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.log2) && !bedBlocks.get(index).contains(Blocks.planks)) {
                            bedBlocks.get(index).add(Blocks.planks);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.end_stone) && !bedBlocks.get(index).contains(Blocks.end_stone)) {
                            bedBlocks.get(index).add(Blocks.end_stone);
                        } else if (Module.mc.theWorld.getBlockState(new BlockPos(bedPos.getX() + xi, bedPos.getY() + yi, bedPos.getZ() + zi)).getBlock().equals(Blocks.obsidian) && !bedBlocks.get(index).contains(Blocks.obsidian)) {
                            bedBlocks.get(index).add(Blocks.obsidian);
                        }
                    }
                }
            }
            beds.set(index, bedPos);
            return true;
        }
        return false;
    }
}
