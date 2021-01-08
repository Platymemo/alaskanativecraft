package com.github.platymemo.alaskanativecraft.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class AlaskaNativeModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // TODO
            // Return the screen here with the one you created from Cloth Config Builder
            return null;
        };
    }
}
