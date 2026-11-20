package studios.versel.alchemy.client.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class FluidSpriteHelperImpl {

    public static TextureAtlasSprite getStillSprite(Fluid fluid) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return null;
        }

        ResourceLocation texture =
                IClientFluidTypeExtensions.of(fluid).getStillTexture();

        if (texture == null) {
            return null;
        }

        return Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(texture);
    }

    public static int getTintColor(
            Fluid fluid,
            BlockAndTintGetter level,
            BlockPos pos
    ) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return 0xFFFFFFFF;
        }

        return IClientFluidTypeExtensions.of(fluid)
                .getTintColor(
                        fluid.defaultFluidState(),
                        level,
                        pos
                );
    }
}