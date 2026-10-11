package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.config.ModConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//~ if >=26.2 'Gui' -> 'Hud'
import net.minecraft.client.gui.Hud;

//~ if >=26.2 'Gui' -> 'Hud'
@Mixin(Hud.class)
public class HudMixin {
	@Unique
	private float smoothX = 0;
	@Unique
	private long lastRender = 0;

	@ModifyArg(
		//? if >=26.1 {
		method = "extractItemHotbar",
		//? } else if >= 1.20.6 {
		/*method = "renderItemHotbar",
		*///? } else
		//method = "renderHotbar",
		at = @At(
			value = "INVOKE",
			//? if >=26.1 {
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
			//? } else if >= 1.21.11 {
			/*target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
			*///? } else if >= 1.21.6 {
			/*target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
			*///? } else if >= 1.21.2 {
			/*target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/Identifier;IIII)V",
			*///? } else if >= 1.20.6 {
			/*target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lnet/minecraft/resources/Identifier;IIII)V",
			*///? } else
			//target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lnet/minecraft/resources/Identifier;IIIIII)V",
			ordinal = 1
		),
		//~ if >=1.21.2 '1' -> '2'
		index = 2
	)
	//? if >=1.21 {
	private int mod(int originalValue, @Local GuiGraphicsExtractor graphics, @Local Player player) {
	//? } else if >= 1.20.6 {
	/*private int mod(int originalValue, @Local GuiGraphicsExtractor graphics, @Local Player player) {
	*///? } else
	//private int mod(int originalValue, @Local GuiGraphicsExtractor graphics, @Local Player player) {
		if (!ModConfig.smoothHotbar) {
			return originalValue;
		}

		long now = System.nanoTime();
		//~ if >=1.21.5 '.selected' -> '.getSelectedSlot()'
		float target = player.getInventory().getSelectedSlot() * 20;

		float deltaTime = Math.min((now - lastRender) / 1_000_000_000f, 0.1f);
		lastRender = now;

		float diff = target - smoothX;
		smoothX = Math.abs(diff) < 1f
			? target
			: smoothX + diff * Math.min(deltaTime * (float)ModConfig.hotbarSpeed, 1f);

		int leftX = (graphics.guiWidth() / 2) - 92;
		return Math.round(leftX + smoothX);
	}
}
