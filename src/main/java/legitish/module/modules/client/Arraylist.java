package legitish.module.modules.client;

import legitish.events.Subscribe;
import legitish.events.impl.RenderGameOverlayEvent;
import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Arraylist extends Module {
    // will be improving this later, already looks good
    private static final MinecraftFontRenderer mfr = FontUtils.regular20;
    private static final List<AnimationUtils> animationsX = new ArrayList<>();
    public static ModuleDesc desc;
    public static ModuleTickSetting editPosition, watermark, rectangles, shadow, background, alphabeticalSort;
    public static ModuleSliderSetting margin;
    public static MouseUtils.PositionMode positionMode;
    private static int hudX = 5;
    private static int hudY = 90;

    public Arraylist() {
        super("Arraylist", category.Client, 0);
        this.registerSetting(desc = new ModuleDesc("Displays enabled modules."));
        this.registerSetting(watermark = new ModuleTickSetting("Watermark", true));
        this.registerSetting(shadow = new ModuleTickSetting("Text shadow", true));
        this.registerSetting(rectangles = new ModuleTickSetting("Rectangles", false));
        this.registerSetting(background = new ModuleTickSetting("Background", false));
        this.registerSetting(alphabeticalSort = new ModuleTickSetting("Alphabetical sort", false));
        this.registerSetting(margin = new ModuleSliderSetting("Margin", 4, 1, 10, 1));
    }

    public static int getHudX() {
        return hudX;
    }

    public static void setHudX(int hudX) {
        Arraylist.hudX = hudX;
    }

    public static int getHudY() {
        return hudY;
    }

    public static void setHudY(int hudY) {
        Arraylist.hudY = hudY;
    }

    public void onEnable() {
        ModuleManager.sort();
    }

    public void guiButtonToggled(ModuleTickSetting b) {
        if (b == editPosition) {
            editPosition.setEnabled(false);
            mc.displayGuiScreen(new EditHUD());
        } else if (b == alphabeticalSort) {
            ModuleManager.sort();
        }

    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderGameOverlayEvent.class)
    public void renderArraylist(RenderGameOverlayEvent event) {
        if (GameUtils.isPlayerInGame()) {
            if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
                return;
            }

            double y = hudY;
            ModuleManager.sort();

            List<Module> enabledModuleList = new ArrayList<>(Legitish.moduleManager.enabledModuleList());
            if (enabledModuleList.isEmpty()) {
                return;
            }

            if (hudX < 0) {
                hudX = 2;
            }
            if (hudY < 0) {
                {
                    hudY = 2;
                }
            }
            if (watermark.isToggled()) {
                FontUtils.regular_bold30.drawString("LEGITISH", hudX, hudY, MinecraftFontRenderer.CenterMode.NONE, shadow.isToggled(), ColorUtils.getFontColor(1).getRGB());
                y += FontUtils.regular_bold30.getHeight() + margin.getInput();
            }
            for (Module m : enabledModuleList) {
                if (m.isEnabled() && m != this && m.moduleCategory() != Module.category.Client) {
                    if (Arraylist.positionMode == MouseUtils.PositionMode.DOWNRIGHT || Arraylist.positionMode == MouseUtils.PositionMode.UPRIGHT) {
                        mfr.drawString(m.getName(), hudX + (Legitish.moduleManager.getLongestActiveModule(mfr) - mfr.getStringWidth(m.getName())), y, MinecraftFontRenderer.CenterMode.NONE, shadow.isToggled(), ColorUtils.getFontColor(1).getRGB());
                        y += mc.fontRendererObj.FONT_HEIGHT + margin.getInput();
                    } else {
                        if (background.isToggled()) {
                            RRectUtils.drawRound(hudX, y, mfr.getStringWidth(m.getName()) + 2, mfr.getHeight() + margin.getInput() - 1.4, 0, new Color(0, 0, 0, 90));
                        }
                        if (rectangles.isToggled()) {
                            RRectUtils.drawRound(hudX - 0.2, y, 0.75, mfr.getHeight() + margin.getInput() - 1.4, 0, ColorUtils.getBackgroundColor(3));
                        }
                        mfr.drawString(m.getName(), hudX + 2, y + margin.getInput() / 2, MinecraftFontRenderer.CenterMode.NONE, shadow.isToggled(), ColorUtils.getFontColor(1).getRGB());
                        y += mfr.getHeight() + margin.getInput();
                    }
                }
            }
        }
    }

    public static class EditHUD extends GuiScreen {
        final String a = "This is an-Example-HUD";
        GuiButton resetPosButton;
        boolean d = false;
        int miX = 0;
        int miY = 0;
        int maX = 0;
        int maY = 0;
        int aX = 5;
        int aY = 70;
        int laX = 0;
        int laY = 0;
        int lmX = 0;
        int lmY = 0;

        public void initGui() {
            super.initGui();
            this.buttonList.add(this.resetPosButton = new GuiButton(1, this.width - 90, 5, 85, 20, "Reset position"));
            this.aX = Arraylist.hudX;
            this.aY = Arraylist.hudY;
        }

        public void drawScreen(int mX, int mY, float pt) {
            drawRect(0, 0, this.width, this.height, -1308622848);
            int miX = this.aX;
            int miY = this.aY;
            int maX = miX + 50;
            int maY = miY + 32;
            this.d(this.mc.fontRendererObj, this.a);
            this.miX = miX;
            this.miY = miY;
            this.maX = maX;
            this.maY = maY;
            Arraylist.hudX = miX;
            Arraylist.hudY = miY;
            ScaledResolution res = new ScaledResolution(this.mc);
            int x = res.getScaledWidth() / 2 - 84;
            int y = res.getScaledHeight() / 2 - 20;
            //GLUtils.dct("Edit the HUD position by dragging.", '-', x, y, 2L, 0L, true, this.mc.fontRendererObj);

            this.handleInput();
            super.drawScreen(mX, mY, pt);
        }

        private void d(FontRenderer fr, String t) {
            int x = this.miX;
            int y = this.miY;
            String[] var5 = t.split("-");

            for (String s : var5) {
                fr.drawString(s, (float) x, (float) y, Color.white.getRGB(), Arraylist.shadow.isToggled());
                y += fr.FONT_HEIGHT + 2;
            }
        }

        protected void mouseClickMove(int mX, int mY, int b, long t) {
            super.mouseClickMove(mX, mY, b, t);
            if (b == 0) {
                if (this.d) {
                    this.aX = this.laX + (mX - this.lmX);
                    this.aY = this.laY + (mY - this.lmY);
                } else if (mX > this.miX && mX < this.maX && mY > this.miY && mY < this.maY) {
                    this.d = true;
                    this.lmX = mX;
                    this.lmY = mY;
                    this.laX = this.aX;
                    this.laY = this.aY;
                }
            }
        }

        protected void mouseReleased(int mX, int mY, int s) {
            super.mouseReleased(mX, mY, s);
            if (s == 0) {
                this.d = false;
            }

        }

        public void actionPerformed(GuiButton b) {
            if (b == this.resetPosButton) {
                this.aX = Arraylist.hudX = 5;
                this.aY = Arraylist.hudY = 70;
            }

        }

        @Override
        public void keyTyped(char typedChar, int keyCode) {
            if (keyCode == 1) {
                mc.displayGuiScreen(Legitish.clickGui);
                Legitish.clientConfig.saveConfig();
            }
        }

        public boolean doesGuiPauseGame() {
            return false;
        }
    }
}
