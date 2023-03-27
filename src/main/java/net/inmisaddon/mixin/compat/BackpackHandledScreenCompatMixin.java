package net.inmisaddon.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import draylar.inmis.ui.BackpackHandledScreen;
import draylar.inmis.ui.BackpackScreenHandler;
import net.inmisaddon.mixin.InmisKeybindsAccessor;
import net.libz.api.Tab;
import net.libz.util.DrawTabHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

@Mixin(BackpackHandledScreen.class)
public abstract class BackpackHandledScreenCompatMixin extends HandledScreen<BackpackScreenHandler> implements Tab {

    public BackpackHandledScreenCompatMixin(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderMixin(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        DrawTabHelper.drawTab(client, matrices, this, x, y, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        DrawTabHelper.onTabButtonClick(client, this, this.x, this.y, mouseX, mouseY, this.focusedSlot != null);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InmisKeybindsAccessor.getOpenBackpackKeyBinding().matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        } else
            return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
