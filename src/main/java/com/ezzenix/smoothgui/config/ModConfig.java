package com.ezzenix.smoothgui.config;

import com.ezzenix.emlib.config.EmConfig;
import com.ezzenix.smoothgui.SmoothGui;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashSet;
import java.util.Set;

@EmConfig.Config(title = SmoothGui.MOD_NAME)
public class ModConfig extends EmConfig {
	@Comment
	public static Comment _animation;
	@Option
	public static boolean enableAnimation = true;
	@Option(min=10, max=1000, isSlider=true, suffix="ms")
	public static int animationTime = 220;
	@Option(min=0.5, max=3,  isSlider=true)
	public static double animationScale = 1;
	@Option
	public static EasingStyle animationStyle = EasingStyle.BACK;
	@Option
	public static AnimationDirection animationDirection = AnimationDirection.DOWN;

	@Comment
	public static Comment _background;
	@Option
	public static boolean modifyBackground = true;
	@Option(min=0, max=1, isSlider=true)
	public static float backgroundOpacity = 0.65f;
	@Option(min=0, max=800, isSlider=true, offText=true)
	public static int backgroundFadeTime = 150;
	//? if >=1.20.5 {
	@Option
	public static boolean alwaysBlurBackground = false;
	//? }

	@Comment
	public static Comment _screens;
	@Option
	public static boolean repeatSameScreen = false;
	@Option
	public static boolean configMode = false;

	@Option @Hidden
	public static Set<String> screensForceEnabled = new HashSet<>();
	@Option @Hidden
	public static Set<String> screensForceDisabled = new HashSet<>();

	public static ScreenMode getScreenMode(Screen screen) {
		if (screensForceEnabled.contains(screen.getClass().getCanonicalName())) {
			return ScreenMode.ON;
		} else if (screensForceDisabled.contains(screen.getClass().getCanonicalName())) {
			return ScreenMode.OFF;
		} else {
			return ScreenMode.DEFAULT;
		}
	}

	public static void nextScreenMode(Screen screen) {
		String name = screen.getClass().getCanonicalName();
		ScreenMode nextMode = getScreenMode(screen).next();
		if (nextMode.equals(ScreenMode.ON)) {
			screensForceEnabled.add(name);
			screensForceDisabled.remove(name);
		} else if (nextMode.equals(ScreenMode.OFF)) {
			screensForceDisabled.add(name);
			screensForceEnabled.remove(name);
		} else {
			screensForceEnabled.remove(name);
			screensForceDisabled.remove(name);
		}
		EmConfig.save(SmoothGui.MOD_ID);
	}

}
