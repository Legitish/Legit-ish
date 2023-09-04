package legitish.module.modules.player;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

public class Refill extends Module {
    private final ModuleSliderSetting minDelay;
    private final ModuleSliderSetting maxDelay;
    private final ModuleTickSetting pots;
    private final ModuleTickSetting soup;
    private int lastShiftedPotIndex = -1;
    private long lastUsageTime = 0;
    private long delay = 800;
    private boolean refillOpened = false;

    public Refill() {
        super("Refill", category.Player, 0);
        this.registerSetting(minDelay = new ModuleSliderSetting("Min Delay", 500, 50, 500, 50));
        this.registerSetting(maxDelay = new ModuleSliderSetting("Max Delay", 500, 50, 500, 50));
        this.registerSetting(pots = new ModuleTickSetting("Pots", true));
        this.registerSetting(soup = new ModuleTickSetting("Soup", true));
    }

    private static boolean isHotbarFull() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }

    public void onEnable() {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().currentScreen == null) {
            refillOpened = true;
            newDelay();
            openInventory();
            if (isHotbarFull()) {
                closeInventory();
            }
        }
    }

    private void openInventory() {
        mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
    }

    public void refillHotbar() {
        int nextPotIndex = findNextPotIndex();
        if (nextPotIndex != -1) {
            newDelay();
            shiftRightClickItem(nextPotIndex);
            lastShiftedPotIndex = nextPotIndex;
            if (isHotbarFull()) {
                closeInventory();
            }
        } else {
            closeInventory();
        }
    }

    public int findNextPotIndex() {
        int inventorySize = mc.thePlayer.inventory.getSizeInventory();
        int startIndex = (lastShiftedPotIndex + 1 + 9) % inventorySize;

        for (int i = startIndex; i != startIndex - 1; i = (i + 1) % inventorySize) {
            int slotIndex = i % inventorySize;

            if (slotIndex < 9) {
                continue;
            }

            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slotIndex);

            if (isValidStack(stack)) {
                lastShiftedPotIndex = slotIndex;
                return slotIndex;
            }

            if (i == (startIndex - 1 + inventorySize) % inventorySize) {
                break;
            }
        }

        return -1;
    }

    public void newDelay() {
        double minDelayValue = minDelay.getInput();
        double maxDelayValue = maxDelay.getInput();
        delay = (long) (minDelayValue + (Math.random() * (maxDelayValue - minDelayValue)));
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        long currentTime = System.currentTimeMillis();
        if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && !isHotbarFull()) {
            if (refillOpened && currentTime - lastUsageTime >= delay) {
                refillHotbar();
                lastUsageTime = currentTime;
            }
        } else if (Minecraft.getMinecraft().currentScreen == null && this.isEnabled()) {
            this.disable();
        }
    }

    private boolean isValidStack(ItemStack stack) {
        if (stack == null) return false;
        return (pots.isToggled() && isPot(stack)) || (soup.isToggled() && isSoup(stack));
    }

    private boolean isSoup(ItemStack stack) {
        return stack.getItem() == Items.mushroom_stew;
    }

    private boolean isPot(ItemStack stack) {
        if (stack.getItem() instanceof ItemPotion) {
            int metadata = stack.getMetadata();
            return metadata == 16421;
        }
        return false;
    }

    private void shiftRightClickItem(int slotIndex) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotIndex, 0, 1, mc.thePlayer);
        mc.playerController.updateController();
    }

    private void closeInventory() {
        mc.thePlayer.closeScreen();
        mc.playerController.sendPacketDropItem(mc.thePlayer.inventory.getItemStack());
        refillOpened = false;
        this.disable();
    }
}
