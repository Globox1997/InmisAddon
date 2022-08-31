package net.inmisaddon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class InmisAddon implements ClientModInitializer {

    public static final EntityModelLayer BACKPACK_LAYER = new EntityModelLayer(new Identifier("inmisaddon:backpack_render_layer"), "backpack_render_layer");
    public static final EntityModelLayer BABY_BACKPACK_LAYER = new EntityModelLayer(new Identifier("inmisaddon:baby_backpack_render_layer"), "baby_backpack_render_layer");

    public static final boolean isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(BACKPACK_LAYER, BackpackModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BABY_BACKPACK_LAYER, BabyBackpackModel::getTexturedModelData);
    }

}
