package com.ezzenix.smoothgui.config;

import com.ezzenix.smoothgui.lib.config.BaseConfig;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashSet;
import java.util.Set;

@BaseConfig.Config(title="Smooth Gui Config")
public class ModConfig extends BaseConfig {
	@Comment(name="Animation")
	public static Comment _animation;
	@Entry(name="Enabled")
	public static boolean enableAnimation = true;
	@Entry(name="Animation Duration", min=10, max=1000, suffix="ms")
	public static int animationTime = 220;
	@Entry(name="Animation Scale", min=0.5, max=3)
	public static double animationScale = 1;
	@Entry(name="Easing Style")
	public static EasingStyle animationStyle = EasingStyle.BACK;
	@Entry(name="Animation Direction")
	public static AnimationDirection animationDirection = AnimationDirection.DOWN;

	@Comment(name="Background")
	public static Comment _background;
	@Entry(name="Enable Background", desc="Disable this if you are having compatibility issues with another mod and do not want this mod to touch the background at all.")
	public static boolean modifyBackground = true;
	@Entry(min=0, max=1)
	public static float backgroundOpacity = 0.65f;
	@Entry(min=0, max=800, offText=true)
	public static int backgroundFadeTime = 150;
	//? if >=1.20.5 {
	@Entry(name="Always Blur", desc="Should the vanilla blur effect behind menus be applied to all screens.")
	public static boolean alwaysBlurBackground = false;
	//? }

	@Comment(name="Screen Filter")
	public static Comment _screens;
	@Entry(name="Repeat Same Screen", desc="Should the animation play when opening a screen of the same type. For example when navigating menus on servers.")
	public static boolean repeatSameScreen = false;
	@Entry(name="Config Mode", desc="Enable config mode to toggle which screens should be animated or not.")
	public static boolean configMode = false;

	@Entry @Hidden
	public static Set<String> screensForceEnabled = new HashSet<>();
	@Entry @Hidden
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
		ModConfig.save();
	}

}
