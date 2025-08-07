package ezzenix.smoothgui.mixin;

import ezzenix.smoothgui.SmoothGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method="render", at=@At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SmoothGui.push(context);
    }
    @Inject(method="render", at=@At("TAIL"))
    private void onRenderEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SmoothGui.pop(context);
    }

    @Inject(method = "renderBackground", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;renderInGameBackground(Lnet/minecraft/client/gui/DrawContext;)V",
        shift = At.Shift.AFTER
    ))
    private void onRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SmoothGui.push(context);
    }
    @Inject(method="renderBackground", at=@At("TAIL"))
    private void onRenderBackgroundEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SmoothGui.pop(context);
    }
}