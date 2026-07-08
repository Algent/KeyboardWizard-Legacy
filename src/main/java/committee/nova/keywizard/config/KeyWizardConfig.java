package committee.nova.keywizard.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class KeyWizardConfig {

    public static Configuration config;
    private static boolean openFromControlsGui;
    private static int maxMouseButtons;

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "KeyboardWizard.cfg"));
        config.load();
        openFromControlsGui = config.getBoolean(
            "openFromControlsGui",
            Configuration.CATEGORY_GENERAL,
            true,
            "If true, keyboard wizard will be accessible through a button or a keybinding in the controls gui. (Default: F8)");
        maxMouseButtons = config.getInt(
            "maxMouseButtons",
            Configuration.CATEGORY_GENERAL,
            5,
            3,
            15,
            "The number of mouse buttons to show (default:5).");
        config.save();
    }

    public static int getMaxMouseButtons() {
        return maxMouseButtons;
    }

    public static boolean canOpenFromControlsGui() {
        return openFromControlsGui;
    }
}
