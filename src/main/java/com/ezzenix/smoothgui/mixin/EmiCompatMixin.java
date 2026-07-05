package com.ezzenix.smoothgui.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "dev.emi.emi.screen.StackBatcher", remap = false)
public class EmiCompatMixin {
	@ModifyReturnValue(method="isEnabled", at = @At("RETURN"))
	private static boolean isEnabled(boolean original) {
		return false;
	}
}
