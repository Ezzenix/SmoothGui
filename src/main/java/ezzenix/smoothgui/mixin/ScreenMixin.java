package ezzenix.smoothgui.mixin;

import ezzenix.smoothgui.SmoothGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class ScreenMixin {
    // Offset screen rendering
    @Inject(method="render", at=@At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, SmoothGui.getOffsetY(), 0.0);
    }
    @Inject(method="render", at=@At("TAIL"))
    private void onRenderEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, -SmoothGui.getOffsetY(), 0.0);
    }

    // Make background not affected
    @Inject(method="renderBackground", at=@At("HEAD"))
    private void onRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, -SmoothGui.getOffsetY(), 0.0);
    }
    @Inject(method="renderBackground", at=@At("TAIL"))
    private void onRenderBackgroundEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        context.getMatrices().translate(0.0, SmoothGui.getOffsetY(), 0.0);
    }

    // Track when new screens are opened
    @Inject(method="onDisplayed", at=@At("HEAD"))
    private void onDisplayed(CallbackInfo ci) {
        SmoothGui.lastGuiOpenedTime = System.currentTimeMillis();
    }
}