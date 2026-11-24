package studios.versel.alchemy.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import studios.versel.alchemy.client.FluidSpriteHelper;
import studios.versel.alchemy.menu.AlchemyMenu;

public class AlchemyScreen extends AbstractContainerScreen<AlchemyMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath("alchemy", "textures/gui/menu.png");

    private static final int TANK_WIDTH = 32;
    private static final int TANK_HEIGHT = 128;

    public AlchemyScreen(AlchemyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        graphics.blit(
                BACKGROUND_TEXTURE,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );

        // idk why these are backwards. We should look into this
        // These are backwards because when you face the table you mirror left and right. Left and right are from the table's facing direction
        renderTank(graphics, 1, leftPos + 4, topPos + 64);
        renderTank(graphics, 0, leftPos + 220, topPos + 64);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderTank(
            GuiGraphics graphics,
            int port,
            int x,
            int y
    ) {
        long amount = menu.getFluidAmount(port);
        long capacity = menu.getCapacity(port);

        // Tank background.
        graphics.fill(
                x,
                y,
                x + TANK_WIDTH,
                y + TANK_HEIGHT,
                0xFF303030
        );

        if (amount > 0 && capacity > 0) {
            float percentage = Math.min(1.0f, (float) amount / capacity);
            int fillHeight = Math.round(TANK_HEIGHT * percentage);

            if (fillHeight > 0) {
                TextureAtlasSprite sprite =
                        FluidSpriteHelper.getStillSprite(menu.getFluid(port));

                if (sprite != null) {
                    BlockPos portPos = port == 0
                            ? AlchemyMenu.getLeftPortPos(
                            menu.getTablePosition(),
                            Minecraft.getInstance().level
                                    .getBlockState(menu.getTablePosition())
                                    .getValue(HorizontalDirectionalBlock.FACING)
                    )
                            : AlchemyMenu.getRightPortPos(
                            menu.getTablePosition(),
                            Minecraft.getInstance().level
                                    .getBlockState(menu.getTablePosition())
                                    .getValue(HorizontalDirectionalBlock.FACING)
                    );

                    int tint = FluidSpriteHelper.getTintColor(
                            menu.getFluid(port),
                            Minecraft.getInstance().level,
                            portPos
                    );

                    float red = ((tint >> 16) & 0xFF) / 255.0F;
                    float green = ((tint >> 8) & 0xFF) / 255.0F;
                    float blue = (tint & 0xFF) / 255.0F;
                    float alpha = ((tint >> 24) & 0xFF) / 255.0F;

                    graphics.setColor(red, green, blue, alpha);

                    renderTiledFluid(
                            graphics,
                            sprite,
                            x,
                            y + 128 - fillHeight,
                            32,
                            fillHeight
                    );

                    graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }

        // Tank outline.
        graphics.renderOutline(
                x,
                y,
                TANK_WIDTH,
                TANK_HEIGHT,
                0xFF000000
        );
    }

    private void renderTiledFluid(
            GuiGraphics graphics,
            TextureAtlasSprite sprite,
            int x,
            int y,
            int width,
            int height
    ) {
        if (sprite == null || width <= 0 || height <= 0) {
            return;
        }

        // Clip rendering to the tank's boundaries.
        graphics.enableScissor(x, y, x + width, y + height);

        // Repeat the fluid texture in 16x16 tiles.
        for (int tileY = 0; tileY < height; tileY += 16) {
            for (int tileX = 0; tileX < width; tileX += 16) {
                graphics.blit(
                        x + tileX,
                        y + tileY,
                        0,
                        16,
                        16,
                        sprite
                );
            }
        }

        graphics.disableScissor();
    }
}
