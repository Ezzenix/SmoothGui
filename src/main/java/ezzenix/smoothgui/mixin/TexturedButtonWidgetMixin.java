package ezzenix.smoothgui.mixin;

import ezzenix.smoothgui.SmoothGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TexturedButtonWidget.class)
public class TexturedButtonWidgetMixin {
    @Final
    @Shadow
    protected ButtonTextures textures;

    @Inject(method="renderWidget", at=@At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        //TexturedButtonWidget t = (TexturedButtonWidget)(Object)this;
        if (textures == RecipeBookWidget.BUTTON_TEXTURES) {
            context.getMatrices().translate(0.0, -SmoothGui.getOffsetY(), 0.0);
        }
    }
    @Inject(method="renderWidget", at=@At("TAIL"))
    private void onRenderEnd(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SmoothGui.isInMenu()) return;
        if (textures == RecipeBookWidget.BUTTON_TEXTURES) {
            context.getMatrices().translate(0.0, SmoothGui.getOffsetY(), 0.0);
        }
    }
}