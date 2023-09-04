package com.github.platymemo.alaskanativecraft.compat;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class AlaskaModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(AlaskaConfig.class, parent).get();
	}
}
