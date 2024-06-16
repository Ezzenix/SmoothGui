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

    DrawContext drawContext;

    // Make background not affected
    @Inject(method="renderBackground", at=@At("HEAD"))
    private void onRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        drawContext = context;

        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, -SmoothGui.getOffsetY(), 0.0);
    }
    @Inject(method="renderBackground", at=@At(value="INVOKE",
            target="Lnet/minecraft/client/gui/screen/ingame/HandledScreen;renderInGameBackground(Lnet/minecraft/client/gui/DrawContext;)V",
            shift=At.Shift.AFTER))
    private void afterRenderInGameBackground(CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        drawContext.getMatrices().translate(0.0, SmoothGui.getOffsetY(), 0.0);
    }
    @Inject(method="renderBackground", at=@At("TAIL"))
    private void onRenderBackgroundEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, SmoothGui.getOffsetY(), 0.0);
    }
}