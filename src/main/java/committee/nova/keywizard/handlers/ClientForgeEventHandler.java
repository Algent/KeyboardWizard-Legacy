package committee.nova.keywizard.handlers;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;

import committee.nova.keywizard.config.KeyWizardConfig;
import committee.nova.keywizard.gui.GuiKeyWizard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandler {

    // Vanilla GuiControls button ids
    private static final int VANILLA_DONE_ID = 200;
    private static final int VANILLA_RESET_ALL_ID = 201;
    // Controlling GuiNewControls button ids
    private static final int CONTROLLING_DONE_ID = 1001;
    private static final int CONTROLLING_RESET_ALL_ID = 1002;

    private static final int OPEN_KEYWIZARD_ID = 203;

    @SubscribeEvent
    public void controlsGuiInit(InitGuiEvent.Post e) {
        if (KeyWizardConfig.canOpenFromControlsGui() && e.gui instanceof GuiControls) {
            int width = e.gui.width;
            int buttonY = 0;
            for (Object b : e.buttonList) {
                if (!(b instanceof GuiButton)) continue;
                final GuiButton button = (GuiButton) b;
                if (button.id == VANILLA_DONE_ID || button.id == CONTROLLING_DONE_ID) {
                    button.width = 100;
                    button.xPosition = width / 2 + 60;
                    buttonY = button.yPosition;
                }
                if (button.id == VANILLA_RESET_ALL_ID || button.id == CONTROLLING_RESET_ALL_ID) {
                    button.width = 100;
                    button.xPosition = width / 2 - 160;
                }
            }
            e.buttonList
                .add(new GuiButton(OPEN_KEYWIZARD_ID, width / 2 - 50, buttonY, 100, 20, I18n.format("gui.openKeyWiz")));
        }
    }

    @SubscribeEvent
    public void controlsGuiActionPreformed(ActionPerformedEvent.Post e) {
        if (KeyWizardConfig.canOpenFromControlsGui() && e.gui instanceof GuiControls
            && e.button.id == OPEN_KEYWIZARD_ID) {
            FMLClientHandler.instance()
                .getClient()
                .displayGuiScreen(
                    new GuiKeyWizard(
                        FMLClientHandler.instance()
                            .getClient(),
                        e.gui));
        }
    }
}
