package com.ezzenix.smoothgui.mixin;

import com.ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiGraphicsExtractor.class)
public class GraphicsMixin {
	@ModifyVariable(method = "enableScissor", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	public int modifyScissorY0(int y0) {
		//? if >=1.21.4
		return y0;
		//? if <1.21.4
		//return y0 - (int)SmoothGui.getAppliedDisplacement();
	}

	@ModifyVariable(method = "enableScissor", at = @At("HEAD"), ordinal = 3, argsOnly = true)
	public int modifyScissorY1(int y1) {
		//? if >=1.21.4
		return y1;
		//? if <1.21.4
		//return y1 - (int)SmoothGui.getAppliedDisplacement();
	}
}
