package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import com.ezzenix.smoothgui.config.IConfigureScreen;
import com.ezzenix.smoothgui.config.ModConfig;
import com.ezzenix.smoothgui.config.ScreenMode;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(Screen.class)
public abstract class ConfigureScreenMixin implements IConfigureScreen {
	@Unique private static final int LINE_HEIGHT = 9;
	@Unique Button smoothgui$button = null;

	@Final @Shadow protected Minecraft minecraft;
	@Shadow protected abstract <T extends GuiEventListener & NarratableEntry> T addWidget(T widget);
	@Shadow protected abstract void removeWidget(GuiEventListener widget);
	@Shadow public abstract List<? extends GuiEventListener> children();

	public void smoothgui$init() {
		if (self() instanceof RealmsNotificationsScreen) return;

		boolean enabled = ModConfig.configMode && !SmoothGui.isScreenFullyBlocked(self());

		if (smoothgui$button != null && !this.children().contains(smoothgui$button)) {
			smoothgui$button = null;
		}

		if (enabled && smoothgui$button == null) {
			smoothgui$button = Button.builder(buildButtonLabel(ModConfig.getScreenMode((self()))), b -> {
				ModConfig.nextScreenMode(self());
				b.setMessage(buildButtonLabel(ModConfig.getScreenMode(self())));
			}).bounds(5, 27, 100, 20).build();
			this.addWidget(smoothgui$button);
		} else if (!enabled && smoothgui$button != null) {
			this.removeWidget(smoothgui$button);
			this.smoothgui$button = null;
		}
	}

	public void smoothgui$extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		if (this.smoothgui$button == null) return;
		Screen screen = (Screen)(Object)this;

		//? if <1.21.6 {
		/*graphics.pose().pushPose();
		graphics.pose().translate(0, 0, 1);
		*///? }
		this.smoothgui$button.extractRenderState(graphics, mouseX, mouseY, a);
		//? if <1.21.6 {
		/*graphics.pose().popPose();
		*///? }

		drawLine(graphics, minecraft.font, Component.translatable("smoothgui.configmode.title"), 0);
		drawLine(graphics, minecraft.font, Component.translatable("smoothgui.configmode.screen").append(": ").append(Component.literal(screen.getClass().getCanonicalName()).withStyle(ChatFormatting.AQUA)), 1);
	}

	@Unique
	public Button smoothgui$getButton() {
		return this.smoothgui$button;
	}

	@Unique
	private Screen self() {
		return (Screen)(Object)this;
	}

	@Inject(
		//~ if >=1.21.11 'init(Lnet/minecraft/client/Minecraft;II)V' -> 'init(II)V'
		method = "init(II)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init()V", shift = At.Shift.AFTER)
	)
	private void onInit(/*? if <1.21.11 {*//*Minecraft minecraft,*//*?}*/ int i, int j, CallbackInfo ci) {
		this.smoothgui$init();
	}

	@Inject(method = "rebuildWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init()V", shift = At.Shift.AFTER))
	private void onRebuild(CallbackInfo ci) {
		this.smoothgui$init();
	}

	@Inject(method = "children", at = @At("RETURN"), cancellable = true)
	private void onGetChildren(CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
		if (ModConfig.configMode) {
			Screen screen = (Screen) (Object) this;
			Button configButton = IConfigureScreen.of(screen).smoothgui$getButton();
			if (configButton != null && configButton.isActive()) {
				List<GuiEventListener> list = new ArrayList<>(cir.getReturnValue());
				list.remove(configButton);
				list.add(0, configButton);
				cir.setReturnValue(list);
			}
		}
	}

	@Unique
	private static Component buildButtonLabel(ScreenMode mode) {
		Component state = switch (mode) {
			case ON -> Component.translatable("smoothgui.configmode.on").withStyle(ChatFormatting.GREEN);
			case OFF -> Component.translatable("smoothgui.configmode.off").withStyle(ChatFormatting.RED);
			case DEFAULT -> Component.translatable("smoothgui.configmode.default").withStyle(ChatFormatting.YELLOW);
		};
		return Component.translatable("smoothgui.configmode.enabled").append(": ").append(state);
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
