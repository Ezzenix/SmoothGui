package com.ezzenix.smoothgui.lib;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

//? if >1.20.1
import net.minecraft.client.gui.components.WidgetSprites;

public class IconButton extends AbstractWidget {
	//~ if >1.20.1 'TextAndImageButton' -> 'SpriteIconButton'
	SpriteIconButton instance;

	//~ if >1.20.1 'TextAndImageButton' -> 'SpriteIconButton'
	private IconButton(SpriteIconButton instance, int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
		this.instance = instance;
	}

	public void setPosition(int x, int y) {
		super.setX(x);
		super.setY(y);
	}

	public static IconButton.Builder builder(Component message, Button.OnPress onPress) {
		return new IconButton.Builder(message, onPress);
	}

	@Override
	//~ if >=26.1 'renderWidget' -> 'extractWidgetRenderState'
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		//~ if >1.20.1 'renderWidget' -> 'render'
		instance.extractRenderState(graphics, mouseX, mouseY, a);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		this.defaultButtonNarrationText(narrationElementOutput);
	}

	public static class Builder {
		private final Component message;
		private final Button.OnPress onPress;
		private int x = 0;
		private int y = 0;
		private int width = 150;
		private int height = 20;
		private Identifier spriteId;
		private int spriteWidth;
		private int spriteHeight;
		private int spriteOffsetX = 0;
		private int spriteOffsetY = 0;
		private Component tooltip;

		private Builder(Component message, Button.OnPress onPress) {
			this.message = message;
			this.onPress = onPress;
		}

		public IconButton.Builder width(final int width) {
			this.width = width;
			return this;
		}

		public IconButton.Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public IconButton.Builder position(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public IconButton.Builder sprite(Identifier spriteId, int spriteWidth, int spriteHeight) {
			this.spriteId = spriteId;
			this.spriteWidth = spriteWidth;
			this.spriteHeight = spriteHeight;
			return this;
		}

		public IconButton.Builder spriteOffset(int spriteOffsetX, int spriteOffsetY) {
			this.spriteOffsetX = spriteOffsetX;
			this.spriteOffsetY = spriteOffsetY;
			return this;
		}

		public IconButton.Builder tooltip(Component tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public IconButton build() {
			//? if >1.20.1 {
			SpriteIconButton button = SpriteIconButton.builder(this.message, this.onPress, true)
				.sprite(this.spriteId, this.spriteWidth, this.spriteHeight)
				.size(this.width, this.height)
				////? >=26.2
				////.spriteOffset(this.spriteOffsetX, this.spriteOffsetY)
				.build();
			button.setPosition(this.x, this.y);
			if (this.tooltip != null) {
				button.setTooltip(Tooltip.create(this.tooltip));
			}
			return new IconButton(button, this.x, this.y, this.width, this.height, this.message);
			//? } else {
			/*// .textureSize(12, 12).usedTextureSize(12, 12).offset(0, 4)
			TextAndImageButton button = TextAndImageButton.builder(this.message, this.spriteId, this.onPress)
				.textureSize(this.spriteWidth, this.spriteHeight)
				.usedTextureSize(this.spriteWidth, this.spriteHeight)
				.offset(this.spriteOffsetX, this.spriteOffsetY)
				.build();
			if (this.tooltip != null) {
				button.setTooltip(Tooltip.create(this.tooltip));
			}
			button.setWidth(this.width);
			button.setPosition(this.x, this.y);
			return new IconButton(button, this.x, this.y, this.width, this.height, this.message);
			*///? }
		}


	}


}
