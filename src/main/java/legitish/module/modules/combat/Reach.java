package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.MouseEvent;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleDoubleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;

import java.util.List;

public class Reach extends Module {
    public static ModuleDoubleSliderSetting reach;
    public static ModuleTickSetting weaponOnly, movingOnly, sprintOnly, hitThroughBlocks;

    public Reach() {
        super("Reach", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Increases your reach."));
        this.registerSetting(reach = new ModuleDoubleSliderSetting("Reach", 3.0D, 3.15D, 3.0D, 6.0D, 0.05D));
        this.registerSetting(weaponOnly = new ModuleTickSetting("Weapon only", false));
        this.registerSetting(movingOnly = new ModuleTickSetting("Moving only", false));
        this.registerSetting(sprintOnly = new ModuleTickSetting("Sprint only", false));
        this.registerSetting(hitThroughBlocks = new ModuleTickSetting("Hit through blocks", false));
    }

    public static boolean callReach() {
        if (!GameUtils.isPlayerInGame()) {
            return false;
        } else if (weaponOnly.isToggled() && !GameUtils.getWeapon()) {
            return false;
        } else if (movingOnly.isToggled() && (double) mc.thePlayer.moveForward == 0.0D && (double) mc.thePlayer.moveStrafing == 0.0D) {
            return false;
        } else if (sprintOnly.isToggled() && !mc.thePlayer.isSprinting()) {
            return false;
        } else {
            if (!hitThroughBlocks.isToggled() && mc.objectMouseOver != null) {
                BlockPos p = mc.objectMouseOver.getBlockPos();
                if (p != null && mc.theWorld.getBlockState(p).getBlock() != Blocks.air) {
                    return false;
                }
            }

            double reach = MathUtils.mmVal(Reach.reach, MathUtils.rand());
            Object[] object = findEntitiesWithinReach(reach);
            if (object == null) {
                return false;
            } else {
                Entity en = (Entity) object[0];
                mc.objectMouseOver = new MovingObjectPosition(en, (Vec3) object[1]);
                mc.pointedEntity = en;
                return true;
            }
        }
    }

    private static Object[] findEntitiesWithinReach(double reach) {
        if (!ModuleManager.reach.isEnabled()) {
            reach = mc.playerController.extendedReach() ? 6.0D : 3.0D;
        }

        Entity renderView = mc.getRenderViewEntity();
        Entity target = null;
        if (renderView == null) {
            return null;
        } else {
            mc.mcProfiler.startSection("pick");
            Vec3 eyePosition = renderView.getPositionEyes(1.0F);
            Vec3 playerLook = renderView.getLook(1.0F);
            Vec3 reachTarget = eyePosition.addVector(playerLook.xCoord * reach, playerLook.yCoord * reach, playerLook.zCoord * reach);
            Vec3 targetHitVec = null;
            List<Entity> targetsWithinReach = mc.theWorld.getEntitiesWithinAABBExcludingEntity(renderView, renderView.getEntityBoundingBox().addCoord(playerLook.xCoord * reach, playerLook.yCoord * reach, playerLook.zCoord * reach).expand(1.0D, 1.0D, 1.0D));
            double adjustedReach = reach;

            for (Entity entity : targetsWithinReach) {
                if (entity.canBeCollidedWith()) {
                    float ex = (float) ((double) entity.getCollisionBorderSize());
                    AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(ex, ex, ex);
                    MovingObjectPosition targetPosition = entityBoundingBox.calculateIntercept(eyePosition, reachTarget);
                    if (entityBoundingBox.isVecInside(eyePosition)) {
                        if (0.0D < adjustedReach || adjustedReach == 0.0D) {
                            target = entity;
                            targetHitVec = targetPosition == null ? eyePosition : targetPosition.hitVec;
                            adjustedReach = 0.0D;
                        }
                    } else if (targetPosition != null) {
                        double distanceToVec = eyePosition.distanceTo(targetPosition.hitVec);
                        if (distanceToVec < adjustedReach || adjustedReach == 0.0D) {
                            if (entity == renderView.ridingEntity) {
                                if (adjustedReach == 0.0D) {
                                    target = entity;
                                    targetHitVec = targetPosition.hitVec;
                                }
                            } else {
                                target = entity;
                                targetHitVec = targetPosition.hitVec;
                                adjustedReach = distanceToVec;
                            }
                        }
                    }
                }
            }

            if (adjustedReach < reach && !(target instanceof EntityLivingBase) && !(target instanceof EntityItemFrame)) {
                target = null;
            }

            mc.mcProfiler.endSection();
            if (target != null && targetHitVec != null) {
                return new Object[]{target, targetHitVec};
            } else {
                return null;
            }
        }
    }

    public void guiUpdate() {
        MathUtils.b(reach);
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = MouseEvent.class)
    public void onMouseUpdate(MouseEvent event) {
        if (GameUtils.isPlayerInGame() && event.button == MouseEvent.Button.LEFT && (!ModuleManager.autoClicker.isEnabled() || !AutoClicker.leftClick.isToggled() || !Mouse.isButtonDown(0))) {
            callReach();
        }
    }
}
