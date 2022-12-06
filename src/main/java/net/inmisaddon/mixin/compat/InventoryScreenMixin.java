package net.inmisaddon.mixin.compat;

import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import draylar.inmis.network.ServerNetworking;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.inmisaddon.util.BackpackUtil;
import net.levelz.init.ConfigInit;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(value = InventoryScreen.class, priority = 998)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if (this.client != null && ConfigInit.CONFIG.inventoryButton && this.focusedSlot == null
                && this.isPointWithinBounds(BackpackUtil.isJobsAddonLoaded ? 77 : 52, -20, 22, 19, (double) mouseX, (double) mouseY) && this.client.player != null
                && BackpackUtil.isBackPackEquipped(this.client.player))
            ClientPlayNetworking.send(ServerNetworking.OPEN_BACKPACK, new PacketByteBuf(Unpooled.buffer()));
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (this.client != null && this.client.player != null && ConfigInit.CONFIG.inventoryButton && BackpackUtil.isBackPackEquipped(this.client.player)) {
            RenderSystem.setShaderTexture(0, BackpackUtil.GUI_TAB_ICONS);
            int xPos = 50;
            if (BackpackUtil.isJobsAddonLoaded)
                xPos = 75;

            this.drawTexture(matrices, this.x + xPos, this.y - 21, 0, 0, 24, 21);
            this.client.getItemRenderer().renderInGui(this.client.player.getEquippedStack(EquipmentSlot.CHEST), this.x + xPos + 4, this.y - 17);

            if (this.isPointWithinBounds(xPos + 2, -20, 22, 19, (double) mouseX, (double) mouseY))
                this.renderTooltip(matrices, Text.translatable("screen.inmisaddon.backpack_screen"), mouseX, mouseY);
        }
    }
}
