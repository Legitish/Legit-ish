package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.events.impl.ClientTickEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {
    public InvMove() {
        super("Inv Move", category.Movement, 0);
        this.registerSetting(new ModuleDesc("Allows you to move while in your inventory."));
        this.registerSetting(new ModuleDesc("Doesn't bypass on Hypixel."));

    }

    @Subscribe(eventClass = ClientTickEvent.class)
    public void onTick(ClientTickEvent e) {
        if (mc.currentScreen != null) {
            if (mc.currentScreen instanceof GuiChat) {
                return;
            }
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
        }
    }
}

