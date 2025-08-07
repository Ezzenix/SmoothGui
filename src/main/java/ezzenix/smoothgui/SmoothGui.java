package ezzenix.smoothgui;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SmoothGui implements ModInitializer {
	public static long lastGuiOpenedTime = 0;
	private static float appliedOffset = 0f;

	@Override
	public void onInitialize() { }

	private static float easeInBack(float t) {
		float c1 = 1.70158f;
		float c3 = c1 + 1;
		return c3 * t * t * t - c1 * t * t;
	}

	private static float getOffsetY() {
		MinecraftClient client = MinecraftClient.getInstance();

		float FADE_TIME = 220;
		float FADE_OFFSET = 9;

		float screenFactor = (float)(Math.max(client.getWindow().getHeight(), 512)) / 1080f;
		float timeSinceOpen = Math.min((float)(System.currentTimeMillis() - SmoothGui.lastGuiOpenedTime), FADE_TIME);
		float alpha = 1 - (timeSinceOpen/FADE_TIME);
		float modifiedAlpha = easeInBack(alpha);

		return modifiedAlpha * FADE_OFFSET * screenFactor;
	}

	private static boolean isInMainMenu() {
		MinecraftClient client = MinecraftClient.getInstance();
		return client.world == null && client.player == null;
	}

	public static void push(DrawContext context) {
		if (isInMainMenu()) return;
		appliedOffset = getOffsetY();
		context.getMatrices().translate(0f, -appliedOffset);
	}

	public static void pop(DrawContext context) {
		if (isInMainMenu()) return;
		context.getMatrices().translate(0f, appliedOffset);
		appliedOffset = 0f;
	}
}