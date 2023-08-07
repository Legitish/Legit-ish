package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

import java.awt.*;
import java.util.Iterator;

public class ChestESP extends Module {
    public static ModuleDesc desc;

    public ChestESP() {
        super("Chest ESP", category.Visual, 0);
        this.registerSetting(desc = new ModuleDesc("Renders an overlay over chests."));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventType = RenderWorldEvent.class)
    public void renderESP(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = (new Color(255, 255, 255)).getRGB();
            Iterator<TileEntity> var3 = mc.theWorld.loadedTileEntityList.iterator();

            while (true) {
                TileEntity tileEntity;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    tileEntity = var3.next();
                } while (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest));

                GLUtils.HighlightBlock(tileEntity.getPos(), rgb, true);
            }
        }
    }
}
