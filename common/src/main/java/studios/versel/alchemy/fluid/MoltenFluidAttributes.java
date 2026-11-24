package studios.versel.alchemy.fluid;

import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static studios.versel.alchemy.Constants.MOD_ID;

public class MoltenFluidAttributes extends SimpleArchitecturyFluidAttributes {

    private static final ImprovedNoise NOISE =
            new ImprovedNoise(RandomSource.create(1337L));

    private final int color;

    protected MoltenFluidAttributes(
            Supplier<? extends Fluid> flowingFluid,
            Supplier<? extends Fluid> sourceFluid,
            int color
    ) {
        super(flowingFluid, sourceFluid);

        this.color = color;

        sourceTexture(ResourceLocation.fromNamespaceAndPath(
                MOD_ID, "block/molten_still"
        ));

        flowingTexture(ResourceLocation.fromNamespaceAndPath(
                MOD_ID, "block/molten_flow"
        ));

        color(color);

        // Physical properties
        density(3000);
        viscosity(6000);
        temperature(1500);

        // Lava-like flow properties
        tickDelay(30);
        slopeFindDistance(2);
        dropOff(2);
        convertToSource(false);
    }

    @Override
    public int getColor(
            @Nullable FluidStack stack,
            @Nullable BlockAndTintGetter level,
            @Nullable BlockPos pos
    ) {
        if (pos == null || level == null || stack == null) {
            return color;
        }

        double n = remap(NOISE.noise(
                pos.getX(),
                pos.getY(),
                pos.getZ()
        ));

        double rf = getFactor(FastColor.ARGB32.red(color));
        double gf = getFactor(FastColor.ARGB32.green(color));
        double bf = getFactor(FastColor.ARGB32.blue(color));

        return FastColor.ARGB32.color(
                255,
                makeInt(n * rf),
                makeInt(n * gf),
                makeInt(n * bf)
        );
    }

    private static double getFactor(int i) {
        return i / 256D;
    }

    private static int makeInt(double d) {
        return Mth.floor(d * 256);
    }

    private static double remap(double n) {
        n += 1;
        n /= 2;
        n /= 2;
        n += 0.5;

        return n;
    }

    @Override
    public int getTickDelay(@Nullable LevelReader level) {
        return level != null && level.dimensionType().ultraWarm()
                ? 10
                : 30;
    }

    @Override
    public int getSlopeFindDistance(@Nullable LevelReader level) {
        return level != null && level.dimensionType().ultraWarm()
                ? 4
                : 2;
    }

    @Override
    public int getDropOff(@Nullable LevelReader level) {
        return level != null && level.dimensionType().ultraWarm()
                ? 1
                : 2;
    }

}