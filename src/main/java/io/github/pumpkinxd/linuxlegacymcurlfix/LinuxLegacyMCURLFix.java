package io.github.pumpkinxd.linuxlegacymcurlfix;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LinuxLegacyMCURLFix.MOD_ID,version = LinuxLegacyMCURLFix.VERSION)
public class LinuxLegacyMCURLFix {

    public static final String MOD_ID = "linuxlegacymcurlfix";
    public static final String VERSION = "0.2.2-alpha-0.1";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        //System.out.println("test");
    }
}
