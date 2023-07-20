package legitish.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ChatComponentText;

public class GameUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean getWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }

    public static boolean isPlayerInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static boolean isHypixel() {
        return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
    }

    public static void sendChat(String txt) {
        if (isPlayerInGame()) {
            String message = formatColorCode("[!] " + txt);
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static String formatColorCode(String txt) {
        return txt.replaceAll("&", "§");
    }
}
