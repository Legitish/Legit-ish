package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

import java.awt.*;
import java.util.Iterator;

public class ChestESP extends Module {
    private final ModuleTickSetting chest, trappedChest, eChest;

    public ChestESP() {
        super("Chest ESP", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Renders an overlay over chests."));
        this.registerSetting(chest = new ModuleTickSetting("Chest", true));
        this.registerSetting(trappedChest = new ModuleTickSetting("Trapped Chest", false));
        this.registerSetting(eChest = new ModuleTickSetting("Enderchest", true));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void renderESP(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = (new Color(255, 255, 255)).getRGB();
            for (TileEntity te : mc.theWorld.loadedTileEntityList) {
                if (te instanceof TileEntityChest && te.getBlockType() == Block.getBlockById(54) && chest.isToggled()
                        || te instanceof TileEntityChest && te.getBlockType() == Block.getBlockById(146) && trappedChest.isToggled()
                        || te instanceof TileEntityEnderChest && eChest.isToggled()) {
                    GLUtils.HighlightBlock(te.getPos(), rgb, true);
                }
            }
        }
    }
}
