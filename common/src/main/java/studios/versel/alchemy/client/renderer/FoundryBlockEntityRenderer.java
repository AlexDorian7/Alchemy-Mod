package studios.versel.alchemy.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.entity.FoundryBlockEntity;
import studios.versel.alchemy.block.entity.FoundryPortEntity;
import studios.versel.alchemy.block.states.ModBlockProperties;
import studios.versel.alchemy.client.FluidSpriteHelper;
import studios.versel.alchemy.fluid.FluidTank;

public class FoundryBlockEntityRenderer
        implements BlockEntityRenderer<FoundryBlockEntity> {

    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderDispatcher;

    public FoundryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(
            FoundryBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        if (blockEntity.getLevel() == null) {
            return;
        }

        if (!blockEntity.getBlockState().getValue(ModBlockProperties.COMPLETED)) return;

        Direction back = blockEntity.getBlockState()
                .getValue(HorizontalDirectionalBlock.FACING)
                .getOpposite();

        BlockPos cavityPos = blockEntity.getBlockPos().relative(back);

        BlockPos fuelPos = blockEntity.getBlockPos()
                .relative(back, 2)
                .below();

        int light = LevelRenderer.getLightColor(
                blockEntity.getLevel(),
                cavityPos.above()
        );

        int fuelLight = LevelRenderer.getLightColor(
                blockEntity.getLevel(),
                fuelPos
        );

        poseStack.pushPose();

        // Move rendering into the two-block space behind the Foundry.
        poseStack.translate(
                back.getStepX(),
                0.0D,
                back.getStepZ()
        );

        // Render the Foundry's main fluid in the cavity.
        renderFluid(blockEntity, poseStack, bufferSource, light, packedOverlay);

        // Render the fuel tank at the fuel port's position.
        poseStack.pushPose();

        poseStack.translate(
                back.getStepX(),
                0,
                back.getStepZ()
        );

        renderFuelTank(blockEntity, poseStack, bufferSource, fuelLight, packedOverlay);

        poseStack.popPose();

        ItemStack stack = blockEntity.getActiveStack();

        if (!stack.isEmpty()) {
            poseStack.pushPose();

            // Center the item in the two-block-tall cavity.
            poseStack.translate(0.5D, 0.5D, 0.5D);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    light,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );

            poseStack.popPose();
        }

        poseStack.translate(
                back.getStepX(),
                0.0D,
                back.getStepZ()
        );

        // Rotate the model around the center of the block.
        poseStack.translate(0.5D, 0.0D, 0.5D);

        poseStack.mulPose(
                Axis.YP.rotationDegrees(-back.toYRot())
        );

        poseStack.translate(-0.5D, 0.0D, -0.5D);


        blockRenderDispatcher.renderSingleBlock(ModBlocks.FOUNDRY_PORT_PLANT.get().defaultBlockState(), poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.popPose();
    }

    private void renderFluid(
            FoundryBlockEntity blockEntity,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        FluidStack fluidStack = blockEntity.getTank().getFluid();

        if (fluidStack.isEmpty()) {
            return;
        }

        Direction back = blockEntity.getBlockState()
                .getValue(HorizontalDirectionalBlock.FACING)
                .getOpposite();

        BlockPos fluidPos = blockEntity.getBlockPos().relative(back);

        Fluid fluid = fluidStack.getFluid();

        TextureAtlasSprite stillSprite = FluidSpriteHelper.getStillSprite(fluid);
        TextureAtlasSprite flowSprite = FluidSpriteHelper.getFlowSprite(fluid);

        int tint = FluidSpriteHelper.getTintColor(
                fluid,
                blockEntity.getLevel(),
                fluidPos
        );

        float fillPercentage = (float) fluidStack.getAmount()
                / (float) blockEntity.getTank().getCapacity();

        float fluidHeight = 2.0F * fillPercentage;

        renderFluidVolume(
                poseStack,
                bufferSource,
                stillSprite,
                flowSprite,
                tint,
                packedLight,
                packedOverlay,
                0.0F, 1.0F,
                0.0F, 1.0F,
                0.0F, fluidHeight
        );
    }

    private void renderFuelTank(
            FoundryBlockEntity blockEntity,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        Direction back = blockEntity.getBlockState()
                .getValue(HorizontalDirectionalBlock.FACING)
                .getOpposite();

        BlockPos pos = blockEntity.getBlockPos()
                .relative(back, 2)
                .below();

        BlockEntity blockEntityAtPos = blockEntity.getLevel().getBlockEntity(pos);

        if (!(blockEntityAtPos instanceof FoundryPortEntity foundryPortEntity)) {
            return;
        }

        FluidTank tank = foundryPortEntity.getTank();
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack.isEmpty()) {
            return;
        }

        Fluid fluid = fluidStack.getFluid();

        TextureAtlasSprite stillSprite = FluidSpriteHelper.getStillSprite(fluid);
        TextureAtlasSprite flowSprite = FluidSpriteHelper.getFlowSprite(fluid);

        int tint = FluidSpriteHelper.getTintColor(
                fluid,
                blockEntity.getLevel(),
                pos
        );

        float fillPercentage = (float) fluidStack.getAmount()
                / (float) tank.getCapacity();

        float fluidHeight = 1.75F * fillPercentage;

        renderFluidVolume(
                poseStack,
                bufferSource,
                stillSprite,
                flowSprite,
                tint,
                packedLight,
                packedOverlay,
                0.25F, 0.75F,
                0.25F, 0.75F,
                0.0F, fluidHeight
        );
    }

    private void renderFluidVolume(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            TextureAtlasSprite stillSprite,
            TextureAtlasSprite flowSprite,
            int tint,
            int packedLight,
            int packedOverlay,
            float minX,
            float maxX,
            float minZ,
            float maxZ,
            float minY,
            float maxY
    ) {
        if (maxY <= minY) {
            return;
        }

        float red = FastColor.ARGB32.red(tint) / 255.0F;
        float green = FastColor.ARGB32.green(tint) / 255.0F;
        float blue = FastColor.ARGB32.blue(tint) / 255.0F;
        float alpha = FastColor.ARGB32.alpha(tint) / 255.0F;

        PoseStack.Pose pose = poseStack.last();

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.translucent()
        );

        /*
         * Top face.
         */
        addFluidQuad(
                consumer,
                pose,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                stillSprite.getU0(),
                stillSprite.getV0(),
                stillSprite.getU0(),
                stillSprite.getV1(),
                stillSprite.getU1(),
                stillSprite.getV1(),
                stillSprite.getU1(),
                stillSprite.getV0(),
                red, green, blue, alpha,
                packedLight, packedOverlay,
                0.0F, 1.0F, 0.0F
        );

        /*
         * Side faces.
         *
         * Each block of vertical height uses one complete flow texture.
         * The final section uses a proportional portion of the texture.
         */

        // North (-Z).
        renderFlowSide(
                consumer, pose, flowSprite,
                minX, minZ,
                maxX, minZ,
                minY, maxY,
                red, green, blue, alpha,
                packedLight, packedOverlay,
                0.0F, 0.0F, -1.0F
        );

        // South (+Z).
        renderFlowSide(
                consumer, pose, flowSprite,
                maxX, maxZ,
                minX, maxZ,
                minY, maxY,
                red, green, blue, alpha,
                packedLight, packedOverlay,
                0.0F, 0.0F, 1.0F
        );

        // West (-X).
        renderFlowSide(
                consumer, pose, flowSprite,
                minZ, minX,
                maxZ, minX,
                minY, maxY,
                red, green, blue, alpha,
                packedLight, packedOverlay,
                -1.0F, 0.0F, 0.0F
        );

        // East (+X).
        renderFlowSide(
                consumer, pose, flowSprite,
                maxZ, maxX,
                minZ, maxX,
                minY, maxY,
                red, green, blue, alpha,
                packedLight, packedOverlay,
                1.0F, 0.0F, 0.0F
        );
    }

    private void renderFlowSide(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            TextureAtlasSprite sprite,
            float startHorizontal,
            float startFixed,
            float endHorizontal,
            float endFixed,
            float minY,
            float maxY,
            float red,
            float green,
            float blue,
            float alpha,
            int packedLight,
            int packedOverlay,
            float normalX,
            float normalY,
            float normalZ
    ) {
        float minU = sprite.getU0();
        float minV = sprite.getV0();

        // Sample only the first 16x16 pixels of the 32x32 sprite.
        float maxU = minU + (sprite.getU1() - minU) * 0.5F;
        float maxV = minV + (sprite.getV1() - minV) * 0.5F;

        // Determine the actual width of the face.
        float horizontalWidth = Math.abs(endHorizontal - startHorizontal);

        if (horizontalWidth <= 0.0F) {
            return;
        }

        // Repeat the texture vertically once per face-width world units.
        float currentY = minY;

        while (currentY < maxY) {
            float sectionHeight = Math.min(horizontalWidth, maxY - currentY);
            float nextY = currentY + sectionHeight;

            // Scale V according to the section's height.
            float sectionV = (maxV - minV) * (sectionHeight / horizontalWidth);

            // Flip V vertically.
            float bottomV = minV + sectionV;
            float topV = minV;

            float x1;
            float z1;
            float x2;
            float z2;

            if (normalX != 0.0F) {
                x1 = startFixed;
                z1 = startHorizontal;
                x2 = endFixed;
                z2 = endHorizontal;
            } else {
                x1 = startHorizontal;
                z1 = startFixed;
                x2 = endHorizontal;
                z2 = endFixed;
            }

            if (normalX != 0.0F) {
                addFluidQuad(
                        consumer,
                        pose,
                        x2, currentY, z2,
                        x2, nextY, z2,
                        x1, nextY, z1,
                        x1, currentY, z1,
                        maxU, bottomV,
                        maxU, topV,
                        minU, topV,
                        minU, bottomV,
                        red, green, blue, alpha,
                        packedLight, packedOverlay,
                        normalX, normalY, normalZ
                );
            } else {
                addFluidQuad(
                        consumer,
                        pose,
                        x1, currentY, z1,
                        x1, nextY, z1,
                        x2, nextY, z2,
                        x2, currentY, z2,
                        minU, bottomV,
                        minU, topV,
                        maxU, topV,
                        maxU, bottomV,
                        red, green, blue, alpha,
                        packedLight, packedOverlay,
                        normalX, normalY, normalZ
                );
            }

            currentY = nextY;
        }
    }

    private void addFluidQuad(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            float u1, float v1,
            float u2, float v2,
            float u3, float v3,
            float u4, float v4,
            float red,
            float green,
            float blue,
            float alpha,
            int packedLight,
            int packedOverlay,
            float normalX,
            float normalY,
            float normalZ
    ) {
        consumer.addVertex(pose, x1, y1, z1)
                .setColor(red, green, blue, alpha)
                .setUv(u1, v1)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(pose, x2, y2, z2)
                .setColor(red, green, blue, alpha)
                .setUv(u2, v2)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(pose, x3, y3, z3)
                .setColor(red, green, blue, alpha)
                .setUv(u3, v3)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(pose, x4, y4, z4)
                .setColor(red, green, blue, alpha)
                .setUv(u4, v4)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);
    }

    @Override
    public boolean shouldRenderOffScreen(FoundryBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(FoundryBlockEntity blockEntity, Vec3 vec3) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, vec3);
    }

    // For neoforge
    public AABB getRenderBoundingBox(FoundryBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();

        return new AABB(
                pos.getX() - 2.0D,
                pos.getY() - 1.0D,
                pos.getZ() - 2.0D,
                pos.getX() + 3.0D,
                pos.getY() + 2.0D,
                pos.getZ() + 3.0D
        );
    }
}