package io.github.pumpkinxd.linuxlegacymcurlfix.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import org.apache.commons.lang3.SystemUtils;


@Mixin(GuiScreenResourcePacks.class)
public class mixinGuiScreenResourcePacks_openFolderFix {
@Inject(
        method = "actionPerformed",
        at=@At(
                value = "INVOKE",
                target = "getAbsolutePath",
                ordinal = 0,
                remap = false
        ),
        cancellable = true
)
public void fixOpenResFolder(CallbackInfo ci){
        ResourcePackRepository repository = Minecraft.getMinecraft().getResourcePackRepository();
        String repopath= repository.getDirResourcepacks().getAbsolutePath();
        final Logger logger = LogManager.getLogger();
        boolean flag = false;
        if (SystemUtils.IS_OS_UNIX) {
                if (!SystemUtils.IS_OS_MAC) {
                        try
                        {
                                logger.info(repopath);
                                Runtime.getRuntime().exec(new String[] {"/usr/bin/xdg-open", repopath});
                                ci.cancel();
                        }
                        catch (IOException ioexception1)
                        {
                                logger.error((String)"Couldn\'t open file via xdg-open", (Throwable)ioexception1);
                                flag = true;
                        }
                } else {
                        try
                        {
                                logger.info(repopath);
                                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", repopath});
                                ci.cancel();
                        }
                        catch (IOException ioexception1)
                        {
                                logger.error((String)"Couldn\'t open file", (Throwable)ioexception1);
                                flag = true;
                        }
                }
                
        } else {
                if (SystemUtils.IS_OS_WINDOWS) {
                        String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {repopath});

                        try
                        {
                                Runtime.getRuntime().exec(s1);
                                ci.cancel();
                        }
                        catch (IOException ioexception)
                        {
                                logger.error((String)"Couldn\'t open file", (Throwable)ioexception);
                                flag = true;
                        }
                } else {
                        try
                        {
                                Class<?> oclass = Class.forName("java.awt.Desktop");
                                Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                                oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {repository.getDirResourcepacks().toURI()});
                        }
                        catch (Throwable throwable)
                        {
                                logger.error("Couldn\'t open link", throwable);
                                flag = true;
                        }
                }
                
        }
        if (flag)
        {
                logger.info("Opening via system class!");
                Sys.openURL("file://" + repopath);
        }


        ci.cancel();
}
}









