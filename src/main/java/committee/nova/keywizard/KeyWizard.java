package committee.nova.keywizard;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import committee.nova.keywizard.config.KeyWizardConfig;
import committee.nova.keywizard.handlers.ClientFMLEventHandler;
import committee.nova.keywizard.handlers.ClientForgeEventHandler;
import committee.nova.keywizard.key.KeyInit;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(
    modid = KeyWizard.MODID,
    version = Tags.VERSION,
    useMetadata = true,
    dependencies = "required-after:controlling@[2.1.7,)")
public class KeyWizard {

    public static final String MODID = "keywizard";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static KeyWizard instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (e.getSide() != Side.CLIENT) return;
        LOGGER.log(Level.INFO, "Let's do some keyboard magic!");
        KeyWizardConfig.init(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        if (e.getSide() != Side.CLIENT) return;
        KeyInit.init();
        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new ClientFMLEventHandler());
    }
}
