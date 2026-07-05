package com.ezzenix.smoothgui.config;

public enum ScreenMode {
	ON,
	OFF,
	DEFAULT;

	public ScreenMode next() {
		ScreenMode[] values = ScreenMode.values();
		return values[(this.ordinal() + 1) % values.length];
	}
}
