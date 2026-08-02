package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import com.ezzenix.smoothgui.config.IConfigureScreen;
import com.ezzenix.smoothgui.config.ModConfig;
import com.ezzenix.smoothgui.config.ScreenMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Objects;

@Mixin(Screen.class)
public abstract class ConfigureScreenMixin implements IConfigureScreen {
	@Unique private static final int LINE_HEIGHT = 9;
	@Unique Button smoothGui$button = null;

	@Final @Shadow protected Minecraft minecraft;
	@Shadow protected abstract <T extends GuiEventListener & NarratableEntry> T addWidget(T arg);
	@Shadow protected abstract void removeWidget(GuiEventListener arg);

	public void smoothGui$init() {
		boolean enabled = ModConfig.configMode && !SmoothGui.isScreenFullyBlocked(self());

		if (enabled && (smoothGui$button == null || !self().children().contains(smoothGui$button))) {
			smoothGui$button = Button.builder(buildButtonLabel(ModConfig.getScreenMode((self()))), b -> {
				ModConfig.nextScreenMode(self());
				b.setMessage(buildButtonLabel(ModConfig.getScreenMode(self())));
			}).bounds(5, 27, 100, 20).build();
			this.addWidget(smoothGui$button);
		} else if (!enabled && smoothGui$button != null) {
			this.removeWidget(smoothGui$button);
			this.smoothGui$button = null;
		}
	}

	public void smoothGui$extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		if (this.smoothGui$button == null) return;

		this.smoothGui$button.extractRenderState(graphics, mouseX, mouseY, a);

		Screen screen = SmoothGui.getScreen();
		if (screen == null) return;
		drawLine(graphics, minecraft.font, Component.literal("SmoothGui Config"), 0);
		drawLine(graphics, minecraft.font, Component.literal("Screen: ").append(Component.literal(screen.getClass().getCanonicalName()).withStyle(ChatFormatting.AQUA)), 1);
	}

	@Unique
	private Screen self() {
		return (Screen)(Object)this;
	}

	@Inject(
		//~ if >=1.21.11 'init(Lnet/minecraft/client/Minecraft;II)V' -> 'init(II)V'
		method = "init(II)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init()V", shift = At.Shift.BEFORE)
	)
	private void onInit(/*? if <1.21.11 {*//*Minecraft minecraft,*//*?}*/ int i, int j, CallbackInfo ci) {
		this.smoothGui$init();
	}

	@Inject(method = "rebuildWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init()V", shift = At.Shift.BEFORE))
	private void onRebuild(CallbackInfo ci) {
		this.smoothGui$init();
	}

	@Unique
	private static Component buildButtonLabel(ScreenMode mode) {
		Component state = switch (mode) {
			case ON -> Component.literal("Yes").withStyle(ChatFormatting.GREEN);
			case OFF -> Component.literal("No").withStyle(ChatFormatting.RED);
			case DEFAULT -> Component.literal("Default").withStyle(ChatFormatting.YELLOW);
		};
		return Component.literal("Enabled: ").append(state);
	}

	@Unique
	private static void drawLine(GuiGraphicsExtractor graphics, Font font, Component text, int lineIndex) {
		Objects.requireNonNull(font);

		int lineY = 5 + ((LINE_HEIGHT + 1) * lineIndex);

		int lineWidth = font.width(text);
		int lineX = 5;
		Color lineColor = new Color(0xFFFFFF);
		Color backgroundColor = new Color(0x47000000, true);

		graphics.fill(lineX - 1, lineY - 1, lineX + lineWidth + 1, lineY + LINE_HEIGHT - 1, backgroundColor.getRGB());
		graphics.text(font, text, lineX, lineY, lineColor.getRGB(), false);
	}
}
