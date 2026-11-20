package studios.versel.alchemy;

import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.entity.ModBlockEntities;
import studios.versel.alchemy.item.ModItems;
import studios.versel.alchemy.menu.ModMenus;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonAlchemy {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModMenus.register();
    }
}