package studios.versel.alchemy.fabric.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    private final HashMap<String, Block> BLOCKS_REGISTRY = new HashMap<>();

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
