package io.github.pumpkinxd.linuxlegacymcurlfix.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URI;

import org.apache.commons.lang3.SystemUtils;

@SuppressWarnings("unused")
@Mixin(GuiScreen.class)
public class mixinGuiScreen_openURLFix {

@Inject(method="openWebLink",at=@At(value="HEAD"),cancellable = true)
public void FixopenWebLink(URI url,CallbackInfo ci){
    boolean flag=false;
    try{
            //Class<?> oclass_java_awt_Desktop = Class.forName("java.awt.Desktop");
            //Object oinstance_java_awt_Desktop=oclass_java_awt_Desktop.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
        if(SystemUtils.IS_OS_UNIX&&(!SystemUtils.IS_OS_MAC)){
            //int status_code=Runtime.getRuntime().exec("xdg-open "+url.toString()).waitFor();
            int status_code=Runtime.getRuntime().exec(new String[]{"xdg-open",url.toString()}).waitFor();
            if(status_code!=0)
                throw new RuntimeException("Failed to open URL \""+url.toString()+"\"via xdg-open with code"+status_code+"!");
        }
        else
        {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {url});
        }
    }
    catch (Throwable throwable)
    {
            final Logger mixinLOGGER = LogManager.getLogger();
            mixinLOGGER.error("Couldn\'t open link", throwable);
            flag=true;
    }
    if (flag)
    {
        LogManager.getLogger().info("Opening via system class!");
        org.lwjgl.Sys.openURL("HTTPS://" + url.toString());
    }
    ci.cancel();
}
}
