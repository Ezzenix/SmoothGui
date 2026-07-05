package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@ModifyExpressionValue(
		//~ if >=1.21.9 'onPress' -> 'onButton'
		method = "onButton",
		at = @At(
			//~ if >=1.21.5 'FIELD' -> 'INVOKE'
			value = "INVOKE",
			//? if >=1.21.5
			target = "Lnet/minecraft/client/MouseHandler;getScaledYPos(Lcom/mojang/blaze3d/platform/Window;)D"
			//? if <1.21.5
			//target = "Lnet/minecraft/client/MouseHandler;ypos:D"
		)
	)
	private double modifyY(double original) {
		//? if >=1.21.5 {
		return original + (int)SmoothGui.displacement;
		//? } else {
		/*Window window = Minecraft.getInstance().getWindow();
		double scaleFactor = (double) window.getScreenHeight() / (double) window.getGuiScaledHeight();
		return original + (SmoothGui.displacement * scaleFactor);
		*///? }
	}

}
