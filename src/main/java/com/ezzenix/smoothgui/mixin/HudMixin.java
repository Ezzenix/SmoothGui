package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.config.ModConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Hud.class)
public class HudMixin {
	@Unique
	private float smoothX = 0;
	@Unique
	private long lastRender = 0;

	@ModifyArgs(
		method = "extractItemHotbar",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
			ordinal = 1
		)
	)
	private void mod(Args args, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, @Local Player player) {
		if (!ModConfig.smoothHotbar) {
			return;
		}

		long now = System.nanoTime();
		float target = player.getInventory().getSelectedSlot() * 20;

		float deltaTime = Math.min((now - lastRender) / 1_000_000_000f, 0.1f);
		lastRender = now;

		float diff = target - smoothX;
		smoothX = Math.abs(diff) < 1f
			? target
			: smoothX + diff * Math.min(deltaTime * ModConfig.hotbarSpeed, 1f);

		int leftX = (graphics.guiWidth() / 2) - 92;
		args.set(2, Math.round(leftX + smoothX));
	}
}
