package net.inmisaddon.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.trinkets.api.SlotReference;
import draylar.inmis.Inmis;
import draylar.inmis.client.TrinketBackpackRenderer;
import draylar.inmis.item.DyeableBackpackItem;
import draylar.inmis.item.DyeableTrinketBackpackItem;
import draylar.inmis.item.TrinketBackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@SuppressWarnings("rawtypes")
@Environment(EnvType.CLIENT)
@Mixin(TrinketBackpackRenderer.class)
public abstract class TrinketBackpackRendererMixin {

    private final BackpackModel backpackModel = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    private final BabyBackpackModel babyBackpackModel = new BabyBackpackModel(BabyBackpackModel.getTexturedModelData().createModel());

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            LivingEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo info) {

        if (!Inmis.CONFIG.trinketRendering) {
            info.cancel();
            return;
        }
        if (!player.getEquippedStack(EquipmentSlot.CHEST).isEmpty() && player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof TrinketBackpackItem) {
            info.cancel();
            return;
        }

        matrices.push();
        if (contextModel instanceof BipedEntityModel) {
            ModelPart modelPart = ((BipedEntityModel) contextModel).body;
            modelPart.rotate(matrices);
        }
        matrices.translate(0D, -0.9D, 0.2D);

        Model backpackModel = stack.isOf(Inmis.BACKPACKS.get(0)) ? this.babyBackpackModel : this.backpackModel;

        VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers,
                backpackModel.getLayer(new Identifier("inmisaddon", "textures/entity/" + Registries.ITEM.getId(stack.getItem()).getPath() + ".png")), false, stack.hasGlint());

        float f = 1.0F;
        float g = 1.0F;
        float h = 1.0F;
        if (stack.getItem() instanceof DyeableTrinketBackpackItem) {
            int i = ((DyeableTrinketBackpackItem) stack.getItem()).getColor(stack);
            f = (float) (i >> 16 & 0xFF) / 255.0f;
            g = (float) (i >> 8 & 0xFF) / 255.0f;
            h = (float) (i & 0xFF) / 255.0f;
        } else if (stack.getItem() instanceof DyeableBackpackItem) {
            int i = ((DyeableBackpackItem) stack.getItem()).getColor(stack);
            f = (float) (i >> 16 & 0xFF) / 255.0f;
            g = (float) (i >> 8 & 0xFF) / 255.0f;
            h = (float) (i & 0xFF) / 255.0f;
        }
        backpackModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
        matrices.pop();

        info.cancel();
    }
}
