package io.github.pumpkinxd.linuxlegacymcurlfix.mixins.bugfixes.optifine;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;

import org.lwjgl.Sys;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@SuppressWarnings("unused")
@Pseudo
@Mixin(targets = "net.optifine.shaders.gui.GuiShaders")
public class mixinGuiShaders_openURLFix {

    @Dynamic("OptiFine")
    @Inject(method = "actionPerformed",
    at=@At(
            value = "INVOKE",
            target = "getOSType",
            ordinal = 0,
            shift = At.Shift.AFTER,
            remap = false
            ),
            cancellable = true
    )
    public void fixOpenShaderFolder(CallbackInfo ci) {
        final File shaderpackDIR=new File(Minecraft.getMinecraft().mcDataDir,"shaderpacks");
        boolean flag=false;

        if(SystemUtils.IS_OS_UNIX){
            if(!SystemUtils.IS_OS_MAC){

                try {
                    Runtime.getRuntime().exec(new String[] {"xdg-open", shaderpackDIR.getAbsolutePath()});//requires xdg-util or won't working
                } catch (IOException e) {

                    flag=true;
                }

            }else{
                try {
                    Runtime.getRuntime().exec(new String[] {"/usr/bin/open", shaderpackDIR.getAbsolutePath()});
                } catch (IOException e) {

                    flag=true;
                }
            }
        }
        else {
            if (SystemUtils.IS_OS_WINDOWS){
                try {
                    Runtime.getRuntime().exec(new String[]{"cmd","/C","start","\"Open file\"",shaderpackDIR.getAbsolutePath()});
                } catch (IOException e) {

                    flag=true;
                }
            }else{
                try{
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {shaderpackDIR.toURI()});
                } catch (Throwable e){
                    LogManager.getLogger().error("Couldn\'t open Folder via java.awt.Desktop", e);
                    flag = true;
                }
            }
        }

        if (flag){
            LogManager.getLogger().info("Opening via system class!");
            Sys.openURL("file://" + shaderpackDIR.getAbsolutePath());
        }
        ci.cancel();

    }


    @Dynamic("OptiFine")
    @Inject(method = "actionPerformed",
            at=@At(
                    value = "INVOKE",
                    target ="forName",            //"java/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;",
                    ordinal = 1,
                    //shift=At.Shift.BY,
                    //by=3,
                    remap = false
            ),
            cancellable = true

    )
    public void fixOpenOptifineLink(CallbackInfo ci)
    {

       final String OFLink="https://optifine.net/shaderPacks";
       boolean flag=false;
       try {


           if (SystemUtils.IS_OS_UNIX && (!SystemUtils.IS_OS_MAC)) {
               LogManager.getLogger().info("Opening via xdg-open!");
               Runtime.getRuntime().exec(new String[] {"xdg-open", OFLink});
           }else {
               LogManager.getLogger().info("Opening via java.awt.Desktop class!");
               Class<?> oclass = Class.forName("java.awt.Desktop");
               Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
               oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(OFLink)});
           }
       }catch (Throwable e){

           flag=true;
       }

       if(flag){
           LogManager.getLogger().info("Opening via system class!");
           org.lwjgl.Sys.openURL("HTTPS://" + OFLink);
       }

       ci.cancel();
    }




}
