//? if fabric {
package com.ezzenix.smoothgui.impl;

import com.ezzenix.smoothgui.config.SmoothConfigScreen;
import com.ezzenix.smoothgui.lib.config.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SmoothConfigScreen::new;
    }

}
//?}
