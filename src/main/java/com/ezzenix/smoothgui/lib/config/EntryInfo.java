package com.ezzenix.smoothgui.lib.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class EntryInfo {
	public BaseConfig.Entry entry;
	public BaseConfig.Comment comment;
	public Field field;
	public final Object defaultValue;

	public EntryInfo(Field field) {
		this.field = field;
		this.entry = field.getAnnotation(BaseConfig.Entry.class);
		this.comment = field.getAnnotation(BaseConfig.Comment.class);
		this.defaultValue = getValue();
	}

	public String getName() {
		if (this.comment != null && !this.comment.name().isEmpty()) {
			return this.comment.name();
		} else if (this.entry != null && !this.entry.name().isEmpty()) {
			return this.entry.name();
		} else {
			String cleanName = this.field.getName().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
			cleanName = cleanName.substring(0, 1).toUpperCase() + cleanName.substring(1);
			return cleanName;
		}
	}

	public Tooltip getTooltip() {
		if (this.entry != null && !this.entry.desc().trim().isEmpty()) {
			return Tooltip.create(Component.literal(this.entry.desc().trim()));
		}
		return null;
	}

	public Class<?> getType() {
		if (this.field == null) return null;
		return this.field.getType();
	}

	public Object getValue() {
		try {
			return field.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setValue(Object newValue) {
		try {
			field.set(null, newValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		//? if >=26.2
		Screen screen = Minecraft.getInstance().gui.screen();
		//? if <26.2
		//Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof ConfigScreen configScreen) {
			configScreen.changed();
		}
	}
}
