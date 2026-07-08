package committee.nova.keywizard.gui;

import static org.lwjgl.input.Keyboard.getKeyName;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.blamejared.controlling.keybinding.ComboKeyBinding;
import committee.nova.keywizard.util.GuiUtils;
import committee.nova.keywizard.util.KeyHelper;
import committee.nova.keywizard.util.KeybindUtils;

public class GuiKeyboard extends FloatGui {

    public double anchorX;
    public double anchorY;
    public GuiKeyWizard parent;

    protected HashMap<Integer, GuiKeyboardKey> keyList = new HashMap<>();

    public GuiKeyboard(GuiKeyWizard parent, double anchorX, double anchorY) {
        this.parent = parent;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        for (GuiKeyboardKey k : this.keyList.values()) {
            k.drawKey(mc, mouseX, mouseY);
        }
        for (GuiKeyboardKey k : this.keyList.values()) {
            if (k.hovered && !parent.getCategoryListExtended()) {
                GuiUtils.drawHoveringText(
                    KeybindUtils.getBindingNamesAndCategories(k.keyCode, this.parent.getActiveModifier()),
                    mouseX,
                    mouseY,
                    this.parent.width,
                    this.parent.height,
                    -1,
                    this.parent.getFontRenderer());

                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
            }
        }
    }

    public void mouseClicked(Minecraft mc, int mouseX, int mouseY, int button) {
        for (GuiKeyboardKey k : this.keyList.values()) {
            k.mouseClicked(mc, mouseX, mouseY, button);
        }
    }

    public void addKey(double xIn, double yIn, double width, double height, int keyCode) {
        this.keyList.put(keyCode, new GuiKeyboardKey(this, xIn, yIn, width, height, keyCode));
    }

    public void disableKey(int keyCode) {
        if (this.HasKey(keyCode)) this.keyList.get(keyCode).enabled = false;
    }

    public void enableKey(int keyCode) {
        if (this.HasKey(keyCode)) this.keyList.get(keyCode).enabled = true;
    }

    public boolean HasKey(int keyCode) {
        return this.keyList.containsKey(keyCode);
    }

    private class GuiKeyboardKey extends FloatGui {

        public GuiKeyboard keyboard;
        public double x;
        public double y;
        public double width;
        public double height;
        public boolean enabled = true;

        public int keyCode;
        public String displayString;

        protected boolean hovered;

        public GuiKeyboardKey(GuiKeyboard keyboard, double x, double y, double width, double height, int keyCode) {
            this.keyboard = keyboard;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.keyCode = keyCode;
            this.displayString = KeyHelper.translateKey(this.keyCode);
        }

        public void drawKey(Minecraft mc, double mouseX, double mouseY) {
            this.hovered = mouseX >= this.absX() && mouseY >= this.absY()
                && mouseX < this.absX() + this.width
                && mouseY < this.absY() + this.height;
            int modifiedBindings = KeybindUtils.getNumBindings(this.keyCode, parent.getActiveModifier());
            // int unmodifiedBindings = KeybindUtils.getNumBindings(this.keyCode, KeyModifier.NONE);
            int color;
            if (this.enabled) {
                if (this.hovered && !parent.getCategoryListExtended()) {
                    color = 0xFFAAAAAA;
                    if (modifiedBindings == 1) {
                        color = 0xFF00AA00;
                    } else if (modifiedBindings > 1) {
                        color = 0xFFAA0000;
                    }
                } else {
                    color = 0xFFFFFFFF;
                    if (modifiedBindings == 1) {
                        color = 0xFF00FF00;
                    } else if (modifiedBindings > 1) {
                        color = 0xFFFF0000;
                    }
                }
            } else {
                color = 0xFF555555;
            }

            drawNoFillRect(this.absX(), this.absY(), this.absX() + this.width, this.absY() + this.height, color);
            drawCenteredString(
                this.keyboard.parent.getFontRenderer(),
                this.displayString,
                (int) (this.absX() + (this.width + 2) / 2.0F),
                (int) (this.absY() + (this.height - 6) / 2.0F),
                color & 0x00FFFFFF);
        }

        public void mouseClicked(Minecraft mc, int mouseX, int mouseY, int button) {
            if (this.hovered && this.enabled && !parent.getCategoryListExtended() && button == 0) {
                mc.getSoundHandler()
                    .playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                if (GuiScreen.isShiftKeyDown()) {
                    parent.setSearchText("@" + getKeyName(this.keyCode));
                } else {
                    ((ComboKeyBinding) parent.getSelectedKeybind())
                        .controlling$setKeyModifierAndCode(parent.getActiveModifier(), this.keyCode);
                    mc.gameSettings.setOptionKeyBinding(parent.getSelectedKeybind(), this.keyCode);
                    KeyBinding.resetKeyBindingArrayAndHash();
                }
            }
        }

        public double absX() {
            return this.keyboard.anchorX + this.x;
        }

        public double absY() {
            return this.keyboard.anchorY + this.y;
        }
    }

}
