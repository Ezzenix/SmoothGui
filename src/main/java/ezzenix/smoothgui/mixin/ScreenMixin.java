package ezzenix.smoothgui.mixin;

import ezzenix.smoothgui.SmoothGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method="render", at=@At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen) return;
        if (MinecraftClient.getInstance().currentScreen instanceof BeaconScreen) return;
        SmoothGui.push(context);
    }

    @Inject(method="render", at=@At("TAIL"))
    private void onRenderEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen) return;
        if (MinecraftClient.getInstance().currentScreen instanceof BeaconScreen) return;
        SmoothGui.pop(context);
    }
}