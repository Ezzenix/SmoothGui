package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
	//? >=1.21.6 {
	@ModifyArgs(
		//~ if >=26.1 'renderBg' -> 'extractBackground'
		method = "extractBackground",
		at = @At(
			value = "INVOKE",
			//~ if >=26.1 'renderEntityInInventoryFollowsMouse' -> 'extractEntityInInventoryFollowsMouse'
			target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"
		)
	)
	private static void shiftEntityBoundingBox(Args args) {
		int displacement = (int) SmoothGui.displacement;
		// Apply vertical offset to y0 and y1
		args.set(2, (int) args.get(2) - displacement);
		args.set(4, (int) args.get(4) - displacement);
	}

	//? <1.21.11 {
	/*@WrapOperation(method = "renderEntityInInventoryFollowsMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;enableScissor(IIII)V"))
	private static void wrapEnableScissor(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, Operation<Void> original) {
		int displacement = (int) SmoothGui.displacement;
		original.call(graphics, x0, y0 + displacement, x1, y1 + displacement);
	}
	*///? }
	//? }
}
