package radon.jujutsu_kaisen.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.block.domain.*;
import radon.jujutsu_kaisen.block.fluid.JJKFluids;

import javax.annotation.Nullable;

public class JJKBlocks {
    private static boolean always(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return false;
    }

    private static boolean never(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return false;
    }

    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, JujutsuKaisen.MOD_ID);

    public static final DeferredHolder<Block, Block> METEOR = BLOCKS.register("meteor", () -> new MagmaBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .lightLevel(pState -> 3)
            .hasPostProcess(JJKBlocks::always)
            .emissiveRendering(JJKBlocks::always)));

    public static DeferredHolder<Block, DomainBlock> DOMAIN = BLOCKS.register("domain", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel(pState -> 14)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainAirBlock> DOMAIN_AIR = BLOCKS.register("domain_air", () ->
            new DomainAirBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noLootTable()
                    .air()));

    public static DeferredHolder<Block, DomainBlock> DAY_SKY = BLOCKS.register("day_sky", () ->
            new DaySkyBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));

    public static DeferredHolder<Block, DomainBlock> NIGHT_SKY = BLOCKS.register("night_sky", () ->
            new NightSkyBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));


    public static DeferredHolder<Block, DomainBlock> UNLIMITED_VOID = BLOCKS.register("unlimited_void", () ->
            new UnlimitedVoidBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));

    public static DeferredHolder<Block, DomainBlock> COFFIN_OF_THE_IRON_MOUNTAIN_ONE = BLOCKS.register("coffin_of_the_iron_mountain_one", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()
                    .lightLevel((pState) -> 14)
                    .emissiveRendering((pState, pLevel, pPos) -> true)));
    public static DeferredHolder<Block, DomainBlock> COFFIN_OF_THE_IRON_MOUNTAIN_TWO = BLOCKS.register("coffin_of_the_iron_mountain_two", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainBlock> COFFIN_OF_THE_IRON_MOUNTAIN_THREE = BLOCKS.register("coffin_of_the_iron_mountain_three", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()));

    public static DeferredHolder<Block, DomainBlock> HORIZON_OF_THE_CAPTIVATING_SKANDHA_FILL = BLOCKS.register("horizon_of_the_datativating_skandha_fill", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));

    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_FILL = BLOCKS.register("shining_sea_of_flowers_fill", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_FLOOR = BLOCKS.register("shining_sea_of_flowers_floor", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_DECORATION_ONE = BLOCKS.register("shining_sea_of_flowers_decoration_one", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()
                    .lightLevel((pState) -> 14)
                    .emissiveRendering((pState, pLevel, pPos) -> true)));
    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_DECORATION_TWO = BLOCKS.register("shining_sea_of_flowers_decoration_two", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()
                    .lightLevel((pState) -> 14)
                    .emissiveRendering((pState, pLevel, pPos) -> true)));
    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_DECORATION_THREE = BLOCKS.register("shining_sea_of_flowers_decoration_three", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()
                    .lightLevel((pState) -> 14)
                    .emissiveRendering((pState, pLevel, pPos) -> true)));
    public static DeferredHolder<Block, DomainBlock> SHINING_SEA_OF_FLOWERS_DECORATION_FOUR = BLOCKS.register("shining_sea_of_flowers_decoration_four", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .noLootTable()
                    .lightLevel((pState) -> 14)
                    .emissiveRendering((pState, pLevel, pPos) -> true)));

    public static DeferredHolder<Block, DomainBlock> ALL_ENCOMPASSING_UNEQUIVOCAL_LOVE_ONE = BLOCKS.register("all_encompassing_unequivocal_love_one", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainBlock> ALL_ENCOMPASSING_UNEQUIVOCAL_LOVE_TWO = BLOCKS.register("all_encompassing_unequivocal_love_two", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));
    public static DeferredHolder<Block, DomainBlock> ALL_ENCOMPASSING_UNEQUIVOCAL_LOVE_THREE = BLOCKS.register("all_encompassing_unequivocal_love_three", () ->
            new DomainBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 8.0F)
                    .isSuffocating(JJKBlocks::never)
                    .lightLevel((pState) -> 14)
                    .noLootTable()));

    public static DeferredHolder<Block, ChimeraShadowGardenBlock> CHIMERA_SHADOW_GARDEN = BLOCKS.register("chimera_shadow_garden", () ->
            new ChimeraShadowGardenBlock(JJKFluids.CHIMERA_SHADOW_GARDEN_SOURCE, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static DeferredHolder<Block, Block> DISPLAY_CASE = BLOCKS.register("display_case", () ->
            new DisplayCaseBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    public static DeferredHolder<Block, Block> ALTAR = BLOCKS.register("altar", () ->
            new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 1200.0F)
                    .sound(SoundType.ANVIL)));

    public static DeferredHolder<Block, Block> VEIL_ROD = BLOCKS.register("veil_rod", () ->
            new VeilRodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.COPPER)
                    .noOcclusion()));
    public static DeferredHolder<Block, Block> VEIL = BLOCKS.register("veil", () ->
            new VeilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .strength(100.0F, 14.0F)
                    .isViewBlocking(JJKBlocks::never)
                    .isSuffocating(JJKBlocks::never)
                    .noOcclusion()));

    public static DeferredHolder<Block, FakeWaterDurationBlock> FAKE_WATER_DURATION = BLOCKS.register("fake_water_duration", () ->
            new FakeWaterDurationBlock(JJKFluids.FAKE_WATER_SOURCE, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static DeferredHolder<Block, FakeWaterDomainBlock> FAKE_WATER_DOMAIN = BLOCKS.register("fake_water_domain", () ->
            new FakeWaterDomainBlock(JJKFluids.FAKE_WATER_SOURCE, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static DeferredHolder<Block, FakeWoodBlock> FAKE_WOOD = BLOCKS.register("fake_wood", () ->
            new FakeWoodBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .isSuffocating(JJKBlocks::never)
                    .ignitedByLava()));
}
