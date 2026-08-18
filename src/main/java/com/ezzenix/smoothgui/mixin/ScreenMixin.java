package com.ezzenix.smoothgui.mixin;

import com.ezzenix.emlib.util.EmPort;
import com.ezzenix.smoothgui.SmoothGui;
import com.ezzenix.smoothgui.config.EasingStyle;
import com.ezzenix.smoothgui.config.IConfigureScreen;
import com.ezzenix.smoothgui.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >=1.21.6 {
import com.ezzenix.smoothgui.mixin.accessor.GuiGraphicAccessor;
import com.ezzenix.smoothgui.mixin.accessor.GuiRenderStateAccessor;
//? }

@Mixin(Screen.class)
public class ScreenMixin {
	@Shadow protected Minecraft minecraft;

	//? if >1.20.1 {
	@WrapMethod(
		//? if >=26.1
		method = "extractRenderStateWithTooltipAndSubtitles"
		//? if >=1.21.9 && <26.1
		//method = "renderWithTooltipAndSubtitles"
		//? if <1.21.9
		//method = "renderWithTooltip"
	)
	private void wrapScreenRender(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, Operation<Void> original) {
		SmoothGui.partialTick = a;
		SmoothGui.onRender((Screen)(Object)this);
		SmoothGui.wrap(graphics, () -> original.call(graphics, mouseX, mouseY + (int)SmoothGui.displacement, a));
		SmoothGui.onRenderEnd(graphics);
		IConfigureScreen.of((Screen)(Object)this).smoothgui$extractRenderState(graphics, mouseX, mouseY, a);
	}
	//~ if >=26.1 'render' -> 'extract'
	@WrapMethod(method = "extractBackground")
	private void wrapBackgroundRender(GuiGraphicsExtractor graphics, /*? if >=1.20.2 {*/ int mouseX, int mouseY, float a, /*?}*/ Operation<Void> original) {
		SmoothGui.wrapInverse(graphics, () -> original.call(graphics/*? if >=1.20.2 {*/ , mouseX, mouseY, a/*?}*/));
	}
	//~ if >=26.1 'render' -> 'extract'
	@WrapMethod(method = "extractTransparentBackground")
	private void wrapTransparentBackgroundRender(GuiGraphicsExtractor graphics, Operation<Void> original) {
		SmoothGui.wrapInverse(graphics, () -> original.call(graphics));
	}
	//? if >=1.20.5 {
	//~ if >=26.1 'render' -> 'extract'
	@WrapMethod(method = "extractMenuBackgroundTexture")
	private static void wrapMenuBackgroundRender(GuiGraphicsExtractor graphics, Identifier menuBackground, int x, int y, float u, float v, int width, int height, Operation<Void> original) {
		SmoothGui.wrapInverse(graphics, () -> original.call(graphics, menuBackground, x, y, u, v, width, height));
	}
	//? }
	//? } else {
	/*@WrapOperation(method = "renderWithTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"))
	private void wrapScreenRender(Screen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, Operation<Void> original) {
		SmoothGui.partialTick = a;
		SmoothGui.onRender(screen);
		SmoothGui.wrap(graphics, () -> original.call(screen, graphics, mouseX, mouseY + (int)SmoothGui.displacement, a));
		SmoothGui.onRenderEnd(graphics);
		IConfigureScreen.of(screen).smoothgui$extractRenderState(graphics, mouseX, mouseY, a);
	}

	@WrapMethod(method="renderBackground")
	private void wrapBackgroundRender(GuiGraphicsExtractor graphics, Operation<Void> original) {
		SmoothGui.wrapInverse(graphics, () -> original.call(graphics));
	}
	@WrapMethod(method="renderDirtBackground")
	private void wrapDirtBackgroundRender(GuiGraphicsExtractor graphics, Operation<Void> original) {
		SmoothGui.wrapInverse(graphics, () -> original.call(graphics));
	}
	*///? }
	
    /*
   		Background fade
     */

	@Unique
	private static int argb(int a, int r, int g, int b) {
		return ((a & 0xFF) << 24)
			| ((r & 0xFF) << 16)
			| ((g & 0xFF) << 8)
			| (b & 0xFF);
	}

	@Unique
	private float getBackgroundFadeAlpha() {
		if (!SmoothGui.shouldAnimateScreen((Screen)(Object)this)) return 1;
		if (ModConfig.backgroundFadeTime <= 0) return 1;
		float alpha = SmoothGui.getAlphaSince(SmoothGui.lastScreenOpenedTime, ModConfig.backgroundFadeTime);
		return EasingStyle.CUBIC.applyReverse((double)alpha).floatValue();
	}

	@Unique
	private int getBackgroundColor(float alphaMultiplier) {
		float alpha = getBackgroundFadeAlpha();
		int a = (int)(alpha * alphaMultiplier * ModConfig.backgroundOpacity * 255f);
		a = Math.max(0, Math.min(255, a));
		return argb(a, 8, 8, 8);
	}

	//? if >1.20.1 {
    @Inject(
		//? if >=26.1
		method = "extractTransparentBackground",
		//? if <26.1
		//method = "renderTransparentBackground",
		at=@At("HEAD"), cancellable = true
	)
    public void onRenderTransparentBackground(GuiGraphicsExtractor graphics, CallbackInfo ci) {
		if (!ModConfig.modifyBackground) return;
		ci.cancel();

		//? >=1.20.5
		renderBackgroundBlur(graphics);

        if (ModConfig.backgroundOpacity == 0) return;

        int top = getBackgroundColor(0.9f);
        int bottom = getBackgroundColor(1f);

        Screen screen = (Screen)(Object)this;
        graphics.fillGradient(
            0, 0, screen.width, screen.height,
            top, bottom
        );
    }
	//? } else {
	/*@ModifyArg(
		method = "renderBackground",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fillGradient(IIIIII)V"),
		index = 4
	)
	public int renderTransparentBackgroundColorTop(int x) {
		if (!ModConfig.modifyBackground) return x;
		return getBackgroundColor(0.9f);
	}
	@ModifyArg(
		method = "renderBackground",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fillGradient(IIIIII)V"),
		index = 5
	)
	public int renderTransparentBackgroundColorBottom(int x) {
		if (!ModConfig.modifyBackground) return x;
		return getBackgroundColor(1f);
	}
	*///? }

	/*
		Screen blur
	 */

	//? >=1.20.5 {
	//? if >=1.21.6 {
	//~ if >=26.1 'renderBlurredBackground' -> 'extractBlurredBackground'
	@Shadow protected void extractBlurredBackground(GuiGraphicsExtractor graphics) {}
	//?} else if >=1.21.2 {
	/*@Shadow protected void renderBlurredBackground() {};
	 *///?} else {
	/*@Shadow protected void renderBlurredBackground(float a) {};
	*///? }

	@Unique
	private void renderBackgroundBlur(GuiGraphicsExtractor graphics) {
		if (!ModConfig.alwaysBlurBackground || SmoothGui.isScreenFullyBlocked(EmPort.screen())) return;

		//? if >=1.21.6 {
		if(((GuiRenderStateAccessor)((GuiGraphicAccessor)graphics).smoothgui$getGuiRenderState()).smoothgui$getFirstStratumAfterBlur() != Integer.MAX_VALUE) {
			return;
		}
		//~ if >=26.1 'renderBlurredBackground' -> 'extractBlurredBackground'
		this.extractBlurredBackground(graphics);
		//?} else if >=1.21.2 {
		/*this.renderBlurredBackground();
		*///? } else {
		/*this.renderBlurredBackground(SmoothGui.partialTick);
		*///? }
	}
	//? }

}
