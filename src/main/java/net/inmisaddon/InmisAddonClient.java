package net.inmisaddon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.inmisaddon.util.BackpackTab;

@Environment(EnvType.CLIENT)
public class InmisAddonClient implements ClientModInitializer {

    private static final boolean isLibZLoaded = FabricLoader.getInstance().isModLoaded("libz");

    @Override
    public void onInitializeClient() {
        if (isLibZLoaded) {
            BackpackTab.init();
        }
    }

}
