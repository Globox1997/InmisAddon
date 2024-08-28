package net.inmisaddon.util;

import java.util.Iterator;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import draylar.inmis.Inmis;
import draylar.inmis.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.inmisaddon.InmisAddonClient;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

@SuppressWarnings("rawtypes")
@Environment(EnvType.CLIENT)
public class BackpackUtil {

    public static boolean isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

    private static final BackpackModel backpackModel = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    private static final BabyBackpackModel babyBackpackModel = new BabyBackpackModel(BabyBackpackModel.getTexturedModelData().createModel());
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean renderBackpack(PlayerEntityModel playerEntityModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BackpackItem) {
            if (InmisAddonClient.isFirstPersonLoaded && client.player != null && client.player.isMainPlayer() && client.options.getPerspective().isFirstPerson() && client.player.isSwimming()) {
                return true;
            }
            matrices.push();
            ModelPart modelPart = playerEntityModel.body;
            modelPart.rotate(matrices);

            matrices.translate(0D, -0.9D, 0.2D);

            Model backpackModel = itemStack.isOf(Inmis.BACKPACKS.get(0)) ? BackpackUtil.babyBackpackModel : BackpackUtil.backpackModel;

            VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers,
                    backpackModel.getLayer(Identifier.of("inmisaddon", "textures/entity/" + Registries.ITEM.getId(itemStack.getItem()).getPath() + ".png")), false, itemStack.hasGlint());

            int color = -1;
            if (itemStack.isIn(ItemTags.DYEABLE) && itemStack.get(DataComponentTypes.DYED_COLOR) != null) {
                color = itemStack.get(DataComponentTypes.DYED_COLOR).rgb();
            }
            backpackModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, color);
            matrices.pop();
            return true;
        }
        return false;
    }

    public static boolean isBackpackEquipped(PlayerEntity playerEntity) {
        if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            return true;
        } else if (isTrinketsLoaded) {
            return TrinketsApi.getTrinketComponent(playerEntity).get().isEquipped(stack -> stack.getItem() instanceof BackpackItem);
        } else {
            return false;
        }
    }

    @Nullable
    public static ItemStack getEquippedBackpack(PlayerEntity playerEntity) {
        if (isTrinketsLoaded && TrinketsApi.getTrinketComponent(playerEntity).get().isEquipped(stack -> stack.getItem() instanceof BackpackItem)) {
            return TrinketsApi.getTrinketComponent(playerEntity).get().getEquipped(stack -> stack.getItem() instanceof BackpackItem).get(0).getRight();
        } else if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            return playerEntity.getEquippedStack(EquipmentSlot.CHEST);
        }
        return null;
    }

}
