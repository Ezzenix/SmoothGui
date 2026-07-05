package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
	//? if >=26.2 {
	@Shadow
	private Screen screen;

	@Inject(
		method = "setScreen",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = 181)
	)
	private void onSetScreen(Screen screen, CallbackInfo ci) {
		SmoothGui.onScreenChanged(this.screen, screen);
	}
	//? }
}
