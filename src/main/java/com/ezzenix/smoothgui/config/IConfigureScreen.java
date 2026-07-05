package com.ezzenix.smoothgui.config;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public interface IConfigureScreen {
	Button smoothGui$button = null;

	void smoothGui$init();
	void smoothGui$extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

	static IConfigureScreen of(Screen screen) {
		return (IConfigureScreen)((Object)screen);
	}
}
