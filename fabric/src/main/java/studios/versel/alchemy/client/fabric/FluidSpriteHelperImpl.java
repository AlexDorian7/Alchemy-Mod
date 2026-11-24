package studios.versel.alchemy.client.fabric;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidSpriteHelperImpl {

    public static TextureAtlasSprite getStillSprite(Fluid fluid) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return null;
        }

        FluidRenderHandler handler =
                FluidRenderHandlerRegistry.INSTANCE.get(fluid);

        if (handler == null) {
            return null;
        }

        TextureAtlasSprite[] sprites = handler.getFluidSprites(
                Minecraft.getInstance().level,
                BlockPos.ZERO,
                fluid.defaultFluidState()
        );

        if (sprites == null || sprites.length == 0) {
            return null;
        }

        return sprites[0];
    }

    public static TextureAtlasSprite getFlowSprite(Fluid fluid) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return null;
        }

        FluidRenderHandler handler =
                FluidRenderHandlerRegistry.INSTANCE.get(fluid);

        if (handler == null) {
            return null;
        }

        TextureAtlasSprite[] sprites = handler.getFluidSprites(
                Minecraft.getInstance().level,
                BlockPos.ZERO,
                fluid.defaultFluidState()
        );

        if (sprites == null || sprites.length == 0) {
            return null;
        }

        return sprites[1];
    }

    public static int getTintColor(
            Fluid fluid,
            BlockAndTintGetter level,
            BlockPos pos
    ) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return 0xFFFFFFFF;
        }

        FluidRenderHandler handler =
                FluidRenderHandlerRegistry.INSTANCE.get(fluid);

        if (handler == null) {
            return 0xFFFFFFFF;
        }

        return handler.getFluidColor(
                level,
                pos,
                fluid.defaultFluidState()
        );
    }
}