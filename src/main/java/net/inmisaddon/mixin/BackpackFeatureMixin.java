package net.inmisaddon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import draylar.inmis.Inmis;
import draylar.inmis.client.BackpackFeature;
import draylar.inmis.item.BackpackItem;
import draylar.inmis.item.DyeableBackpackItem;
import draylar.inmis.item.DyeableTrinketBackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
@Mixin(BackpackFeature.class)
public abstract class BackpackFeatureMixin extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final BackpackModel backpackModel = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    private final BabyBackpackModel babyBackpackModel = new BabyBackpackModel(BabyBackpackModel.getTexturedModelData().createModel());

    public BackpackFeatureMixin(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta,
            float animationProgress, float headYaw, float headPitch, CallbackInfo info) {

        ItemStack chestSlot = player.getEquippedStack(EquipmentSlot.CHEST);

        if (chestSlot.getItem() instanceof BackpackItem) {
            matrices.push();
            ModelPart modelPart = this.getContextModel().body;
            modelPart.rotate(matrices);

            matrices.translate(0D, -0.9D, 0.2D);

            Model backpackModel = chestSlot.isOf(Inmis.BACKPACKS.get(0)) ? this.babyBackpackModel : this.backpackModel;

            VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers,
                    backpackModel.getLayer(new Identifier("inmisaddon", "textures/entity/" + Registry.ITEM.getId(chestSlot.getItem()).getPath() + ".png")), false, chestSlot.hasGlint());

            float f = 1.0F;
            float g = 1.0F;
            float h = 1.0F;
            if (chestSlot.getItem() instanceof DyeableBackpackItem) {
                int i = ((DyeableBackpackItem) chestSlot.getItem()).getColor(chestSlot);
                f = (float) (i >> 16 & 0xFF) / 255.0f;
                g = (float) (i >> 8 & 0xFF) / 255.0f;
                h = (float) (i & 0xFF) / 255.0f;
            } else if (chestSlot.getItem() instanceof DyeableTrinketBackpackItem) {
                int i = ((DyeableTrinketBackpackItem) chestSlot.getItem()).getColor(chestSlot);
                f = (float) (i >> 16 & 0xFF) / 255.0f;
                g = (float) (i >> 8 & 0xFF) / 255.0f;
                h = (float) (i & 0xFF) / 255.0f;
            }
            backpackModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
            matrices.pop();
        }

        info.cancel();
    }
}
