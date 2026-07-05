package com.ezzenix.smoothgui.mixin.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? >=1.21.6 {
//~ if >=26.1 'gui.render.state' -> 'renderer.state.gui'
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@Mixin(GuiGraphicsExtractor.class)
public interface GuiGraphicAccessor {
	@Accessor("guiRenderState")
	GuiRenderState smoothgui$getGuiRenderState();
}

//? } else {
/*@Mixin(Minecraft.class)
public interface GuiGraphicAccessor { }
*///? }
