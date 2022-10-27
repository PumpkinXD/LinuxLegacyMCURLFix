package io.github.sdxqw;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExampleMod.MOD_ID)
public class ExampleMod {

    public static final String MOD_ID = "examplemod";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        System.out.println("test");
    }
}
