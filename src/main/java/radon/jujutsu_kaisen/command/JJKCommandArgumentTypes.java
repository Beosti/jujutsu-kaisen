package radon.jujutsu_kaisen.command;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import radon.jujutsu_kaisen.JujutsuKaisen;

public class JJKCommandArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, JujutsuKaisen.MOD_ID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> CURSED_TECHNIQUE = COMMAND_ARGUMENT_TYPES.register("cursed_technique", () ->
            ArgumentTypeInfos.registerByClass(CursedTechniqueArgument.class, SingletonArgumentInfo.contextFree(CursedTechniqueArgument::cursedTechnique)));
}
