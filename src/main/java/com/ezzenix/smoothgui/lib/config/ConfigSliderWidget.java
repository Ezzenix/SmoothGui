package com.ezzenix.smoothgui.lib.config;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class ConfigSliderWidget extends AbstractSliderButton {
	private final EntryInfo info;
	private final BaseConfig.Entry e;

	public ConfigSliderWidget(int x, int y, int width, int height, double value, EntryInfo info) {
		super(x, y, width, height, Component.empty(), Math.min(Math.max(value, 0), 1));
		this.e = info.entry;
		this.info = info;

		this.setMessage(buildLabel());
	}

	private Component buildLabel() {
		if (info.entry.offText() && String.valueOf(info.getValue()).equals("0")) {
			return Component.literal("OFF");
		}
		return Component.literal(info.getValue() + info.entry.suffix());
	}

	@Override
	protected void updateMessage() {
		setMessage(buildLabel());
	}

	@Override
	protected void applyValue() {
		if (info.getType() == int.class) info.setValue(((Number) (e.min() + value * (e.max() - e.min()))).intValue());
		else if (info.getType() == double.class)
			info.setValue(Math.round((e.min() + value * (e.max() - e.min())) * (double) e.precision()) / (double) e.precision());
		else if (info.getType() == float.class)
			info.setValue(Math.round((e.min() + value * (e.max() - e.min())) * (float) e.precision()) / (float) e.precision());
	}

}
