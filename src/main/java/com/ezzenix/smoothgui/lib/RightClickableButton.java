package com.ezzenix.smoothgui.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;

//? if >=1.21.9
import net.minecraft.client.input.MouseButtonEvent;

//~ if >=1.21.11 ' Button ' -> ' Button.Plain '
public class RightClickableButton extends Button.Plain {
	private final BiConsumer<Button, Boolean> onPress;

	public RightClickableButton(int x, int y, int width, int height, Component message, BiConsumer<Button, Boolean> onPress) {
		super(x, y, width, height, message, (b) -> {}, DEFAULT_NARRATION);
		this.onPress = onPress;
	}

	@Override
	//? if >=1.21.9 {
	public boolean mouseClicked(MouseButtonEvent event, boolean wasDouble) {
		double x = event.x();
		double y = event.y();
		int button = event.button();
	//? } else {
	/*public boolean mouseClicked(double x, double y, int button) {
	*///? }
		if (this.active && this.visible) {
			if (this.isMouseOver(x, y)) {
				if (button == 0 || button == 1) {
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					this.onPress.accept(this, button == 1);
					return true;
				}
			}
		}
		//~ if >=1.21.9 'x, y, button' -> 'event, wasDouble'
		return super.mouseClicked(event, wasDouble);
	}
}
