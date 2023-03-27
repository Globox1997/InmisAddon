package net.inmisaddon.util;

import org.jetbrains.annotations.Nullable;

import draylar.inmis.network.ServerNetworking;
import draylar.inmis.ui.BackpackHandledScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.libz.api.InventoryTab;
import net.libz.registry.TabRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BackpackTab extends InventoryTab {

    public BackpackTab(Text title, @Nullable Identifier texture, int preferedPos, Class<?>... screenClasses) {
        super(title, texture, preferedPos, screenClasses);
    }

    @Nullable
    @Override
    public ItemStack getItemStack(MinecraftClient client) {
        return BackpackUtil.getEquippedBackpack(client.player);
    }

    @Override
    public boolean shouldShow(MinecraftClient client) {
        return BackpackUtil.getEquippedBackpack(client.player) != null && !BackpackUtil.getEquippedBackpack(client.player).isEmpty();
    }

    @Override
    public void onClick(MinecraftClient client) {
        ClientPlayNetworking.send(ServerNetworking.OPEN_BACKPACK, new PacketByteBuf(Unpooled.buffer()));
    }

    // Had to outsource registry here
    public static void init() {
        TabRegistry.registerInventoryTab(new BackpackTab(Text.translatable("screen.inmisaddon.backpack_screen"), null, -1, BackpackHandledScreen.class));
    }
}
