package com.ezzenix.smoothgui.lib;

import java.nio.file.Path;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?}

//? if neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
*///?}

//? if forge {
/*import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
*///?}

public class Platform {
	//? if fabric {
	public static String getName() {
		return "fabric";
	}
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}
	public static boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
	//?} else if neoforge {
    /*public static String getName() {
        return "neoforge";
    }
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
    *///?} else if forge {
    /*public static String getName() {
        return "forge";
    }
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
    *///?}
}
