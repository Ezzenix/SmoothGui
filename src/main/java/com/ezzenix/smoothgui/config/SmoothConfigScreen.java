package com.ezzenix.smoothgui.config;

import com.ezzenix.emlib.config.ConfigScreen;
import com.ezzenix.emlib.config.EmConfig;
import com.ezzenix.emlib.util.EmGraphics;
import com.ezzenix.emlib.util.EmId;
import com.ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

//? if >=1.21.5
import net.minecraft.client.renderer.RenderPipelines;

//? if >=1.21.2 && <=1.21.5
//import net.minecraft.client.renderer.RenderType;

//? if >=1.21.9
import net.minecraft.client.input.KeyEvent;

public class SmoothConfigScreen extends ConfigScreen {
	private static final Identifier CONTAINER_BACKGROUND = EmId.withDefaultNamespace("textures/gui/container/generic_54.png");

	private boolean showPreview = false;
	private long previewStart = 0;
	private Button previewButton;

	private boolean lastConfigModeState = ModConfig.configMode;

	public SmoothConfigScreen(Screen parent, EmConfig instance) {
		super(parent, instance);
	}

	@Override
	public void init() {
		super.init();

		this.previewButton = Button.builder(buildPreviewButtonMessage(), b -> {
			this.showPreview = !this.showPreview;
			this.previewStart = System.currentTimeMillis();
			b.setMessage(buildPreviewButtonMessage());
		}).bounds(6, this.height - 26, 60, 20).build();

		this.addRenderableWidget(this.previewButton);
	}

	private Component buildPreviewButtonMessage() {
		return !this.showPreview ? Component.translatable("smoothgui.preview") : Component.translatable("smoothgui.stop");
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		if (!showPreview) return;

		int rowCount = 3;
		int imageWidth = 176;
		int imageHeight = 114 + rowCount * 18;

		float alpha = getPreviewAnimationAlpha();
		int offsetY = (int)SmoothGui.calculateDisplacement(alpha);

		//? if <1.21.6 {
		/*graphics.pose().pushPose();
		graphics.pose().translate(0, 0, 200f);
		*///? }

		graphics.fill(0, 0, this.width, this.height, 0x32000000);

		int xo = (this.width - imageWidth) / 2;
		int yo = (this.height - imageHeight) / 2 + offsetY;
		//? if >=1.21.6 {
		graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo, 0.0F, 0.0F, imageWidth, rowCount * 18 + 17, 256, 256);
		graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo + rowCount * 18 + 17, 0.0F, 126.0F, imageWidth, 96, 256, 256);
		//? } else if >=1.21.2 {
		/*graphics.blit(RenderType::guiTextured, CONTAINER_BACKGROUND, xo, yo, 0.0F, 0.0F, imageWidth, rowCount * 18 + 17, 256, 256);
		graphics.blit(RenderType::guiTextured, CONTAINER_BACKGROUND, xo, yo + rowCount * 18 + 17, 0.0F, 126.0F, imageWidth, 96, 256, 256);
		*///? } else {
		/*graphics.blit(CONTAINER_BACKGROUND, xo, yo, 0.0F, 0.0F, imageWidth, rowCount * 18 + 17, 256, 256);
		graphics.blit(CONTAINER_BACKGROUND, xo, yo + rowCount * 18 + 17, 0.0F, 126.0F, imageWidth, 96, 256, 256);
		*///? }

		//? if <1.21.6 {
		/*graphics.pose().popPose();
		*///? }
	}

	@Override
	public void changed() {
		this.previewStart = System.currentTimeMillis() + 700;

		if (ModConfig.configMode != this.lastConfigModeState) {
			IConfigureScreen.of(this).smoothgui$init();
			this.lastConfigModeState = ModConfig.configMode;
		}
	}

	private float getPreviewAnimationAlpha() {
		long animationTime = ModConfig.animationTime;
		long pauseTime = 700;

		long elapsed = System.currentTimeMillis() - this.previewStart;
		if (elapsed < 0) return 1f;
		long cycle = animationTime + pauseTime;

		long cycleTime = elapsed % cycle;

		if (cycleTime < animationTime) {
			return (float) cycleTime / animationTime;
		} else {
			return 1.0f;
		}
	}

	@Override
	//? if >=1.21.9 {
	public boolean keyPressed(KeyEvent event) {
		int key = event.key();
	//? } else {
	/*public boolean keyPressed(int key, int j, int k) {
	*///? }
		if (showPreview && key == GLFW.GLFW_KEY_ESCAPE) {
			showPreview = false;
			this.previewButton.setMessage(buildPreviewButtonMessage());
			return true;
		}
		//? if >=1.21.9 {
		return super.keyPressed(event);
		//? } else {
		/*return super.keyPressed(key, j, k);
		 *///? }
	}
}
