package net.inmisaddon.mixin;

import org.spongepowered.asm.mixin.Mixin;

import draylar.inmis.ui.BackpackHandledScreen;
import draylar.inmis.ui.BackpackScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(BackpackHandledScreen.class)
public abstract class BackpackHandledScreenMixin extends HandledScreen<BackpackScreenHandler> {

    public BackpackHandledScreenMixin(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InmisKeybindsAccessor.getOpenBackpackKeyBinding().matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
