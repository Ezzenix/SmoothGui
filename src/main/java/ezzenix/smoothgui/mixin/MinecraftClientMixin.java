package ezzenix.smoothgui.mixin;

import ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public Screen currentScreen;

	@Inject(
		method = "setScreen",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
			opcode = Opcodes.PUTFIELD
		),
		locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	private void onSetScreen(Screen screen, CallbackInfo ci) {
		if (screen != null && currentScreen == null) {
			SmoothGui.lastGuiOpenedTime = System.currentTimeMillis();
		}
	}
}