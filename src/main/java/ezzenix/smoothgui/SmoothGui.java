package ezzenix.smoothgui;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmoothGui implements ModInitializer {
	public static final String MOD_ID = "smoothgui";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("SmoothGui initialized!");
	}

	//
	public static long lastGuiOpenedTime = 0;

	private static float easeInBack(float t) {
//		float c1 = 1.70158f;
//		float c3 = c1 + 1;
//		return c3 * t * t * t - c1 * t * t;
		return (float) (1 - Math.sqrt(1 - Math.pow(t, 2)));
	}

	public static float getOffsetY() {
		MinecraftClient client = MinecraftClient.getInstance();

//		float FADE_TIME = 220;
		float FADE_TIME = 500;
//		float FADE_OFFSET = 9;
		float FADE_OFFSET = 50;

		float screenFactor = (float)client.getWindow().getHeight() / 1080;
		float timeSinceOpen = Math.min((float)(System.currentTimeMillis() - SmoothGui.lastGuiOpenedTime), FADE_TIME);
		float alpha = 1 - (timeSinceOpen/FADE_TIME);
		float modifiedAlpha = easeInBack(alpha);

		return modifiedAlpha * FADE_OFFSET * screenFactor;
	}

	public static boolean isInMenu() {
		MinecraftClient client = MinecraftClient.getInstance();
		return client.world == null && client.player == null;
	}
}