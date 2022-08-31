package net.inmisaddon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import draylar.inmis.client.InmisKeybinds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;

@Environment(EnvType.CLIENT)
@Mixin(InmisKeybinds.class)
public interface InmisKeybindsAccessor {

    @Accessor("OPEN_BACKPACK")
    static KeyBinding getOpenBackpackKeyBinding() {
        throw new AssertionError("This should not occur!");
    }

}
