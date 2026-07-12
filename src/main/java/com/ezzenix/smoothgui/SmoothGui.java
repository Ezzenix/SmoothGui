package com.ezzenix.smoothgui;

import java.util.Set;

import com.ezzenix.smoothgui.config.AnimationDirection;
import com.ezzenix.smoothgui.config.ModConfig;
import com.ezzenix.smoothgui.config.ScreenMode;
import com.ezzenix.smoothgui.config.SmoothConfigScreen;
import com.ezzenix.smoothgui.lib.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if forge {
/*import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.client.ConfigScreenHandler;

@Mod(value = SmoothGui.MOD_ID)
public class SmoothGui {
*///? }

//? if neoforge {
/*import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SmoothGui.MOD_ID, dist = Dist.CLIENT)
public class SmoothGui {
*///? }

//? if fabric {
import net.fabricmc.api.ModInitializer;

public class SmoothGui implements ModInitializer {
//? }

    public static final String MOD_ID = "smoothgui";
    public static final String MOD_NAME = "SmoothGui";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	private static final Set<Class<?>> BLOCKED_SCREEN_CLASSES = Set.of(
		ChatScreen.class,
		LevelLoadingScreen.class,
		//? if <=1.20.4
		//GenericDirtMessageScreen.class,
		//? if >1.20.4
		GenericMessageScreen.class,
		//? if <1.21.9
		//ReceivingLevelScreen.class,
		ConnectScreen.class,
		ProgressScreen.class,
		InBedChatScreen.class
	);

	private static final Set<String> BLOCKED_SCREEN_NAMES = Set.of(

	);

	public static long lastScreenOpenedTime = 0;
	public static long lastScreenChangedTime = 0;

	public static float partialTick = 0f;

	public static float displacement = 0f;
	public static boolean applied = false;

	private static void initialize() {
		ModConfig.init(MOD_ID, ModConfig.class);
		ModConfig.configMode = false;
	}

	public static void onRender(Screen screen) {
		displacement = (ModConfig.enableAnimation && shouldAnimateScreen(screen)) ? calculateDisplacement() : 0;
	}

	public static void onRenderEnd(GuiGraphicsExtractor context) {
		if (applied) pop(context);
	}

	public static void onScreenChanged(Screen oldScreen, Screen newScreen) {
		if (newScreen == null) return;
		long now = System.currentTimeMillis();

		/* if screen is same type */
		if (oldScreen != null && oldScreen.getClass().equals(newScreen.getClass()) && !ModConfig.repeatSameScreen) return;

		/* curios mod compatibility */
		String newName = newScreen.getClass().getSimpleName();
		String oldName = oldScreen != null ? oldScreen.getClass().getSimpleName() : "";
		if (newName.equals("CuriosScreen")) return;
		if (newName.contains("InventoryScreen") && oldName.equals("CuriosScreen")) return;

		SmoothGui.lastScreenChangedTime = now;
		if (oldScreen == null) {
			SmoothGui.lastScreenOpenedTime = now;
		}
	}

	public static Screen getScreen() {
		//? if >= 26.2 {
		return Minecraft.getInstance().gui.screen();
		//?} else {
		/*return Minecraft.getInstance().screen;
		*///?}
	}

	public static float getAlphaSince(long time, float animationTime) {
		float timeSinceOpen = Math.min((float)(System.currentTimeMillis() - time), animationTime);
		return (timeSinceOpen/animationTime);
	}

	public static float getAlphaSince(long time) {
		return getAlphaSince(time, ModConfig.animationTime);
	}

	public static float calculateDisplacement(float alpha) {
		float FADE_OFFSET = 9;
		return FADE_OFFSET * (float)ModConfig.animationScale
			* ModConfig.animationStyle.apply((double)(1-alpha)).floatValue()
			* (float)ModConfig.animationScale
			* (ModConfig.animationDirection.equals(AnimationDirection.DOWN) ? 1f : -1f);
	}

	public static float calculateDisplacement() {
		return calculateDisplacement(getAlphaSince(SmoothGui.lastScreenChangedTime));
	}

	public static float getAppliedDisplacement() {
		return applied ? displacement : 0;
	}

	public static boolean isScreenFullyBlocked(Screen screen) {
		return (BLOCKED_SCREEN_CLASSES.contains(screen.getClass()) || BLOCKED_SCREEN_NAMES.contains(screen.getClass().getSimpleName()));
	}

	public static boolean shouldAnimateScreen(Screen screen) {
		if (isScreenFullyBlocked(screen)) return false;

		ScreenMode mode = ModConfig.getScreenMode(screen);
		if (mode.equals(ScreenMode.ON)) return true;
		if (mode.equals(ScreenMode.OFF)) return false;

		Minecraft client = Minecraft.getInstance();
		if (client.level == null && client.player == null) {
			return false;
		}

		for (GuiEventListener widget : screen.children()) {
			if (widget instanceof AbstractSelectionList<?>) {
				return false;
			}
		}

		return true;
	}

	public static void push(GuiGraphicsExtractor graphics) {
		if (!ModConfig.enableAnimation || applied || displacement == 0) return;
		Screen screen = getScreen();
		if (screen == null) return;
		applied = true;
		//~ if >=1.21.6 'pushPose' -> 'pushMatrix'
		graphics.pose().pushMatrix();
		//~ if >=1.21.6 '(0, -displacement, 0)' -> '(0, -displacement)'
		graphics.pose().translate(0, -displacement);
	}

	public static void pop(GuiGraphicsExtractor graphics) {
		if (!applied) return;
		applied = false;
		//~ if >=1.21.6 'popPose' -> 'popMatrix'
		graphics.pose().popMatrix();
	}

	public static void wrap(GuiGraphicsExtractor graphics, Runnable runnable) {
		if (!applied) {
			push(graphics);
			runnable.run();
			pop(graphics);
		} else {
			runnable.run();
		}
	}

	public static void wrapInverse(GuiGraphicsExtractor graphics, Runnable runnable) {
		if (applied) {
			pop(graphics);
			runnable.run();
			push(graphics);
		} else {
			runnable.run();
		}
	}

	//? if forge {
    /*public SmoothGui(final FMLJavaModLoadingContext context) {
        initialize();

		net.minecraftforge.fml.ModLoadingContext.get().registerExtensionPoint(
			ConfigScreenHandler.ConfigScreenFactory.class,
			() -> new ConfigScreenHandler.ConfigScreenFactory((c, parent) -> new SmoothConfigScreen(parent))
		);
    }
    *///? }

	//? if neoforge {
    /*public SmoothGui(ModContainer container) {
        initialize();
        container.registerExtensionPoint(IConfigScreenFactory.class, (c, parent) -> new SmoothConfigScreen(parent));
    }
    *///? }

	//? if fabric {
	@Override
	public void onInitialize() {
		initialize();
	}
	//? }
}
