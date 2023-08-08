package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.impl.DrawBlockHighlightEvent;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoPlace extends Module {
    public static ModuleDesc desc;
    public static ModuleTickSetting holdRight;
    public static ModuleSliderSetting frameDelay;
    private double lfd = 0.0D;
    private long l = 0L;
    private int f = 0;
    private MovingObjectPosition lm = null;
    private BlockPos lp = null;

    public AutoPlace() {
        super("Auto Place", category.Player, 0);
        this.registerSetting(desc = new ModuleDesc("Automatically places blocks under you."));
        this.registerSetting(frameDelay = new ModuleSliderSetting("Frame delay (fps/80)", 8.0D, 0.0D, 30.0D, 1.0D));
        this.registerSetting(holdRight = new ModuleTickSetting("Hold right", true));
    }

    public void guiUpdate() {
        if (this.lfd != frameDelay.getInput()) {
            this.rv();
        }

        this.lfd = frameDelay.getInput();
    }

    public void onDisable() {
        if (holdRight.isToggled()) {
            this.rd(4);
        }

        this.rv();
    }

    @SubscribeEvent
    public void onTick(TickEvent tickEvent) {
        if (holdRight.isToggled() && Mouse.isButtonDown(1) && !mc.thePlayer.capabilities.isFlying && !ModuleManager.fastPlace.isEnabled()) {
            ItemStack i = mc.thePlayer.getHeldItem();
            if (i == null || !(i.getItem() instanceof ItemBlock)) {
                return;
            }

            this.rd(mc.thePlayer.motionY > 0.0D ? 1 : 1000);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = DrawBlockHighlightEvent.class)
    public void onHighlight(DrawBlockHighlightEvent event) {
        if (GameUtils.isPlayerInGame()) {
            if (mc.currentScreen == null && !mc.thePlayer.capabilities.isFlying) {
                ItemStack i = mc.thePlayer.getHeldItem();
                if (i != null && i.getItem() instanceof ItemBlock) {
                    MovingObjectPosition m = mc.objectMouseOver;
                    if (m != null && m.typeOfHit == MovingObjectType.BLOCK && m.sideHit != EnumFacing.UP && m.sideHit != EnumFacing.DOWN) {
                        if (this.lm != null && (double) this.f < frameDelay.getInput()) {
                            ++this.f;
                        } else {
                            this.lm = m;
                            BlockPos pos = m.getBlockPos();
                            if (this.lp == null || pos.getX() != this.lp.getX() || pos.getY() != this.lp.getY() || pos.getZ() != this.lp.getZ()) {
                                Block b = mc.theWorld.getBlockState(pos).getBlock();
                                if (b != null && b != Blocks.air && !(b instanceof BlockLiquid)) {
                                    if (!holdRight.isToggled() || Mouse.isButtonDown(1)) {
                                        long n = System.currentTimeMillis();
                                        if (n - this.l >= 25L) {
                                            this.l = n;
                                            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, i, pos, m.sideHit, m.hitVec)) {
                                                MouseUtils.sendClick(1, true);
                                                mc.thePlayer.swingItem();
                                                mc.getItemRenderer().resetEquippedProgress();
                                                MouseUtils.sendClick(1, false);
                                                this.lp = pos;
                                                this.f = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void rd(int i) {
        try {
            if (FastPlace.field != null) {
                FastPlace.field.set(mc, i);
            }
        } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
        }
    }

    private void rv() {
        this.lp = null;
        this.lm = null;
        this.f = 0;
    }
}
