package net.inmisaddon.mixin.compat;

import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import draylar.inmis.item.BackpackItem;
import draylar.inmis.network.ServerNetworking;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.inmisaddon.util.BackpackRenderUtil;
import net.levelz.gui.LevelzScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(LevelzScreen.class)
public abstract class LevelzScreenMixin extends CottonClientScreen {

    public LevelzScreenMixin(GuiDescription description) {
        super(description);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 1, shift = Shift.BEFORE))
    private void renderMixin(MatrixStack matrices, int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        if (this.client != null && this.client.player != null && this.client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            RenderSystem.setShaderTexture(0, BackpackRenderUtil.GUI_TAB_ICONS);
            int xPos = 50;
            if (BackpackRenderUtil.isJobsAddonLoaded)
                xPos = 75;
            if (LibGui.isDarkMode())
                this.drawTexture(matrices, this.left + xPos, this.top - 21, 48, 0, 24, 21);
            else
                this.drawTexture(matrices, this.left + xPos, this.top - 21, 0, 0, 24, 21);

            this.client.getItemRenderer().renderInGui(this.client.player.getEquippedStack(EquipmentSlot.CHEST), this.left + xPos + 4, this.top - 17);

            if (this.isPointWithinIconBounds(xPos, 23, (double) mouseX, (double) mouseY))
                this.renderTooltip(matrices, Text.translatable("screen.inmisaddon.backpack_screen"), mouseX, mouseY);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClickedMixin(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> info) {
        if (this.client != null && this.isPointWithinIconBounds(BackpackRenderUtil.isJobsAddonLoaded ? 75 : 50, 23, (double) mouseX, (double) mouseY) && this.client.player != null
                && this.client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem)
            ClientPlayNetworking.send(ServerNetworking.OPEN_BACKPACK, new PacketByteBuf(Unpooled.buffer()));
    }

    @Shadow
    private boolean isPointWithinIconBounds(int x, int width, double pointX, double pointY) {
        return false;
    }
}
