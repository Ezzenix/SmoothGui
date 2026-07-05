package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	//? if <26.2 {
	/*@Shadow
	public Screen screen;

	@Inject(
		method = "setScreen",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = 181)
	)
	private void onSetScreen(Screen screen, CallbackInfo ci) {
		SmoothGui.onScreenChanged(this.screen, screen);
	}
	*///? }
}
