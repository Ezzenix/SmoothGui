//~ !unobfuscate

package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import com.ezzenix.smoothgui.config.EasingStyle;
import com.ezzenix.smoothgui.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Unique
	private static float getBlurRadius(float original) {
		if (!ModConfig.modifyBackground) return original;
		if (ModConfig.backgroundFadeTime == 0) return original;
		float alpha = SmoothGui.getAlphaSince(SmoothGui.lastScreenOpenedTime, ModConfig.backgroundFadeTime);
		alpha = EasingStyle.CUBIC.applyReverse((double) alpha).floatValue();
		return alpha * original;
	}

	//? if >= 26.1 {
	@ModifyExpressionValue(method = "render", at=@At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/OptionsRenderState;menuBackgroundBlurriness:I", opcode = Opcodes.GETFIELD))
	public int modifyBlurRadius(int original) {
		return (int)getBlurRadius(original);
	}
	//? } else if >=1.21.6 {
	/*@ModifyExpressionValue(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getMenuBackgroundBlurriness()I"))
	public int modifyBlurRadius(int original) {
		return (int)getBlurRadius(original);
	}
	*///? } else if >=1.21 {
	/*@ModifyExpressionValue(method = "processBlurEffect", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getMenuBackgroundBlurriness()I"))
	public int modifyBlurRadius(int original) {
		return (int)getBlurRadius(original);
	}
	*///? } else if >=1.20.5 {
	/*@ModifyExpressionValue(method = "processBlurEffect", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getMenuBackgroundBlurriness()D"))
	public double modifyBlurRadius(double original) {
		return getBlurRadius((float) original);
	}
	*///? }

}
