package com.ezzenix.smoothgui.config;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public interface IConfigureScreen {
	void smoothgui$init();
	void smoothgui$extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

	Button smoothgui$getButton();

	static IConfigureScreen of(Screen screen) {
		return (IConfigureScreen) screen;
	}
}
