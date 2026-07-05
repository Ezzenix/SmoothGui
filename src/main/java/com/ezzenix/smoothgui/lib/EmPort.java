package com.ezzenix.smoothgui.lib;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

//~ if >1.20.1 'TextAndImageButton' -> 'SpriteIconButton'
import net.minecraft.client.gui.components.SpriteIconButton;

public class EmPort {

	//~ if >1.20.1 'TextAndImageButton' -> 'SpriteIconButton'
	public static SpriteIconButton createIconButton(Component message, Button.OnPress onPress, Identifier sprite, int spriteSize, int size) {
		//? if >1.20.1 {
		SpriteIconButton button = SpriteIconButton.builder(message,
		//?} else {
		/*TextAndImageButton button = TextAndImageButton.builder(message, sprite,
		 *///?}
		onPress
		//? if >1.20.1 {
		, true).sprite(sprite, spriteSize, spriteSize).size(size, size).build();
		//?} else {
		/*).textureSize(spriteSize, spriteSize).usedTextureSize(spriteSize, spriteSize).offset(0, 4).build();
		button.setWidth(size);
		*///?}
		return button;
	}

}
