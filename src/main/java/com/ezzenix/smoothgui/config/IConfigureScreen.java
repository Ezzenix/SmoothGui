package com.ezzenix.smoothgui.config;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public interface IConfigureScreen {
	void smoothGui$init();
	void smoothGui$extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

	static IConfigureScreen of(Screen screen) {
		return (IConfigureScreen)((Object)screen);
	}
}
