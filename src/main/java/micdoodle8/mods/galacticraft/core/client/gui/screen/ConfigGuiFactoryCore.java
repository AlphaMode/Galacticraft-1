//package micdoodle8.mods.galacticraft.core.client.gui.screen;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraftforge.fml.client.IModGuiFactory;
//import net.minecraftforge.fml.client.config.GuiConfig;
//
//import java.util.Set;
//
//public class ConfigGuiFactoryCore implements IModGuiFactory
//{
//    public static class CoreConfigGUI extends GuiConfig
//    {
//        public CoreConfigGUI(Screen parent)
//        {
//            super(parent, ConfigManagerCore.getConfigElements.get()(), Constants.MOD_ID_CORE, false, false, GCCoreUtil.translate("gc.configgui.title"));
//        }
//    }
//
//    @Override
//    public void initialize(Minecraft minecraftInstance)
//    {
//
//    }
//
//    @Override
//    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
//    {
//        return null;
//    }
//
//	public Screen createConfigGui(Screen arg0)
//	{
//		// TODO  Forge 2282 addition!
//		return new CoreConfigGUI(arg0);
//	}
//
//	public boolean hasConfigGui()
//	{
//		return true;
//	}
//} TODO Config GUI
