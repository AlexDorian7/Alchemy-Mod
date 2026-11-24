package studios.versel.alchemy.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;

public class FluidSpriteHelper {

    @ExpectPlatform
    public static TextureAtlasSprite getStillSprite(Fluid fluid) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static TextureAtlasSprite getFlowSprite(Fluid fluid) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getTintColor(Fluid fluid, BlockAndTintGetter level, BlockPos pos) {
        throw new AssertionError();
    }
}