package com.ezzenix.smoothgui.lib;

import net.minecraft.resources.Identifier;

public class EmId {
	public static Identifier of(String full) {
		Identifier result = Identifier.tryParse(full);
		if (result == null) {
			throw new RuntimeException("Invalid identifier: " + full);
		}
		return result;
	}

	public static Identifier of(String namespace, String path) {
		return of(namespace + ":" + path);
	}

	public static Identifier withDefaultNamespace(String path) {
		return of("minecraft:" + path);
	}

	public static Identifier forSprite(String namespace, String spriteName) {
		//? if >1.20.1 {
		return of(namespace, spriteName);
		//? } else {
		/*return of(namespace, "textures/gui/sprites/"+spriteName+".png");
		*///? }
	}


}
