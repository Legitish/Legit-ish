package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.modules.client.SelfDestruct;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.render.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.Iterator;

public class ChestESP extends Module {
    public static ModuleSliderSetting a, b, c;
    public static ModuleTickSetting d;

    public ChestESP() {
        super("Chest ESP", Module.category.Visual, 0);
        a = new ModuleSliderSetting("Red", 0.0D, 0.0D, 255.0D, 1.0D);
        b = new ModuleSliderSetting("Green", 0.0D, 0.0D, 255.0D, 1.0D);
        c = new ModuleSliderSetting("Blue", 255.0D, 0.0D, 255.0D, 1.0D);
        d = new ModuleTickSetting("Rainbow", false);
        this.registerSetting(a);
        this.registerSetting(b);
        this.registerSetting(c);
        this.registerSetting(d);
    }

    @SubscribeEvent
    public void o(RenderWorldEvent ev) {
        if (GameUtils.isPlayerInGame() && !SelfDestruct.destructed) {
            int rgb = d.isToggled() ? MouseUtils.gc(2L, 0L) : (new Color((int) a.getInput(), (int) b.getInput(), (int) c.getInput())).getRGB();
            Iterator<TileEntity> var3 = mc.theWorld.loadedTileEntityList.iterator();

            while (true) {
                TileEntity te;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    te = var3.next();
                } while (!(te instanceof TileEntityChest) && !(te instanceof TileEntityEnderChest));

                RenderUtils.HighlightBlock(te.getPos(), rgb, true);
            }
        }
    }
}
