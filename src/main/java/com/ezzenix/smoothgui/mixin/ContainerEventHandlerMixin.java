package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.config.IConfigureScreen;
import com.ezzenix.smoothgui.config.ModConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerMixin {

	/*
		Force config button to be above anything else
	*/

	@Inject(method="getChildAt", at=@At("HEAD"), cancellable=true)
	default void smoothgui$getChildAt(double x, double y, CallbackInfoReturnable<Optional<GuiEventListener>> cir) {
		if (ModConfig.configMode && this instanceof Screen screen) {
			Button configButton = IConfigureScreen.of(screen).smoothgui$getButton();
			if (configButton != null && configButton.isActive() && configButton.isMouseOver(x, y)) {
				cir.setReturnValue(Optional.of(configButton));
			}
		}
	}

	//? if <=1.21.3 {
	/*@Inject(method="mouseClicked", at=@At("HEAD"), cancellable=true)
	default void smoothgui$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.configMode && this instanceof Screen screen) {
			Button configButton = IConfigureScreen.of(screen).smoothgui$getButton();
			if (configButton != null && configButton.isActive() && configButton.isMouseOver(x, y)) {
				configButton.mouseClicked(x, y, button);
				cir.setReturnValue(true);
			}
		}
	}
	*///? }

}
